package com.sudoku.sudokucodex.controller;

import com.sudoku.sudokucodex.model.CellPosition;
import com.sudoku.sudokucodex.model.SudokuBoard;
import com.sudoku.sudokucodex.model.SudokuPuzzle;
import com.sudoku.sudokucodex.model.SudokuRule;
import com.sudoku.sudokucodex.model.SudokuRuleAdapter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * Controller of the Sudoku game view.
 */
public class GameController {

    /** Cuadrícula visual del tablero de Sudoku. */
    @FXML
    private GridPane boardGrid;

    /** Etiqueta de texto para mostrar mensajes al usuario. */
    @FXML
    private Label messageLabel;

    /** Etiqueta para contabilizar las ayudas empleadas por el jugador. */
    @FXML
    private Label hintCounterLabel;

    /** Instancia del tablero de Sudoku (modelo). */
    private final SudokuBoard board = new SudokuBoard();

    /** Representa el puzzle lógico y su solución. */
    private SudokuPuzzle puzzle;

    /** Adaptador para validar las reglas de juego del Sudoku. */
    private final SudokuRule rule = new SudokuRuleAdapter();

    /** Generador de números aleatorios para seleccionar la celda de ayuda. */
    private final Random random = new Random();

    /** Conjunto de llaves correspondientes a las celdas con ayudas reveladas. */
    private final Set<String> hintedCells = new HashSet<>();

    /** Matriz de campos de texto del tablero en la UI. */
    private TextField[][] cells;

    /** Solución actual del tablero completo. */
    private int[][] solution;

    /** Número de ayudas usadas en la partida actual. */
    private int hintsUsed;

    /** Controla si la partida ya ha sido iniciada. */
    private boolean gameStarted;

    /** Bandera para evitar que se muestre el aviso de victoria más de una vez. */
    private boolean winAnnounced;

    /**
     * Indica si la vista se está actualizando internamente para no activar
     * listeners.
     */
    private boolean updatingView;

    /**
     * Initializes the visual board and the empty game state.
     */
    @FXML
    public void initialize() {
        cells = new TextField[SudokuBoard.SIZE][SudokuBoard.SIZE];
        puzzle = new SudokuPuzzle();
        solution = puzzle.copySolution();
        createVisualBoard();
        updateHintCounter();
        messageLabel.setText("Presiona Nuevo juego para comenzar.");
    }

    /**
     * Starts a new game after user confirmation.
     */
    @FXML
    private void startNewGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nuevo juego");
        alert.setHeaderText("Iniciar una nueva partida");
        alert.setContentText("Se reiniciara el tablero actual.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            puzzle = new SudokuPuzzle(); // NUEVA plantilla aleatoria

            board.loadPuzzle(puzzle.copyPuzzle());

            solution = puzzle.copySolution(); // si tienes esta variable

            hintsUsed = 0;
            gameStarted = true;
            hintedCells.clear();
            winAnnounced = false;

            refreshBoardFromModel();
            updateHintCounter();

            messageLabel.setText("Juego iniciado. Completa las casillas vacias.");
        }
    }

    /**
     * Gives a valid hint for one empty cell without completing the whole board.
     */
    @FXML
    private void requestHint() {
        if (!gameStarted) {
            messageLabel.setText("Primero inicia una partida.");
            return;
        }

        List<CellPosition> emptyPositions = board.emptyPositions();
        if (emptyPositions.size() <= 1) {
            messageLabel.setText("La ayuda no puede completar la ultima casilla.");
            return;
        }

        CellPosition position = emptyPositions.get(random.nextInt(emptyPositions.size()));
        int row = position.getRow();
        int column = position.getColumn();
        int value = solution[row][column];

        board.setValue(row, column, value);
        hintedCells.add(key(row, column));
        updatingView = true;
        cells[row][column].setText(String.valueOf(value));
        updatingView = false;
        hintsUsed++;

        updateHintCounter();
        validateCurrentBoard();
        messageLabel.setText("Ayuda aplicada en una casilla resaltada.");
    }

    /**
     * Clears editable player values while keeping the original puzzle clues.
     */
    @FXML
    private void clearEditableCells() {
        if (!gameStarted) {
            messageLabel.setText("No hay una partida activa.");
            return;
        }

        int[][] originalPuzzle = puzzle.copyPuzzle();
        board.loadPuzzle(originalPuzzle);
        hintedCells.clear();
        hintsUsed = 0;
        winAnnounced = false;
        refreshBoardFromModel();
        updateHintCounter();
        messageLabel.setText("Casillas editables limpiadas.");
    }

    /**
     * Construye la cuadrícula visual de celdas y les asocia sus escuchadores de
     * eventos.
     */
    private void createVisualBoard() {
        boardGrid.getChildren().clear();

        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int column = 0; column < SudokuBoard.SIZE; column++) {
                TextField cell = new TextField();
                cell.getStyleClass().add("sudoku-cell");
                cell.setStyle(borderStyle(row, column));
                cell.setEditable(false);
                cell.setFocusTraversable(true);
                cell.setOnMouseClicked(event -> cell.selectAll());
                cell.textProperty().addListener(new CellInputListener(row, column, cell));

                cells[row][column] = cell;
                boardGrid.add(cell, column, row);
            }
        }
    }

    /**
     * Actualiza la interfaz gráfica con los valores almacenados en el modelo del
     * tablero.
     */
    private void refreshBoardFromModel() {
        updatingView = true;
        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int column = 0; column < SudokuBoard.SIZE; column++) {
                int value = board.getValue(row, column);
                TextField cell = cells[row][column];

                cell.setText(value == SudokuBoard.EMPTY ? "" : String.valueOf(value));
                cell.setEditable(gameStarted && !board.isFixedCell(row, column));
            }
        }
        updatingView = false;

        validateCurrentBoard();
    }

    /**
     * Revisa si el tablero contiene errores en filas, columnas o bloques, o si ya
     * se ganó.
     */
    private void validateCurrentBoard() {
        int[][] values = board.copyValues();
        boolean hasErrors = false;

        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int column = 0; column < SudokuBoard.SIZE; column++) {
                TextField cell = cells[row][column];
                cell.getStyleClass().removeAll("fixed-cell", "hint-cell", "error-cell", "editable-cell");

                if (board.isFixedCell(row, column)) {
                    cell.getStyleClass().add("fixed-cell");
                } else if (hintedCells.contains(key(row, column))) {
                    cell.getStyleClass().add("hint-cell");
                } else {
                    cell.getStyleClass().add("editable-cell");
                }

                if (!rule.isValidValue(values, row, column)) {
                    hasErrors = true;
                    cell.getStyleClass().add("error-cell");
                }
            }
        }

        if (hasErrors) {
            messageLabel.setText("Hay numeros repetidos en una fila, columna o bloque.");
            return;
        }

        if (gameStarted && board.isComplete() && rule.isValidBoard(values) && !winAnnounced) {
            winAnnounced = true;
            messageLabel.setText("Felicitaciones, resolviste el Sudoku.");
            showWinAlert();
        } else if (gameStarted) {
            messageLabel.setText("Tablero valido. Continua jugando.");
        }
    }

    /**
     * Despliega una alerta emergente para notificar al usuario que ha ganado el
     * juego.
     */
    private void showWinAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sudoku");
        alert.setHeaderText("Partida completada");
        alert.setContentText("El tablero cumple todas las reglas.");
        alert.showAndWait();
    }

    /**
     * Actualiza la etiqueta de ayuda para mostrar la cantidad total de ayudas
     * consumidas.
     */
    private void updateHintCounter() {
        hintCounterLabel.setText("Ayudas usadas: " + hintsUsed);
    }

    /**
     * Determina el grosor de los bordes para separar visualmente los bloques del
     * Sudoku.
     *
     * @param row    Fila de la celda.
     * @param column Columna de la celda.
     * @return Estilo CSS correspondiente al grosor de los bordes.
     */
    private String borderStyle(int row, int column) {
        int top = row == 0 ? 3 : 1;
        int right = column == SudokuBoard.SIZE - 1 || column == 2 ? 3 : 1;
        int bottom = row == SudokuBoard.SIZE - 1 || row == 1 || row == 3 ? 3 : 1;
        int left = column == 0 ? 3 : 1;

        return "-fx-border-width: " + top + "px "
                + right + "px " + bottom + "px " + left + "px;";
    }

    /**
     * Convierte la fila y columna en una cadena de texto para usarla como clave.
     *
     * @param row    Fila de la celda.
     * @param column Columna de la celda.
     * @return Cadena que representa las coordenadas de la celda.
     */
    private String key(int row, int column) {
        return row + ":" + column;
    }

    /**
     * Inner listener that handles keyboard changes for a single cell.
     */
    private class CellInputListener implements ChangeListener<String> {

        /** Fila a la que pertenece el listener de entrada. */
        private final int row;

        /** Columna a la que pertenece el listener de entrada. */
        private final int column;

        /** Campo de texto asociado a este listener de entrada. */
        private final TextField cell;

        /**
         * Constructor del escuchador para detectar cambios en una celda.
         *
         * @param row    Fila de la celda.
         * @param column Columna de la celda.
         * @param cell   Campo de texto gráfico de la celda.
         */
        CellInputListener(int row, int column, TextField cell) {
            this.row = row;
            this.column = column;
            this.cell = cell;
        }

        /**
         * Accepts only one digit from 1 to 6 or an empty value.
         *
         * @param observable observed text property.
         * @param oldValue   previous cell text.
         * @param newValue   new cell text.
         */
        @Override
        public void changed(ObservableValue<? extends String> observable,
                String oldValue,
                String newValue) {
            if (!gameStarted || board.isFixedCell(row, column)) {
                return;
            }

            if (updatingView) {
                return;
            }

            if (!newValue.matches("[1-6]?")) {
                cell.setText(oldValue);
                return;
            }

            int value = newValue.isEmpty()
                    ? SudokuBoard.EMPTY
                    : Integer.parseInt(newValue);

            hintedCells.remove(key(row, column));
            board.setValue(row, column, value);
            validateCurrentBoard();
        }
    }
}

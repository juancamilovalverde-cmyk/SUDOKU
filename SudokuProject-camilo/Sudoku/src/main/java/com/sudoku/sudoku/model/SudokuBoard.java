package com.sudoku.sudokucodex.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model that stores the Sudoku board values and fixed starting cells.
 */
public class SudokuBoard {

    /** Tamaño del tablero de Sudoku (6x6). */
    public static final int SIZE = 6;

    /** Valor representativo para una celda vacía. */
    public static final int EMPTY = 0;

    /** Matriz de enteros que guarda los valores actuales del tablero. */
    private final int[][] values;

    /** Matriz de booleanos para indicar qué celdas son pistas fijas iniciales. */
    private final boolean[][] fixedCells;

    /**
     * Creates an empty 6x6 Sudoku board.
     */
    public SudokuBoard() {
        values = new int[SIZE][SIZE];
        fixedCells = new boolean[SIZE][SIZE];
    }

    /**
     * Loads a puzzle and marks the provided values as fixed clues.
     *
     * @param puzzle 6x6 matrix with zeroes for empty cells.
     */
    public void loadPuzzle(int[][] puzzle) {
        clear();

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                values[row][column] = puzzle[row][column];
                fixedCells[row][column] = puzzle[row][column] != EMPTY;
            }
        }
    }

    /**
     * Clears all values and fixed-cell markers.
     */
    public void clear() {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                values[row][column] = EMPTY;
                fixedCells[row][column] = false;
            }
        }
    }

    /**
     * Stores a value in an editable cell.
     *
     * @param row row index.
     * @param column column index.
     * @param value value between 0 and 6.
     */
    public void setValue(int row, int column, int value) {
        values[row][column] = value;
    }

    /**
     * @param row row index.
     * @param column column index.
     * @return value stored at the requested position.
     */
    public int getValue(int row, int column) {
        return values[row][column];
    }

    /**
     * @param row row index.
     * @param column column index.
     * @return true when the position is an initial clue.
     */
    public boolean isFixedCell(int row, int column) {
        return fixedCells[row][column];
    }

    /**
     * @return a defensive copy of the current board values.
     */
    public int[][] copyValues() {
        int[][] copy = new int[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(values[row], 0, copy[row], 0, SIZE);
        }

        return copy;
    }

    /**
     * @return positions that still have no value.
     */
    public List<CellPosition> emptyPositions() {
        List<CellPosition> positions = new ArrayList<>();

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (values[row][column] == EMPTY) {
                    positions.add(new CellPosition(row, column));
                }
            }
        }

        return positions;
    }

    /**
     * @return true when every cell has a value.
     */
    public boolean isComplete() {
        return emptyPositions().isEmpty();
    }
}

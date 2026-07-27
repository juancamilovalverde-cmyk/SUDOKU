package com.sudoku.sudokucodex.model;

/**
 * Represents a row and column inside the 6x6 Sudoku board.
 */
public final class CellPosition {

    /** Fila de la celda dentro del tablero de Sudoku. */
    private final int row;

    /** Columna de la celda dentro del tablero de Sudoku. */
    private final int column;

    /**
     * Creates an immutable board position.
     *
     * @param row board row.
     * @param column board column.
     */
    public CellPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * @return row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return column index.
     */
    public int getColumn() {
        return column;
    }
}

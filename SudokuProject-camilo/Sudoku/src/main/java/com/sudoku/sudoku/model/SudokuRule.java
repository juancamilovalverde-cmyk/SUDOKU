package com.sudoku.sudokucodex.model;

/**
 * Interface for classes that validate Sudoku rules.
 */
public interface SudokuRule {

    /**
     * Checks whether the value at a position respects row, column and block rules.
     *
     * @param board current board values.
     * @param row row index.
     * @param column column index.
     * @return true when the value does not conflict with another equal value.
     */
    boolean isValidValue(int[][] board, int row, int column);

    /**
     * Checks whether every filled cell on the board is valid.
     *
     * @param board current board values.
     * @return true when no filled cell violates Sudoku rules.
     */
    boolean isValidBoard(int[][] board);
}

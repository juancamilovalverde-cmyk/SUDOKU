package com.sudoku.sudokucodex.model;

/**
 * Validates Sudoku rules for a 6x6 board.
 */
public class SudokuRuleAdapter implements SudokuRule {

    /** Rows per block. */
    private static final int BLOCK_ROWS = 2;

    /** Columns per block. */
    private static final int BLOCK_COLUMNS = 3;

    /**
     * Returns true if the cell value is valid.
     *
     * @param board current board values.
     * @param row cell row index.
     * @param column cell column index.
     * @return true if no rule is violated.
     */
    @Override
    public boolean isValidValue(int[][] board, int row, int column) {
        int value = board[row][column];

        if (value == SudokuBoard.EMPTY) {
            return true;
        }

        return isUniqueInRow(board, row, column, value)
                && isUniqueInColumn(board, row, column, value)
                && isUniqueInBlock(board, row, column, value);
    }

    /**
     * Returns true if all board cells are valid.
     *
     * @param board current board values.
     * @return true if no cell violates the rules.
     */
    @Override
    public boolean isValidBoard(int[][] board) {
        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int column = 0; column < SudokuBoard.SIZE; column++) {
                if (!isValidValue(board, row, column)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks the value is unique in its row.
     *
     * @param board current board values.
     * @param row row to check.
     * @param column column to skip.
     * @param value value to find.
     * @return true if not repeated in row.
     */
    private boolean isUniqueInRow(int[][] board, int row, int column, int value) {
        for (int currentColumn = 0; currentColumn < SudokuBoard.SIZE; currentColumn++) {
            if (currentColumn != column && board[row][currentColumn] == value) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks the value is unique in its column.
     *
     * @param board current board values.
     * @param row row to skip.
     * @param column column to check.
     * @param value value to find.
     * @return true if not repeated in column.
     */
    private boolean isUniqueInColumn(int[][] board, int row, int column, int value) {
        for (int currentRow = 0; currentRow < SudokuBoard.SIZE; currentRow++) {
            if (currentRow != row && board[currentRow][column] == value) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks the value is unique in its 2x3 block.
     *
     * @param board current board values.
     * @param row cell row index.
     * @param column cell column index.
     * @param value value to find.
     * @return true if not repeated in block.
     */
    private boolean isUniqueInBlock(int[][] board, int row, int column, int value) {
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startColumn = (column / BLOCK_COLUMNS) * BLOCK_COLUMNS;

        for (int currentRow = startRow; currentRow < startRow + BLOCK_ROWS; currentRow++) {
            for (int currentColumn = startColumn;
                 currentColumn < startColumn + BLOCK_COLUMNS;
                 currentColumn++) {
                boolean samePosition = currentRow == row && currentColumn == column;
                if (!samePosition && board[currentRow][currentColumn] == value) {
                    return false;
                }
            }
        }

        return true;
    }
}
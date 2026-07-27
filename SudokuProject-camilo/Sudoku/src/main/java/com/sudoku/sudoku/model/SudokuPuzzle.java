package com.sudoku.sudokucodex.model;

import java.util.Random;

/**
 * Provides random 6x6 Sudoku puzzles and their complete solution.
 */
public class SudokuPuzzle {

    /** Generador de números aleatorios para elegir la plantilla de la partida. */
    private static final Random RANDOM = new Random();

    /** Arreglo tridimensional que almacena las plantillas de Sudoku disponibles. */
    private static final int[][][] PUZZLES = {

            // Plantilla 1
            {
                    {1, 0, 0, 4, 0, 0},
                    {0, 5, 0, 0, 2, 0},
                    {2, 0, 0, 5, 0, 0},
                    {0, 6, 0, 0, 3, 0},
                    {3, 0, 0, 6, 0, 0},
                    {0, 1, 0, 0, 4, 0}
            },

            // Plantilla 2
            {
                    {0, 2, 0, 0, 5, 0},
                    {4, 0, 0, 1, 0, 0},
                    {0, 3, 0, 0, 6, 0},
                    {5, 0, 0, 2, 0, 0},
                    {0, 4, 0, 0, 1, 0},
                    {6, 0, 0, 3, 0, 0}
            },

            // Plantilla 3
            {
                    {1, 0, 0, 0, 5, 0},
                    {0, 0, 6, 1, 0, 0},
                    {2, 0, 0, 0, 6, 0},
                    {0, 0, 1, 2, 0, 0},
                    {3, 0, 0, 0, 1, 0},
                    {0, 0, 2, 3, 0, 0}
            }
    };

    /** Matriz bidimensional que representa la solución correcta del Sudoku. */
    private static final int[][] SOLUTION = {
            {1, 2, 3, 4, 5, 6},
            {4, 5, 6, 1, 2, 3},
            {2, 3, 4, 5, 6, 1},
            {5, 6, 1, 2, 3, 4},
            {3, 4, 5, 6, 1, 2},
            {6, 1, 2, 3, 4, 5}
    };

    /** Índice del rompecabezas seleccionado al azar para jugar. */
    private final int selectedPuzzle;

    /**
     * Selects a random template when a new game is created.
     */
    public SudokuPuzzle() {
        selectedPuzzle = RANDOM.nextInt(PUZZLES.length);
    }

    /**
     * @return copy of a random puzzle.
     */
    public int[][] copyPuzzle() {
        return copyMatrix(PUZZLES[selectedPuzzle]);
    }

    /**
     * @return copy of the complete solution.
     */
    public int[][] copySolution() {
        return copyMatrix(SOLUTION);
    }

    /**
     * Realiza una copia de una matriz bidimensional.
     *
     * @param source Matriz bidimensional origen que se va a duplicar.
     * @return Nueva matriz bidimensional con el mismo contenido.
     */
    private int[][] copyMatrix(int[][] source) {
        int[][] copy = new int[SudokuBoard.SIZE][SudokuBoard.SIZE];

        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            System.arraycopy(source[row], 0, copy[row], 0, SudokuBoard.SIZE);
        }

        return copy;
    }
}
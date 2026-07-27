package com.sudoku.sudokucodex;

import javafx.application.Application;

/**
 * Launches the JavaFX application from IDEs that require a plain main method.
 */
public class Launcher {

    /**
     * Starts the Sudoku application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }
}

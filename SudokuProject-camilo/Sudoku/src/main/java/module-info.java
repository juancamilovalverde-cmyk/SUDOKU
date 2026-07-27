module com.sudoku.sudokucodex {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sudoku.sudokucodex to javafx.fxml;
    opens com.sudoku.sudokucodex.controller to javafx.fxml;

    exports com.sudoku.sudokucodex;
    exports com.sudoku.sudokucodex.controller;
    exports com.sudoku.sudokucodex.model;
}

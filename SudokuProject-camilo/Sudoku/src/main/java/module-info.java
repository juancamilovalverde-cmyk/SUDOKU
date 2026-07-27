module com.sudoku.sudoku {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.sudoku.sudoku to javafx.fxml;
    opens com.sudoku.sudoku.controller to javafx.fxml;

    exports com.sudoku.sudoku;
    exports com.sudoku.sudoku.controller;
}

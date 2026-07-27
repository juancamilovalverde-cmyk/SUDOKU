package com.sudoku.sudokucodex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for the Sudoku JavaFX application.
 */
public class MainApplication extends Application {

    /**
     * Loads the FXML view and shows the main game window.
     *
     * @param stage primary JavaFX stage.
     * @throws IOException when the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainApplication.class.getResource("game-view.fxml")
        );
        Scene scene = new Scene(loader.load(), 820, 720);
        scene.getStylesheets().add(
                MainApplication.class.getResource("styles.css").toExternalForm()
        );

        stage.setTitle("Sudoku 6x6");
        stage.setMinWidth(760);
        stage.setMinHeight(680);
        stage.setScene(scene);
        stage.show();
    }
}

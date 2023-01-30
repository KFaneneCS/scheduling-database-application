package main;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The main class from which the application is launched.
 * <p>
 * Details on the application can be found in the provided README.txt file.
 * <p>
 * javadoc folder can be found in provided zip folder.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * The main method.  Application is launched.
     */
    public static void main(String[] args) {

        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }

}
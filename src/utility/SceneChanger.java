package utility;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class that handles screen changes.
 *
 * @author Kyle Fanene
 */
public class SceneChanger {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";
    Stage stage;


    /**
     * Method which changes screen to desired location.
     *
     * @param event             Button event depending on the form from which it is called.
     * @param nextViewFileName  Name of View file to which screen will be changed.
     */
    public void changeScreen(ActionEvent event, String nextViewFileName) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/" + nextViewFileName + ".fxml"));
            loader.load();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Method which changes screen to desired location.
     *
     * @param event             Key event (pressing Enter) from Login form.
     * @param nextViewFileName  Name of View file to which screen will be changed.
     */
    public void changeScreen(KeyEvent event, String nextViewFileName) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/" + nextViewFileName + ".fxml"));
            loader.load();

            stage = (Stage) ((TextField) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

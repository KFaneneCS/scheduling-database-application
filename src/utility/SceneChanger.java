package utility;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChanger {

    String ERROR_MESSAGE = "Sorry, there was an error.";
    Stage stage;


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
//            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
//            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(e.getMessage());  // FIXME
            e.printStackTrace();
        }
    }
}

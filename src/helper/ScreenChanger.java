package helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class ScreenChanger {

    Stage stage;

    public void changeScreen(ActionEvent event, String desiredViewFileName) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/" + desiredViewFileName + ".fxml"));
            loader.load();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (IOException e) {
            AlertPopups.generateErrorMessage(e.getMessage());  // FIXME
        }
    }
}

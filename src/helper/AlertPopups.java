package helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.MissingResourceException;
import java.util.Optional;

import static translation.Translation.translate;

public class AlertPopups {

    public static void generateErrorMessage(String messageToDisplay) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            alert.setTitle(translate(("Error")));
            alert.setContentText(translate(messageToDisplay));
        } catch (MissingResourceException e) {
            alert.setTitle("Error");
            alert.setContentText(messageToDisplay);
        }
        alert.showAndWait();
    }

    public static boolean receiveConfirmation(String alertTitle, String alertText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertText);
        alert.setTitle(alertTitle);
        Optional<ButtonType> result = alert.showAndWait();
        return (result.isPresent()) && (result.get() == ButtonType.OK);
    }
}

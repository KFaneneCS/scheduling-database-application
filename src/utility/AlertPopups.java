package utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Appointment;

import java.util.Optional;

import static translation.Translation.translate;

public class AlertPopups {

    public static void generateErrorMessage(String messageToDisplay) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(messageToDisplay);
        alert.showAndWait();
    }

    public static void generateLoginErrorMessage(String messageToDisplay) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(translate(("Error")));
        alert.setContentText(translate(messageToDisplay));
        alert.showAndWait();
    }

    public static void generateInfoMessage(String title, String messageToDisplay) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(messageToDisplay);
        alert.showAndWait();
    }

    public static void generateCancelConfirmationMessage(int appointmentId, String type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Cancellation Confirmation");
        alert.setContentText("Appointment Cancelled. \nAppointment ID: " + appointmentId + "\nType: " + type);
        alert.showAndWait();
    }

    public static void generateDeleteConfirmationMessage(int customerId, String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Customer Deletion Confirmation");
        alert.setContentText("Customer record deleted. \nCustomer ID: " + customerId + "\nName: " + name);
        alert.showAndWait();
    }

    public static void generateUpcomingApptMessage(Appointment assocAppt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(translate("upcoming_appointments"));
        if (assocAppt == null) {
            alert.setContentText(translate("no_upcoming_appointments"));
        } else {
            alert.setContentText(translate("yes_upcoming_appointments") + "\nID: " + assocAppt.getId() + "\nDate: " +
                    assocAppt.getStart().toLocalDate() + "\n" + translate("time") + assocAppt.getStart().toLocalTime());
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

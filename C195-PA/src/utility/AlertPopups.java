package utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Appointment;

import java.util.Optional;

import static translation.Translation.translate;

/**
 * Class which handles all pop-up alerts the user may experience.
 *
 * @author Kyle Fanene
 */
public class AlertPopups {

    /**
     * Displays error message with passed String message.
     *
     * @param messageToDisplay      String message to be displayed in error message.
     */
    public static void generateErrorMessage(String messageToDisplay) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(messageToDisplay);
        alert.showAndWait();
    }

    /**
     * Displays error message with passed String message that will be translated
     * based on user locale.
     *
     * @param messageToDisplay      String message to be displayed in error message.
     */
    public static void generateLoginErrorMessage(String messageToDisplay) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(translate(("Error")));
        alert.setContentText(translate(messageToDisplay));
        alert.showAndWait();
    }

    /**
     * Displays informational alert with passed String title and message.
     *
     * @param title                 Text to be used for title of alert.
     * @param messageToDisplay      Text to be displayed in error message body.
     */
    public static void generateInfoMessage(String title, String messageToDisplay) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(messageToDisplay);
        alert.showAndWait();
    }

    /**
     * Alert which confirms that appointment was cancelled, along with appointment's information.
     *
     * @param appointmentId         Appointment ID displayed in alert.
     * @param type                  Appointment type displayed in alert.
     */
    public static void generateCancelConfirmationMessage(int appointmentId, String type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Cancellation Confirmation");
        alert.setContentText("Appointment Cancelled. \nAppointment ID: " + appointmentId + "\nType: " + type);
        alert.showAndWait();
    }

    /**
     * Alert which confirms that customer records were deleted, along with customer's information.
     *
     * @param customerId            Customer ID displayed in alert.
     * @param name                  Customer name displayed in alert.
     */
    public static void generateDeleteConfirmationMessage(int customerId, String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Customer Deletion Confirmation");
        alert.setContentText("Customer record deleted. \nCustomer ID: " + customerId + "\nName: " + name);
        alert.showAndWait();
    }

    /**
     * Informational alert to user as to any upcoming appointments (within 15 mintues) upon successful
     * login, along with said appointment's ID, date, and time.  Translated depending on user's locale.
     *
     * @param assocAppt         Associated Appointment object (null if none exist)
     *
     */
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

    /**
     * Alert that requires confirmation from user.
     *
     * @param alertTitle        Text to be used for alert title.
     * @param alertText         Text to be displayed in error message body
     *
     * @return Returns true if user confirms, false otherwise.
     */
    public static boolean receiveConfirmation(String alertTitle, String alertText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertText);
        alert.setTitle(alertTitle);
        Optional<ButtonType> result = alert.showAndWait();
        return (result.isPresent()) && (result.get() == ButtonType.OK);
    }
}

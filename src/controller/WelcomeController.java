package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import utility.AlertPopups;
import utility.SceneChanger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class which provides control logic for the components of the Welcome screen.  The user has
 * options to either view Customer records information, Appointment records information, or to exit the
 * program.  The Welcome screen is considered the "home" screen.
 *
 * @author Kyle Fanene
 */
public class WelcomeController implements Initializable {

    SceneChanger sceneChanger = new SceneChanger();
    private static final String EXIT_CONFIRMATION = "Are you sure you want to quit?";
    @FXML
    private Button appointmentsButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label selectRecordsLabel;

    @FXML
    private Label welcomeLabel;

    /**
     * Changes screen Appointments screen.
     *
     * @param event Appointments button pressed.
     */
    @FXML
    void onActionDisplayAppointmentRecords(ActionEvent event) {
        try {
            sceneChanger.changeScreen(event, "Appointment");
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(e.getMessage());
        }
    }

    /**
     * Changes screen Customers screen.
     *
     * @param event Customers button pressed.
     */
    @FXML
    void onActionDisplayCustomerRecords(ActionEvent event) {
        try {
            sceneChanger.changeScreen(event, "Customer");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Exits program after receiving confirmation from user.
     *
     * @param event Exit button pressed.
     */
    @FXML
    void onActionExitProgram(ActionEvent event) {
        if (AlertPopups.receiveConfirmation("Exit", EXIT_CONFIRMATION)) {
            System.exit(0);
        }
    }

    /**
     * Initializes the Welcome controller class.
     *
     * @param url            Per Initializable javadoc reference: "The location used to resolve relative
     *                       paths for the root object, or null if the location is not known."
     * @param resourceBundle Per Initializable javadoc reference: "The resources used to
     *                       localize the root object, or null if the root object was not localized."
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Welcome screen initialized
    }

}

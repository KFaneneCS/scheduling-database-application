package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utility.AlertPopups;
import utility.SceneChanger;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable{

    SceneChanger sceneChanger = new SceneChanger();
    String EXIT_CONFIRMATION = "Are you sure you want to quit?";
    String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    Stage stage;
    Parent scene;
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

    @FXML
    void onActionDisplayAppointmentRecords(ActionEvent event) {
        try {
            sceneChanger.changeScreen(event, "Appointment");
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(e.getMessage());
        }
    }

    @FXML
    void onActionDisplayCustomerRecords(ActionEvent event) {
        try {
            sceneChanger.changeScreen(event, "Customer");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void onActionExitProgram(ActionEvent event) {
        if (AlertPopups.receiveConfirmation("Exit",EXIT_CONFIRMATION)) {
            System.exit(0);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("~WelcomeController initialized");

    }

}

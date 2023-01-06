package controller;

import helper.AlertPopups;
import helper.ScreenChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable{

    ScreenChanger screenChanger = new ScreenChanger();
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
        System.out.println("~Appointment button clicked");
    }

    @FXML
    void onActionDisplayCustomerRecords(ActionEvent event) throws IOException {
        screenChanger.changeScreen(event, "Customer");
    }

    @FXML
    void onActionExitProgram(ActionEvent event) {
        if (AlertPopups.receiveConfirmation("Exit","Are you sure?")) {
            System.exit(0);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("~WelcomeController initialized");
    }

}

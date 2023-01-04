package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import helper.AlertPopups;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController {

    Stage stage;
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
        System.out.println("Appointment button clicked");
    }

    @FXML
    void onActionDisplayCustomerRecords(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Customer.fxml"));
            loader.load();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (IOException e) {
            AlertPopups.generateErrorMessage(e.getMessage());  // FIXME
        }
    }

    @FXML
    void onActionExitProgram(ActionEvent event) {
        if (AlertPopups.receiveConfirmation("Exit","Are you sure?")) {
            System.exit(0);
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("WelcomeController initialized");
    }

}

package controller;

import DAO.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Appointment;
import model.User;
import utility.AlertPopups;
import utility.SceneChanger;
import utility.TimeHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static translation.Translation.translate;

public class LoginController implements Initializable {

    String ERROR_MESSAGE = "Error";
    String CREDENTIALS_ERROR = "invalid_credentials";
    SceneChanger sceneChanger = new SceneChanger();
    @FXML
    private Button loginButton;
    @FXML
    private Label loginLabel;
    @FXML
    private Label userLocationLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;

    @FXML
    void onActionLogin(ActionEvent event) throws SQLException {

        String loginUsername = usernameTextField.getText();
        try {
            int userId = UserQueries.convertToUserId(loginUsername);
            User user = UserAccess.lookupUser(userId);
            if (user == null) {
                AlertPopups.generateErrorMessage(CREDENTIALS_ERROR);
            } else {
                String loginPassword = passwordField.getText();

                try {
                    if (Objects.equals(loginPassword, user.getPassword())) {

                        try {
                            TimeHelper.checkUpcomingAppointment(user);
                            Appointment assocAppt = TimeHelper.checkUpcomingAppointment(user);
                            AlertPopups.generateUpcomingApptMessage(assocAppt);
                        } catch (NullPointerException npe) {
                            AlertPopups.generateUpcomingApptMessage(null);
                        }
                        sceneChanger.changeScreen(event, "Welcome");

                    } else {
                        AlertPopups.generateErrorMessage(CREDENTIALS_ERROR);
                    }
                } catch (IOException ioe) {
                    AlertPopups.generateErrorMessage(ERROR_MESSAGE);
                    ioe.printStackTrace();
                }
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ZoneId userLocation = ZoneId.of(TimeZone.getDefault().getID());
        userLocationLabel.setText(userLocation.toString());

        loginLabel.setText(translate(loginLabel.getText()));
        usernameTextField.setPromptText(translate(usernameTextField.getPromptText()));
        passwordField.setPromptText(translate(passwordField.getPromptText()));
        loginButton.setText(translate(loginButton.getText()));

        try {
            ContactAccess.initializeContacts();
            CountryAccess.initializeCountries();
            FLDAccess.initializeFLDs();
            AppointmentAccess.initializeAppointments();
            CustomerAccess.initializeCustomers();
            UserAccess.initializeUsers();
            TimeHelper.setHoursList();

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

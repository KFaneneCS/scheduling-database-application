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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TimeZone;

import static translation.Translation.translate;

public class LoginController implements Initializable {

    String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    String CREDENTIALS_ERROR = "invalid_credentials";
    SceneChanger sceneChanger = new SceneChanger();
    String fileName = "login_activity.txt", login;
    Scanner keyboard = new Scanner(System.in);
    FileWriter fileWriter = new FileWriter(fileName, true);
    PrintWriter outputFile = new PrintWriter(fileWriter);
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

    public LoginController() throws IOException {
    }

    @FXML
    void onActionLogin(ActionEvent event) {

//        Scene scene = loginButton.getScene();
//        scene.getWindow().setOnCloseRequest((e) -> {
//            outputFile.close();
//        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss (z)");
        String now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")).format(formatter);
        String loginUsername = usernameTextField.getText();
        try {
            int userId = UserQueries.convertToUserId(loginUsername);
            User user = UserAccess.lookupUser(userId);
            if (user == null) {
                // login NOT successful
                outputFile.println("User " + loginUsername + " gave invalid log-in at " + now);
                outputFile.flush();
                AlertPopups.generateLoginErrorMessage(CREDENTIALS_ERROR);
            } else {
                String loginPassword = passwordField.getText();
                if (Objects.equals(loginPassword, user.getPassword())) {
                    // login successful
                    try {
                        outputFile.println("User " + loginUsername + " successfully logged in at " + now);
                        TimeHelper.checkUpcomingAppointment(user);
                        Appointment assocAppt = TimeHelper.checkUpcomingAppointment(user);
                        AlertPopups.generateUpcomingApptMessage(assocAppt);
                    } catch (NullPointerException npe) {
                        AlertPopups.generateUpcomingApptMessage(null);
                    }
                    sceneChanger.changeScreen(event, "Welcome");
                    outputFile.close();
                } else {
                    // login NOT successful
                    outputFile.println("User " + loginUsername + " gave invalid log-in at " + now);
                    outputFile.flush();
                    AlertPopups.generateLoginErrorMessage(CREDENTIALS_ERROR);
                }
            }

        } catch (Exception e) {
            AlertPopups.generateLoginErrorMessage(GENERAL_ERROR_MESSAGE);
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
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

}

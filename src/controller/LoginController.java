package controller;

import DAO.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.*;
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
import java.util.TimeZone;

import static translation.Translation.translate;

/**
 * Controller class which provides control logic for the components of the Login screen.  User login
 * information is validated, and all user attempts are logged (successful and unsuccessful) in a txt
 * file titled "login_activity". Username and corresponding passwords are matched against the database
 * user table.
 *
 * @author Kyle Fanene
 */
public class LoginController implements Initializable {

    private static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    private static final String CREDENTIALS_ERROR = "invalid_credentials";
    private static final SceneChanger sceneChanger = new SceneChanger();
    String fileName = "login_activity.txt";
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

    /**
     * Constructor that throws IOException to avoid unhandled exception stemming from FileWriter class.
     */
    public LoginController() throws IOException {
    }

    /**
     * Attempts login by calling executeLoginAttempt().  If successful, changes screen to Welcome screen.
     * Otherwise, a validation error is shown to user.  All login attempts are logged.
     *
     * @param event Button to add customer.  Disabled when a customer is selected for update or deletion.
     */
    @FXML
    void onActionLogin(ActionEvent event) {

        try {
            if (executeLoginAttempt()) {
                sceneChanger.changeScreen(event, "Welcome");
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Boolean method that provides main logic for login attempts. All user login attempts are logged.
     *
     * @return Returns true if login is successful, otherwise returns false.
     *
     */
    public boolean executeLoginAttempt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss (z)");
        String now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")).format(formatter);
        String loginUsername = usernameTextField.getText();
        try {
            int userId = UserAccess.lookupUserId(loginUsername);
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
                        AppointmentAccess.checkUpcomingAppointment(user);
                        Appointment assocAppt = AppointmentAccess.checkUpcomingAppointment(user);
                        AlertPopups.generateUpcomingApptMessage(assocAppt);
                    } catch (NullPointerException npe) {
                        AlertPopups.generateUpcomingApptMessage(null);
                    }
                    outputFile.close();
                    UserAccess.setActiveUser(user);
                    return true;

                } else {
                    // login NOT successful
                    outputFile.println("User " + loginUsername + " gave invalid log-in at " + now);
                    outputFile.flush();
                    AlertPopups.generateLoginErrorMessage(CREDENTIALS_ERROR);
                }
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Initializes the Login controller class.  Also initializes all data used in program, including class
     * objects.
     * <p>
     * Two lambda expressions are used, each of which checks for whether user pressed Enter while text cursor
     * is active in username or password text fields.  This allows for a better user experience by giving
     * the user the option to complete login attempt without having to click the Login button.  The code itself
     * is concise and clear and is a strategic use of lambda expressions.
     *
     * @param url            Per Initializable javadoc reference: "The location used to resolve relative
     *                       paths for the root object, or null if the location is not known."
     * @param resourceBundle Per Initializable javadoc reference: "The resources used to
     *                       localize the root object, or null if the root object was not localized."
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ZoneId userLocation = ZoneId.of(TimeZone.getDefault().getID());
        userLocationLabel.setText(userLocation.toString());

        loginLabel.setText(translate(loginLabel.getText()));
        usernameTextField.setPromptText(translate(usernameTextField.getPromptText()));
        passwordField.setPromptText(translate(passwordField.getPromptText()));
        loginButton.setText(translate(loginButton.getText()));

        // Lambda expression used to check for Enter pressed on username text field
        usernameTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    if (executeLoginAttempt()) {
                        sceneChanger.changeScreen(e, "Welcome");
                    }
                } catch (Exception ex) {
                    AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Lambda expression used to check for Enter pressed on password text field
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    if (executeLoginAttempt()) {
                        sceneChanger.changeScreen(e, "Welcome");
                    }
                } catch (Exception ex) {
                    AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

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

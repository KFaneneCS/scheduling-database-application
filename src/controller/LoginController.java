package controller;

import utility.AlertPopups;
import utility.Query;
import utility.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static translation.Translation.translate;

public class LoginController implements Initializable {

    SceneChanger screenChanger = new SceneChanger();
    @FXML
    private Button loginButton;
    @FXML
    private Label loginLabel;
    @FXML
    private Label userLocationLabel;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    void onActionLogin(ActionEvent event) {
        System.out.println("~Login button clicked");

        String username = usernameTextField.getText();
        try {
            int userId = Query.convertToUserId(username);
            if (userId < 1) {
                AlertPopups.generateErrorMessage("invalid_username");
            }
            else {
                String providedPassword = passwordTextField.getText();
                if (Objects.equals(Query.checkPassword(userId), providedPassword)) {
                    // Login successful, go to Records screen
                    try {
                        screenChanger.changeScreen(event, "Welcome");
                    } catch (IOException e) {
                        AlertPopups.generateErrorMessage(e.getMessage());  // FIXME
                    }
                }
                else {
                    AlertPopups.generateErrorMessage("invalid_password");
                }
            }

        } catch (SQLException e) {
            AlertPopups.generateErrorMessage(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("~LoginController is initialized");

        ZoneId userLocation = ZoneId.of(TimeZone.getDefault().getID());
        userLocationLabel.setText(userLocation.toString());

        loginLabel.setText(translate(loginLabel.getText()));
        usernameTextField.setPromptText(translate(usernameTextField.getPromptText()));
        passwordTextField.setPromptText(translate(passwordTextField.getPromptText()));
        loginButton.setText(translate(loginButton.getText()));
    }
}

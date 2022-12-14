package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Stage stage;
    Parent scene;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginLanguageLabel;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    void onActionLogin(ActionEvent event) {
        System.out.println("Login button clicked");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("LoginController is initialized!");
    }
}

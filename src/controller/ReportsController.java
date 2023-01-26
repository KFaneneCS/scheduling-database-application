package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import utility.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    SceneChanger sceneChanger = new SceneChanger();
    @FXML
    private TableColumn<?, ?> apptIdCol;

    @FXML
    private ComboBox<?> contactComboBox;

    @FXML
    private TableView<?> contactScheduleTableView;

    @FXML
    private ComboBox<?> countryComboBox;

    @FXML
    private TableColumn<?, ?> custIdCol;

    @FXML
    private Label customersLabel;

    @FXML
    private TableColumn<?, ?> descCol;

    @FXML
    private TableColumn<?, ?> endCol;

    @FXML
    private ComboBox<?> monthComboBox;

    @FXML
    private TableColumn<?, ?> startCol;

    @FXML
    private TableColumn<?, ?> titleCol;

    @FXML
    private TextField totalByAppointmentTextField;

    @FXML
    private TextField totalByCountryTextField;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private ComboBox<?> typeComboBox;

    @FXML
    void onActionCalcTotalByAppt(ActionEvent event) {
        System.out.println("Activated");
    }

    @FXML
    void onActionCalcTotalByCountry(ActionEvent event) {
        System.out.println("Activated");
    }

    @FXML
    void onActionEnableType(ActionEvent event) {
        System.out.println("Activated");
    }

    @FXML
    void onActionDisplayAppointment(ActionEvent event) throws IOException {
        sceneChanger.changeScreen(event, "Appointment");
    }

    @FXML
    void onActionPopulateTable(ActionEvent event) {
        System.out.println("Activated");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Reports initialized");
    }
}

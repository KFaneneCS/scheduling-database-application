package controller;

import DAO.ContactAccess;
import DAO.CountryAccess;
import DAO.ReportsQueries;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.Contact;
import model.Country;
import utility.AlertPopups;
import utility.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    SceneChanger sceneChanger = new SceneChanger();
    ObservableList<Integer> yearsList = FXCollections.observableArrayList();
    ObservableList<Integer> monthsList = FXCollections.observableArrayList();
    ObservableList<String> typesList = FXCollections.observableArrayList();
    ObservableList<String> contactsList = FXCollections.observableArrayList();
    ObservableList<String> countriesList = FXCollections.observableArrayList();
    boolean isReset = false;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> contactComboBox;

    @FXML
    private TableView<ObservableList> contactScheduleTableView;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private Label customersLabel;

    @FXML
    private ComboBox<Integer> monthComboBox;

    @FXML
    private TextField totalByAppointmentTextField;

    @FXML
    private TextField totalByCountryTextField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<Integer> yearComboBox;

    @FXML
    void onActionCalcTotalByAppt(ActionEvent event) throws SQLException {

        if (!isReset) {
            Integer year = yearComboBox.getValue();
            Integer month = monthComboBox.getValue();
            String type = typeComboBox.getValue();

            int calculatedTotal = ReportsQueries.selectTotalByTypeMonth(year, month, type);
            totalByAppointmentTextField.setText(String.valueOf(calculatedTotal));
        }
    }

    @FXML
    void onActionCalcTotalByCountry(ActionEvent event) throws SQLException {

        String country = countryComboBox.getValue();
        totalByCountryTextField.setText(String.valueOf(ReportsQueries.selectTotalByCountry(country)));
    }

//    @FXML
//    void onActionDisplayAppointment(ActionEvent event) throws IOException {
//        sceneChanger.changeScreen(event, "Appointment");
//    }

    @FXML
    void onActionEnableMonth(ActionEvent event) {

        if (!monthComboBox.isDisabled()) {
            isReset = true;
            monthComboBox.getSelectionModel().clearSelection();
            typeComboBox.getSelectionModel().clearSelection();
            typeComboBox.setDisable(true);
            totalByAppointmentTextField.clear();
            totalByAppointmentTextField.setPromptText("Total");
            isReset = false;
        } else {
            monthComboBox.setDisable(false);
        }
    }

    @FXML
    void onActionEnableType(ActionEvent event) {

        if (!isReset) {
            if (!typeComboBox.isDisabled()) {
                typeComboBox.getSelectionModel().clearSelection();
                totalByAppointmentTextField.clear();
                totalByAppointmentTextField.setPromptText("Total");
            } else {
                typeComboBox.setDisable(false);
            }
        }
    }

    @FXML
    void onActionPopulateTable(ActionEvent event) throws SQLException {

        int contactId = ContactAccess.lookupContactId(contactComboBox.getSelectionModel().getSelectedItem());
        ResultSet rs = ReportsQueries.selectContactSchedule(contactId);
        fillContactScheduleTable(rs);
    }

    public void fillContactScheduleTable(ResultSet rs) {

        // checks whether table columns were already populated
        boolean populated = contactScheduleTableView.getColumns().size() > 0;

        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        contactScheduleTableView.getItems().clear();

        try {

            if (!populated) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn column = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            try {
                                return new SimpleStringProperty(param.getValue().get(j).toString());
                            } catch (NullPointerException npe) {
                                return new SimpleStringProperty("null");
                            }
                        }
                    });
                    contactScheduleTableView.getColumns().addAll(column);
                }
            }

            contactScheduleTableView.getItems().clear();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            contactScheduleTableView.setItems(data);

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int fromYear = LocalDate.now().minusYears(5).getYear();
        int toYear = LocalDate.now().plusYears(5).getYear();

        // fill list of years
        for (int i = fromYear; i < toYear + 1; i++) {
            yearsList.add(i);
        }
        yearComboBox.setItems(yearsList);

        // fill list of months
        for (int i = 1; i < 13; i++) {
            monthsList.add(i);
        }
        monthComboBox.setItems(monthsList);

        // fill list of appointment types
        try {
            typesList = ReportsQueries.selectDistinctTypes();
            typeComboBox.setItems(typesList);
        } catch (SQLException e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }

        for (Contact contact : ContactAccess.getAllContacts()) {
            contactsList.add(contact.getName());
        }
        contactComboBox.setItems(contactsList);

        for (Country country : CountryAccess.getAllCountries()) {
            countriesList.add(country.getName());
        }
        countryComboBox.setItems(countriesList);

        backButton.setOnAction(event -> {
            try {
                sceneChanger.changeScreen(event, "Appointment");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

}

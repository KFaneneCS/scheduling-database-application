package controller;

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
import DAO.ContactAccess;
import model.Country;
import DAO.CountryAccess;
import utility.AlertPopups;
import utility.SceneChanger;
import utility.TimeHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class which provides control logic for the various components of the Reports screen.  The three
 * reports options are: 1) calculating total number customers by appointment date and type; 2) providing
 * schedule information in a table for each customer; and 3) calculating total number of customers by
 * country.
 *
 * @author Kyle Fanene
 */
public class ReportsController implements Initializable {

    private static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    private static final ObservableList<Integer> yearsList = FXCollections.observableArrayList();
    private static final ObservableList<Integer> monthsList = FXCollections.observableArrayList();
    private static final ObservableList<String> contactsList = FXCollections.observableArrayList();
    private static final ObservableList<String> countriesList = FXCollections.observableArrayList();
    SceneChanger sceneChanger = new SceneChanger();
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

    /**
     * Calculates total number of customers by appointment date and type. Boolean "isReset" checks
     * if combo boxes have previously had values picked so that the ActionEvent will be properly
     * observed.
     *
     * @param event User chooses a type from the type combo box.
     */
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

    /**
     * Calculates total number of customers by country of origin.
     *
     * @param event User chooses a country from the country combo box.
     */
    @FXML
    void onActionCalcTotalByCountry(ActionEvent event) throws SQLException {

        String country = countryComboBox.getValue();
        totalByCountryTextField.setText(String.valueOf(ReportsQueries.selectTotalByCountry(country)));
    }

    /**
     * Enables the month combo box if it is not currently disabled, otherwise it will clear month combo box,
     * type combo box, and total text field, which allows for proper activation of onActionCalcTotalByAppt's
     * event.
     *
     * @param event User chooses a year from the year combo box.
     */
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

    /**
     * Enables type combo box when month is chosen unless month value is determined as part of
     * onActionEnableMonth method.
     *
     * @param event User chooses a month from the month combo box.
     */
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

    /**
     * Fetches Contact ID of user's chosen contact and displays contact's schedule information in table.
     *
     * @param event User chooses contact from contact combo box.
     */
    @FXML
    void onActionPopulateTable(ActionEvent event) throws SQLException {

        int contactId = ContactAccess.lookupContactId(contactComboBox.getSelectionModel().getSelectedItem());
        ResultSet rs = ReportsQueries.selectContactSchedule(contactId);
        fillContactScheduleTable(rs);
    }

    /**
     * Main logic handling the population of contact schedule table.  DateTimes are displayed in user's
     * local time.
     *
     * @param rs ResultSet from SELECT query that gets relevant contact information from database.
     */
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
                    if ((Objects.equals(rs.getMetaData().getColumnName(i), "Start")) ||
                            (Objects.equals(rs.getMetaData().getColumnName(i), "End"))) {
                        row.add(TimeHelper.dbStringToLocalString(rs.getString(i)));
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                data.add(row);
            }
            contactScheduleTableView.setItems(data);

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Initializes the Reports controller class. Data is initialized for ObservableLists that are used
     * for years, months, appointment types, contacts, and countries combo boxes.
     * <p>
     * Lambda expression used on Back button that changes screen to Appointments screen.  The purpose of
     * this lambda expression is to compare it with the standard onAction methods used for Back buttons
     * in the Customer and Appointment controllers.  It demonstrates the options to the developer regarding
     * the use of JavaFX controls.
     *
     * @param url            Per Initializable javadoc reference: "The location used to resolve relative
     *                       paths for the root object, or null if the location is not known."
     * @param resourceBundle Per Initializable javadoc reference: "The resources used to
     *                       localize the root object, or null if the root object was not localized."
     */
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
            ObservableList<String> typesList = ReportsQueries.selectDistinctTypes();
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

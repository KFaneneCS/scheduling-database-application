package controller;

import DAO.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.*;
import utility.AlertPopups;
import utility.SceneChanger;
import utility.TimeHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class which provides control logic for the various components of the Customers screen, including:
 * adding, updating, selecting, and deleting customers; viewing all customers in a table that directly
 * corresponds to the applicable customers table from the database (except that date/times are displayed in
 * user's local time); and filtering table by ID or name.  Customers are added to, updated to, and deleted from
 * both the database and their corresponding Customer objects.  To maintain relational integrity, all appointments
 * associated with a given customer are deleted when that customer is deleted.
 *
 * @author Kyle Fanene
 */
public class CustomerController implements Initializable {

    private final ObservableList<String> countriesList = FXCollections
            .observableArrayList("U.S", "UK", "Canada");
    private static final SceneChanger sceneChanger = new SceneChanger();
    private static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    private static final String EMPTY_ERROR_MESSAGE = "Sorry, all fields must be filled in.";
    private static final String NO_RESULTS_ERROR_MESSAGE = "Sorry, could not find any results.";
    private static final String SELECTION_ERROR_MESSAGE = "No customer was selected.";
    private static final String CHANGE_CONFIRMATION = "Confirm changes?";
    private static final String DELETE_CONFIRMATION = "Are you sure you wish to delete customer, including ALL associated appointments?";
    private static final String ADD_SUCCESS_MESSAGE = "Customer successfully added.";
    boolean customerSelected = false;

    @FXML
    private Button addButton;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button backButton;

    @FXML
    private Button clearButton;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private Label countryLabel;

    @FXML
    private TextField customerFilterTextField;

    @FXML
    private Label customersLabel;

    @FXML
    private TableView<ObservableList> customersTableView;

    @FXML
    private Button deleteButton;

    @FXML
    private Label idLabel;

    @FXML
    private TextField idTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label phoneLabel;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Label postalCodeLabel;

    @FXML
    private Button selectButton;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private ComboBox<String> stateProvinceComboBox;

    @FXML
    private Label stateProvinceLabel;

    @FXML
    private Button updateButton;

    /**
     * Adds customer object and inserts information into database per user's input.
     * <p>
     * Logical validations are included to prevent empty fields or non-sequential time inputs.
     *
     * @param event Button to add customer.  Disabled when a customer is selected for update or deletion.
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws SQLException {

        String name = nameTextField.getText().strip();
        String address = addressTextField.getText().strip();
        String postalCode = postalCodeTextField.getText().strip();
        String phone = phoneTextField.getText().strip();
        String createdBy = UserAccess.getActiveUser();
        String lastUpdatedBy = UserAccess.getActiveUser();

        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty()) {
            AlertPopups.generateErrorMessage(EMPTY_ERROR_MESSAGE);
            return;
        }

        int divisionId = FLDAccess.lookupFLDId(stateProvinceComboBox.getValue());
        if (divisionId < 1) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            return;
        }

        CustomerAccess.executeAdd(name, address, postalCode, phone, createdBy, lastUpdatedBy, divisionId);
        fillCustomerTable();
        AlertPopups.generateInfoMessage("Add successful", ADD_SUCCESS_MESSAGE);

    }

    /**
     * Clears text fields and resets all combo boxes.
     * <p>
     * Sets customerSelected boolean to false.
     *
     * @param event Button to clear fields/combo boxes.
     */
    @FXML
    void onActionClearTextFields(ActionEvent event) {

        idTextField.setText("auto-generated");
        nameTextField.clear();
        addressTextField.clear();
        postalCodeTextField.clear();
        phoneTextField.clear();
        countryComboBox.setValue("U.S");
        stateProvinceComboBox.setValue("Alabama");

        addButton.setDisable(false);

        customerSelected = false;
    }

    /**
     * Removes selected customer object and associated appointment objects and
     * deletes information from database.
     * <p>
     * Logical validation checks that a customer was selected.
     *
     * @param event Button to delete customer.
     */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) {

        if (!customerSelected) {
            AlertPopups.generateErrorMessage(SELECTION_ERROR_MESSAGE);
        } else {

            try {

                if (AlertPopups.receiveConfirmation("Delete", DELETE_CONFIRMATION)) {

                    int customerId = Integer.parseInt(idTextField.getText());
                    CustomerAccess.executeDelete(CustomerAccess.lookupCustomer(customerId));
                    fillCustomerTable();

                }
            } catch (Exception e) {
                AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes screen to home (Welcome).
     *
     * @param event Back button, which returns to home screen.
     */
    @FXML
    void onActionDisplayWelcome(ActionEvent event) throws IOException {
        sceneChanger.changeScreen(event, "Welcome");
    }

    /**
     * Populates text fields and combo boxes with selected customer's information.
     * <p>
     * Logical validation checks that a customer was selected.
     *
     * @param event Button to select customer.
     */
    @FXML
    void onActionSelectCustomer(ActionEvent event) {

        try {

            ObservableList<Customer> customerList = customersTableView.getSelectionModel().getSelectedItem();
            int customerIdForSearch = Integer.parseInt(String.valueOf(customerList.get(0)));
            Customer customer = CustomerAccess.lookupCustomer(customerIdForSearch);

            idTextField.setText(String.valueOf(customer.getId()));
            nameTextField.setText(customer.getName());
            addressTextField.setText(customer.getAddress());
            postalCodeTextField.setText(customer.getPostalCode());
            phoneTextField.setText(customer.getPhone());
            countryComboBox.setValue(customer.getCountry());
            stateProvinceComboBox.setValue(customer.getFirstLevelDiv());

            addButton.setDisable(true);

            customerSelected = true;

        } catch (NullPointerException npe) {
            AlertPopups.generateErrorMessage(SELECTION_ERROR_MESSAGE);
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
        }
    }

    /**
     * Filters customer table by Customer ID or Name.
     *
     * @param event Filter text field when user presses Enter.
     */
    @FXML
    void onActionFilterCustomers(ActionEvent event) throws SQLException {
        String searchText = customerFilterTextField.getText();
        filterTable(searchText);
    }

    /**
     * Calls method which populates state/province combo box depending on country chosen.
     *
     * @param event User selects country from country combo box.
     */
    @FXML
    void onActionPopulateStateProvince(ActionEvent event) throws SQLException {
        fillStateProvinceComboBox();
    }

    /**
     * Updates customer object and applicable information in database for selected customer.
     * <p>
     * Logical validations check that a customer was selected and fields are not left empty.
     *
     * @param event Button to update customer.
     */
    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws SQLException {

        if (!customerSelected) {
            AlertPopups.generateErrorMessage(SELECTION_ERROR_MESSAGE);
        } else {

            int id = Integer.parseInt(idTextField.getText());
            String name = nameTextField.getText().strip();
            String address = addressTextField.getText().strip();
            String postalCode = postalCodeTextField.getText().strip();
            String phone = phoneTextField.getText().strip();
            String lastUpdatedBy = UserAccess.getActiveUser();

            if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty()) {
                AlertPopups.generateErrorMessage(EMPTY_ERROR_MESSAGE);
                return;
            }

            int divisionId = FLDAccess.lookupFLDId(stateProvinceComboBox.getValue());
            if (divisionId < 1) {
                AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
                return;
            }

            if (AlertPopups.receiveConfirmation("Confirm Update", CHANGE_CONFIRMATION)) {
                if (CustomerQueries.updateCustomer(id, name, address, postalCode, phone, lastUpdatedBy, divisionId) == 1) {
                    fillCustomerTable();
                    ResultSet rs = CustomerQueries.selectCustomer(id);
                    while (rs.next()) {
                        Customer updatedCustomer = CustomerAccess.getCustomerObjFromDB(rs);
                        CustomerAccess.updateCustomer(id, updatedCustomer);
                    }

                } else {
                    AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Method that sets up customer TableView columns corresponding to customer table in database.
     * "populated" boolean ensure columns are not populated multiple times.  Calls refreshTable()
     * method to fill rows.
     */
    public void fillCustomerTable() {

        // checks whether table columns were already populated
        boolean populated = customersTableView.getColumns().size() > 0;

        customersTableView.getItems().clear();

        try {

            ResultSet rs = CustomerQueries.selectAllCustomers();

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
                    customersTableView.getColumns().addAll(column);
                }
            }

            refreshTable(rs);

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Overloaded method that takes rows of data from database per passed ResultSet and populates the
     * customer table accordingly.  All DateTimes are converted from UTC to user's local time
     * zone.
     *
     * @param rs    The ResultSet from a database select query that pulls the relevant
     *              database table information.
     */
    public void refreshTable(ResultSet rs) throws SQLException {

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        customersTableView.getItems().clear();

        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                if ((Objects.equals(rs.getMetaData().getColumnName(i), "Start")) ||
                        (Objects.equals(rs.getMetaData().getColumnName(i), "End")) ||
                        (Objects.equals(rs.getMetaData().getColumnName(i), "Create_Date")) ||
                        (Objects.equals(rs.getMetaData().getColumnName(i), "Last_Update"))) {
                    row.add(TimeHelper.dbStringToLocalString(rs.getString(i)));
                } else {
                    row.add(rs.getString(i));
                }
            }
            data.add(row);
        }
        customersTableView.setItems(data);
    }

    /**
     * Overloaded method that takes ObservableList of ResultSets stemming from multiple
     * SELECT-FROM-WHERE queries and filters the customer accordingly.  All
     * DateTimes are converted from UTC to user's local time zone.
     *
     * @param rsList    The ResultSet ObservableList from database select queries that pull the relevant
     *                  database table information to filter the customer table.
     */
    public void refreshTable(ObservableList<ResultSet> rsList) throws SQLException {

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        customersTableView.getItems().clear();

        for (ResultSet rs : rsList) {
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if ((Objects.equals(rs.getMetaData().getColumnName(i), "Start")) ||
                            (Objects.equals(rs.getMetaData().getColumnName(i), "End")) ||
                            (Objects.equals(rs.getMetaData().getColumnName(i), "Create_Date")) ||
                            (Objects.equals(rs.getMetaData().getColumnName(i), "Last_Update"))) {
                        row.add(TimeHelper.dbStringToLocalString(rs.getString(i)));
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                data.add(row);
            }
        }
        customersTableView.setItems(data);
    }

    /**
     * Fills state/province combo box corresponding to the country chosen by user.  Ensures
     * referential integrity between first-level divisions and countries.
     */
    public void fillStateProvinceComboBox() throws SQLException {
        String country = countryComboBox.getValue();
        int countryId = CountryAccess.lookupCountryId(country);
        ObservableList<String> fldList = FLDAccess.getFilteredFLDsAsStrings(countryId);
        stateProvinceComboBox.setItems(fldList);
        stateProvinceComboBox.setValue(fldList.get(0));
    }

    /**
     * Filters customer table by String provided in the "Search by" text field. Method
     * will first check if user provided an integer, in which case it will search by
     * customer ID.  Otherwise, it will search by customer name. If no results are found,
     * an error alert will pop up.
     *
     * @param text  The text input from user.
     */
    public void filterTable(String text) throws SQLException {

        ObservableList<ResultSet> rsList = FXCollections.observableArrayList();
        try {
            if (text.isBlank()) {
                fillCustomerTable();
                return;
            }
            int id = Integer.parseInt(text.strip());
            Customer customer = CustomerAccess.lookupCustomer(id);
            if (customer == null) {
                throw new NullPointerException();
            } else {
                ResultSet rs = CustomerQueries.selectCustomer(customer.getId());
                refreshTable(rs);
            }
        } catch (NumberFormatException numberFormatException) {
            ObservableList<Customer> customersList = CustomerAccess.lookupCustomer(text.strip());
            if (customersList.size() < 1) {
                fillCustomerTable();
                AlertPopups.generateErrorMessage(NO_RESULTS_ERROR_MESSAGE);
            } else {
                for (Customer customer : customersList) {
                    ResultSet rs = CustomerQueries.selectCustomer(customer.getName(), customer.getAddress());
                    rsList.add(rs);
                }
                refreshTable(rsList);
            }
        } catch (NullPointerException nullPointerException) {
            AlertPopups.generateErrorMessage(NO_RESULTS_ERROR_MESSAGE);
        }

    }

    /**
     * Initializes the Customer controller class. Populates default data for combo boxes and tables.
     *
     * @param url            Per Initializable javadoc reference: "The location used to resolve relative
     *                       paths for the root object, or null if the location is not known."
     * @param resourceBundle Per Initializable javadoc reference: "The resources used to
     *                       localize the root object, or null if the root object was not localized."
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillCustomerTable();
        countryComboBox.setValue("U.S");
        countryComboBox.setItems(countriesList);

        try {
            fillStateProvinceComboBox();
        } catch (SQLException e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}


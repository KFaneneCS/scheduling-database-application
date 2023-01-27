package controller;

import DAO.CustomerQueries;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.Customer;
import utility.AlertPopups;
import utility.SceneChanger;
import utility.TimeHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

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

    @FXML
    void onActionDeleteCustomer(ActionEvent event) {

        System.out.println("TEST:  Total number of customers BEFORE delete = " + CustomerAccess.getAllCustomers().size());
        if (!customerSelected) {
            AlertPopups.generateErrorMessage(SELECTION_ERROR_MESSAGE);
        } else {

            try {

                if (AlertPopups.receiveConfirmation("Delete", DELETE_CONFIRMATION)) {

                    int customerId = Integer.parseInt(idTextField.getText());
                    CustomerAccess.executeDelete(CustomerAccess.lookupCustomer(customerId));
                    fillCustomerTable();
                    System.out.println("TEST:  Total number of customers AFTER delete = " + CustomerAccess.getAllCustomers().size());

                }
            } catch (Exception e) {
                AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onActionDisplayWelcome(ActionEvent event) throws IOException {
        sceneChanger.changeScreen(event, "Welcome");
    }

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

    @FXML
    void onActionFilterCustomers(ActionEvent event) throws SQLException {
        String searchText = customerFilterTextField.getText();
        filterTable(searchText);
    }

    @FXML
    void onActionPopulateStateProvince(ActionEvent event) throws SQLException {
        fillStateProvinceComboBox();
    }

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

    public void fillStateProvinceComboBox() throws SQLException {
        String country = countryComboBox.getValue();
        int countryId = CountryAccess.lookupCountryId(country);
        ObservableList<String> fldList = FLDAccess.getFilteredFLDsAsStrings(countryId);
        stateProvinceComboBox.setItems(fldList);
        stateProvinceComboBox.setValue(fldList.get(0));
    }

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


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

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    private final ObservableList<String> countriesList = FXCollections
            .observableArrayList("U.S", "UK", "Canada");
    SceneChanger screenChanger = new SceneChanger();
    String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    String EMPTY_ERROR_MESSAGE = "Sorry, all fields must be filled in.";
    String NO_RESULTS_ERROR_MESSAGE = "Sorry, could not find any results.";
    String SELECTION_ERROR_MESSAGE = "No customer was selected.";
    String CHANGE_CONFIRMATION = "Confirm changes?";
    String DELETE_CONFIRMATION = "Are you sure you wish to delete customer, including ALL associated appointments?";
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

        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty()) {
            AlertPopups.generateErrorMessage(EMPTY_ERROR_MESSAGE);
            return;
        }

        int divisionId = FLDAccess.lookupFLDId(stateProvinceComboBox.getValue());
        if (divisionId < 1) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            return;
        }

        CustomerAccess.executeAdd(name, address, postalCode, phone, divisionId);
        fillCustomerTable();

//        CustomerAccess.addCustomer(rs);

//        System.out.println("All customers count == " + CustomerAccess.allCustomers.size() +
//                " which should be same as # of customers in database ==  " + customersTableView.getItems().size());

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

        // TODO:  Haven't tested this yet!!!
        if (!customerSelected) {
            AlertPopups.generateErrorMessage(SELECTION_ERROR_MESSAGE);
        } else {

            try {

                if (AlertPopups.receiveConfirmation("Delete", DELETE_CONFIRMATION)) {

                    int customerId = Integer.parseInt(idTextField.getText());
                    CustomerAccess.executeDelete(CustomerAccess.lookupCustomers(customerId));
                    fillCustomerTable();


                }
            } catch (Exception e) {
                AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onActionDisplayWelcome(ActionEvent event) throws IOException {
        screenChanger.changeScreen(event, "Welcome");
    }

    @FXML
    void onActionSelectCustomer(ActionEvent event) {

        try {

            ObservableList<Customer> customerList = customersTableView.getSelectionModel().getSelectedItem();
            int customerIdForSearch = Integer.parseInt(String.valueOf(customerList.get(0)));
            Customer customer = CustomerAccess.lookupCustomers(customerIdForSearch);

            idTextField.setText(String.valueOf(customer.getId()));
            nameTextField.setText(customer.getName());
            addressTextField.setText(customer.getAddress());
            postalCodeTextField.setText(customer.getPostalCode());
            phoneTextField.setText(customer.getPhone());
            countryComboBox.setValue(customer.getCountry());
            stateProvinceComboBox.setValue(customer.getFirstLevelDiv());

            System.out.println("TEST:  Customer start date using conversion === " + customer.getCreateDate());

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
                if (CustomerQueries.updateCustomer(id, name, address, postalCode, phone, divisionId) == 1) {
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
                row.add(rs.getString(i));
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
                    row.add(rs.getString(i));
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
            Customer customer = CustomerAccess.lookupCustomers(id);
            if (customer == null) {
                throw new NullPointerException();
            } else {
                ResultSet rs = CustomerQueries.selectCustomer(customer.getId());
                refreshTable(rs);
            }
        } catch (NumberFormatException numberFormatException) {
            ObservableList<Customer> customersList = CustomerAccess.lookupCustomers(text.strip());
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
        System.out.println("~CustomersController initialized");
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


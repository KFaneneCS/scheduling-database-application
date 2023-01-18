package controller;

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
import model.CustomerAccess;
import utility.AlertPopups;
import utility.Query;
import utility.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    String EMPTY_ERROR_MESSAGE = "Sorry, all fields must be filled in.";
    String NO_RESULTS_ERROR_MESSAGE = "Sorry, could not find any results.";
    private ObservableList<ObservableList> data = FXCollections.observableArrayList();
    private final ObservableList<String> countriesList = FXCollections
            .observableArrayList("U.S", "UK", "Canada");
    SceneChanger screenChanger = new SceneChanger();

    @FXML
    private Button addButton;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button backButton;

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
    private TextField postalCodeTextField;

    @FXML
    private ComboBox<String> stateProvinceComboBox;

    @FXML
    private Label stateProvinceLabel;

    @FXML
    private Button updateButton;


    @FXML
    void onActionAddCustomer(ActionEvent event) throws SQLException {

        // TODO:  Current task
        String name = nameTextField.getText().strip();
        String address = addressTextField.getText().strip();
        String postalCode = postalCodeTextField.getText().strip();
        String phone = phoneTextField.getText().strip();

        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty()) {
            AlertPopups.generateErrorMessage(EMPTY_ERROR_MESSAGE);
            return;
        }

        int divisionId = Query.getDivisionId(stateProvinceComboBox.getValue());
        if (divisionId < 1) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            return;
        }

        Query.doEverything(name, address, postalCode, phone, divisionId);
        fillCustomerTable();

//        CustomerAccess.addCustomer(rs);

//        System.out.println("All customers count == " + CustomerAccess.allCustomers.size() +
//                " which should be same as # of customers in database ==  " + customersTableView.getItems().size());

    }

    @FXML
    void onActionDeleteCustomer(ActionEvent event) {
        System.out.println("~Delete customer button pressed");
    }

    @FXML
    void onActionDisplayWelcome(ActionEvent event) throws IOException {
        screenChanger.changeScreen(event, "Welcome");
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
    void onActionUpdateCustomer(ActionEvent event) {
        System.out.println("~Update customer button pressed");
    }

    public void fillCustomerTable() {

        // checks whether table columns were already populated
        boolean populated = customersTableView.getColumns().size() > 0;

        data.removeAll();
        customersTableView.getItems().clear();

        try {

            ResultSet rs = Query.selectAllCustomers();

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

            updateTable(rs);

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
        System.out.println("# of rows after = " + customersTableView.getItems().size());
        System.out.println("DATA size = " + data.size());
    }


    public void updateTable(ResultSet rs) throws SQLException {

        data.removeAll();
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

    public void updateTable(ObservableList<ResultSet> rsList) throws SQLException {

        data.removeAll();
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
        int countryId = Query.convertToCountryId(country);
        ObservableList<String> fldList = Query.getFirstLevelDiv(countryId);
        stateProvinceComboBox.setItems(fldList);
        stateProvinceComboBox.setValue(fldList.get(0));
    }

    public void filterTable(String text) throws SQLException {
        ObservableList<ResultSet> rsList = FXCollections.observableArrayList();
        try {
            int id = Integer.parseInt(text);
            Customer customer = CustomerAccess.lookupCustomer(id);
            if (customer == null) {
                throw new NullPointerException();
            } else {
                ResultSet rs = Query.selectCustomer(customer.getId());
                updateTable(rs);
            }
        } catch (NumberFormatException numberFormatException) {
            ObservableList<Customer> customersList = CustomerAccess.lookupCustomer(text);
            if (customersList.size() < 1) {
                fillCustomerTable();
                AlertPopups.generateErrorMessage(NO_RESULTS_ERROR_MESSAGE);
            } else {
                for (Customer customer : customersList) {
                    ResultSet rs = Query.selectCustomer(customer.getName());
                    rsList.add(rs);
                }
                updateTable(rsList);
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
            CustomerAccess.initializeCustomers();
        } catch (SQLException e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}


package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.AlertPopups;
import utility.Query;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";
    public static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    public static final ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();


    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public static ObservableList<Customer> getFilteredCustomers() {
        return filteredCustomers;
    }
    //Methods
    public static void initializeCustomers() throws SQLException {

        try {
            ResultSet rs = Query.selectAllCustomers();

            while (rs.next()) {
                addCustomer(rs);
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public static void addCustomer(ResultSet rs) throws SQLException {

        int id = rs.getInt("Customer_ID");
        String name = rs.getString("Customer_Name");
        String address = rs.getString("Address");
        String postalCode = rs.getString("Postal_Code");
        String phone = rs.getString("Phone");
        String createDate = rs.getString("Create_Date");
        String createdBy = rs.getString("Created_By");
        String lastUpdate = rs.getString("Last_Update");
        String lastUpdatedBy = rs.getString("Last_Updated_By");
        int divisionId = rs.getInt("Division_ID");
        Customer customer = new Customer(id, name, address, postalCode, phone, createDate,
                createdBy, lastUpdate, lastUpdatedBy, divisionId);
        allCustomers.add(customer);
    }

    public static Customer lookupCustomer(int customerId) {

        for (Customer customer : getAllCustomers()) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    public static ObservableList<Customer> lookupCustomer(String customerName) {

        if (!getFilteredCustomers().isEmpty()) {
            getFilteredCustomers().clear();
        }
        for (Customer customer : getAllCustomers()) {
            if (customer.getName().contains(customerName)) {
                getFilteredCustomers().add(customer);
            }
        }
        return getFilteredCustomers();
    }

}

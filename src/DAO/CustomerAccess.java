package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static final ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public static ObservableList<Customer> getFilteredCustomers() {
        return filteredCustomers;
    }

    public static boolean executeDelete(Customer customer) throws SQLException {

        try {

            for (Appointment appointment : getAssociatedAppointments(customer.getId())) {

                if (appointment.getCustomerId() == customer.getId()) {
                    AppointmentAccess.executeDelete(appointment.getId());
                }
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }

        if (getAssociatedAppointments(customer.getId()).size() != 0) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            return false;
        }

        if (CustomerQueries.deleteCustomer(customer.getId()) == 1) {
            return true;
        }

        AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        return false;
    }


    //Methods
    public static void initializeCustomers() {

        try {
            ResultSet rs = CustomerQueries.selectAllCustomers();

            while (rs.next()) {
                Customer customer = getCustomerObjFromDB(rs);
                addCustomer(customer);
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void addCustomer(Customer customer) {
        allCustomers.add(customer);
    }


    public static Customer getCustomerObjFromDB(ResultSet rs) throws SQLException {

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
        return new Customer(id, name, address, postalCode, phone, createDate,
                createdBy, lastUpdate, lastUpdatedBy, divisionId);
    }

    public static void updateCustomer(int id, Customer updatedCustomer) {

        int index = lookupIndex(id);
        allCustomers.set(index, updatedCustomer);
    }

    public static Customer lookupCustomer(int customerId) {

        for (Customer customer : getAllCustomers()) {

            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    public static int lookupCustomerId(String customerName) {

        for (Customer customer : getAllCustomers()) {

            if (customer.getName() == customerName) {
                return customer.getId();
            }
        }
        return -1;
    }

    public static int lookupIndex(int customerId) {

        for (Customer customer : getAllCustomers()) {

            if (customer.getId() == customerId) {
                return getAllCustomers().indexOf(customer);
            }
        }
        AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        return -1;
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

    public static ObservableList<Appointment> getAssociatedAppointments(int customerId) {

        ObservableList<Appointment> associatedAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : AppointmentAccess.getAllAppointments()) {
            if (customerId == appointment.getId()) {
                associatedAppointments.add(appointment);
            }
        }
        return associatedAppointments;
    }

    public static void executeAdd(String name, String address, String postalCode,
                                  String phone, int divId) throws SQLException {

        if (CustomerQueries.insertCustomer(name, address, postalCode, phone, divId) < 1) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        } else {
            ResultSet rs = CustomerQueries.selectCustomer(name, address);

            while (rs.next()) {
                Customer customer = getCustomerObjFromDB(rs);
                addCustomer(customer);
            }
        }
    }

}

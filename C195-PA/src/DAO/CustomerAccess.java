package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class which provides various means of access to Customer class, such as retrieving customer
 * object lists, adding, deleting, and updating Customer objects, looking up customers, and
 * converting data from database into Customer object.
 *
 * @author Kyle Fanene
 */
public class CustomerAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Observable list of all Customer objects.
     */
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * Observable list of filtered Customer objects.
     */
    private static final ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();

    /**
     * Method that returns allCustomers list.
     *
     * @return Returns allCustomers ObservableList.
     */
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    /**
     * Method that returns filteredCustomers list.
     *
     * @return Returns filteredCustomers ObservableList.
     */
    public static ObservableList<Customer> getFilteredCustomers() {
        return filteredCustomers;
    }

    /**
     * Method that adds Customer object to allCustomers list
     *
     * @param customer       Customer object to be added.
     */
    public static void addCustomer(Customer customer) {
        allCustomers.add(customer);
    }

    /**
     * Method that removes Customer object to allCustomers list
     *
     * @return Returns true if Customer object was successfully removed, otherwise false.
     */
    public static boolean removeCustomer(Customer customer) {
        return allCustomers.remove(customer);
    }

    /**
     * Method that contains main logic behind deleting Customer objects and data from database.
     * All appointments associated with Customer to delete are also removed, to which user is
     * alerted.
     *
     * @param customer     Customer object to determine customer data to be deleted.
     *
     * @return Returns true if delete is successful, otherwise false.
     */
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
            AlertPopups.generateDeleteConfirmationMessage(customer.getId(), customer.getName());
            removeCustomer(customer);
            return true;
        }

        AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        return false;
    }

    /**
     * Method that initializes Customer objects via customers table in database.
     */
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

    /**
     * Method that takes customer information from database and returns a new Customer object
     * based on said data.
     *
     * @param rs        ResultSet used to query database for customer data to use.
     *
     * @return Returns newly created Customer object.
     */
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

    /**
     * Method that updates Customer object in allCustomers list.
     *
     * @param id                    Customer ID to determine Customer object's index position in list.
     *
     * @param updatedCustomer       Updated Customer object.
     */
    public static void updateCustomer(int id, Customer updatedCustomer) {

        int index = lookupIndex(id);
        allCustomers.set(index, updatedCustomer);
    }

    /**
     * Method that searches allCustomers list using customer ID and returns
     * Customer object.
     *
     * @param customerId       Customer ID to search list against.
     *
     * @return Returns Customer object; returns null if not found.
     */
    public static Customer lookupCustomer(int customerId) {

        for (Customer customer : getAllCustomers()) {

            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Method that searches allCustomers list using customer name String and returns
     * Customer ID.
     *
     * @param customerName       String customer name to search list against.
     *
     * @return Returns Customer ID integer; returns -1 if not found.
     */
    public static int lookupCustomerId(String customerName) {

        for (Customer customer : getAllCustomers()) {

            if (customer.getName().equals(customerName)) {
                return customer.getId();
            }
        }
        return -1;
    }

    /**
     * Method that searches for allCustomers list index given an customer ID and returns index.
     *
     * @param customerId       Customer ID to search list against.
     *
     * @return Returns index of Customer object in list; returns -1 if not found.
     */
    public static int lookupIndex(int customerId) {

        for (Customer customer : getAllCustomers()) {

            if (customer.getId() == customerId) {
                return getAllCustomers().indexOf(customer);
            }
        }
        AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        return -1;
    }

    /**
     * Method that searches allCustomers list via user text input and returns list
     * of Customer objects whose name contains user text.
     *
     * @param userText       User text to search list against.
     *
     * @return Returns list of Customer objects whose name contains user's text input.
     */
    public static ObservableList<Customer> lookupCustomer(String userText) {

        if (!getFilteredCustomers().isEmpty()) {
            getFilteredCustomers().clear();
        }
        for (Customer customer : getAllCustomers()) {
            if (customer.getName().contains(userText)) {
                getFilteredCustomers().add(customer);
            }
        }
        return getFilteredCustomers();
    }

    /**
     * Method that searches all appointments list and compares appointment's customer IDs to
     * passed customer ID, which determines all appointments associated with given customer.
     *
     * @param customerId       Customer ID with which to compare Appointment's associated Customer ID.
     *
     * @return Returns list of Appointment objects that are associated with given customer.
     */
    public static ObservableList<Appointment> getAssociatedAppointments(int customerId) {

        ObservableList<Appointment> associatedAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : AppointmentAccess.getAllAppointments()) {
            if (customerId == appointment.getCustomerId()) {
                associatedAppointments.add(appointment);
            }
        }
        return associatedAppointments;
    }

    /**
     * Method that contains main logic behind adding Customer objects and data to database.
     *
     * @param name          Name of newly created Customer and database insert.
     * @param address       Address of newly created Customer and database insert.
     * @param postalCode    Postal code of newly created Customer and database insert.
     * @param phone         Phone number as a String of newly created Customer and database insert.
     * @param createdBy     User who created this customer object.
     * @param lastUpdatedBy User who created this customer object.
     * @param divId         First-level division ID of newly created Customer and database insert.
     *
     */
    public static void executeAdd(String name, String address, String postalCode, String phone, String createdBy,
                                  String lastUpdatedBy, int divId) throws SQLException {

        if (CustomerQueries.insertCustomer(name, address, postalCode, phone, createdBy, lastUpdatedBy, divId) < 1) {
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

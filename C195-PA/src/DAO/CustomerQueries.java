package DAO;

import utility.AlertPopups;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class that holds all database query methods pertaining to the Customer class.
 *
 * @author Kyle Fanene
 */
public class CustomerQueries {

    public static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Method that takes customer information and inserts into customer table in database.
     *
     * @param name          Customer name String used in INSERT INTO query.
     * @param address       Customer address String used in INSERT INTO query.
     * @param postalCode    Customer postal code String used in INSERT INTO query.
     * @param phone         Customer phone number as a String used in INSERT INTO query.
     * @param createdBy     User who is inserting customer, as a String, used in INSERT INTO query.
     * @param lastUpdatedBy User who is inserting customer, as a String, used in INSERT INTO query.
     * @param divId         Customer's corresponding first-level division ID used to determine both
     *                      country and state/province names.
     * @return Returns 1 if query is successful, otherwise return 0.
     */
    public static int insertCustomer(String name, String address, String postalCode, String phone,
                                     String createdBy, String lastUpdatedBy, int divId) {
        try {
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, " +
                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES (?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setString(5, createdBy);
            ps.setString(6, lastUpdatedBy);
            ps.setInt(7, divId);
            return ps.executeUpdate();
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Method that updates a given customer from database's customer table.
     *
     * @param cusId         Customer ID used to identify customer data to be queried.
     * @param name          Customer name String used in UPDATE query.
     * @param address       Custer address String used in UPDATE query.
     * @param postalCode    Customer postal code String used in UPDATE query.
     * @param phone         Customer phone number as a String used in UPDATE query.
     * @param lastUpdatedBy User who is updating customer, as a String, used in UPDATE query.
     * @param divId         Customer's corresponding first-level division ID, as an int, used in UPDATE query.
     *
     * @return              Returns 1 if query is successful, otherwise returns 0.
     */
    public static int updateCustomer(int cusId, String name, String address, String postalCode,
                                     String phone, String lastUpdatedBy, int divId) throws SQLException {

        String sql = "UPDATE customers SET " +
                "Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                "Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, lastUpdatedBy);
        ps.setInt(6, divId);
        ps.setInt(7, cusId);
        return ps.executeUpdate();

    }

    /**
     * Method that queries the database to select all from the customers table.
     *
     * @return Returns the ResultSet from selecting all customers query.
     */
    public static ResultSet selectAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

    /**
     * Overloaded method that queries the database to select a particular customer by name and address from
     * database's customers table.
     *
     * @param name      Customer name String used in SELECT query.
     * @param address   Customer address String used in SELECT query.
     *
     * @return Returns the ResultSet from selecting given customer query.
     */
    public static ResultSet selectCustomer(String name, String address) throws SQLException {

        String sql = "SELECT * FROM customers WHERE Customer_Name = ? AND Address = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        return ps.executeQuery();
    }

    /**
     * Overloaded method that queries the database to select a particular customer by customer ID from
     * database's customers table.
     *
     * @param id        Customer ID int used in SELECT query.
     *
     * @return Returns the ResultSet from selecting given customer query.
     */
    public static ResultSet selectCustomer(int id) throws SQLException {

        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    /**
     * Method that queries the database to delete a customer with a given ID from the database.
     *
     * @param id        Customer ID int used in DELETE FROM query.
     *
     * @return Returns the ResultSet from executing delete query for particular customer.
     */
    public static int deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

}

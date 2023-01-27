package DAO;

import utility.AlertPopups;
import utility.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerQueries {

    public static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";

    public static int insertCustomer(String n, String a, String pc,
                                     String ph, String cB, String luB, int d) {
        try {
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, " +
                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES (?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, n);
            ps.setString(2, a);
            ps.setString(3, pc);
            ps.setString(4, ph);
            ps.setString(5, cB);
            ps.setString(6, luB);
            ps.setInt(7, d);
            return ps.executeUpdate();
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
            return -1;
        }
    }

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

    public static ResultSet selectAllCustomers() throws SQLException {
        String sql = "SELECT * FROM client_schedule.customers";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

    public static ResultSet selectCustomer(String name, String address) throws SQLException {

        String sql = "SELECT * FROM client_schedule.customers WHERE Customer_Name = ? AND Address = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        return ps.executeQuery();
    }

    public static ResultSet selectCustomer(int id) throws SQLException {

        String sql = "SELECT * FROM client_schedule.customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    public static int deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM client_schedule.customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

}

package utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Query {

    public static int convertToUserId(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getInt("User_ID");
        }
        return 0;
    }

    public static int convertToCountryId(String country) throws SQLException {
        String sql = "SELECT * FROM countries WHERE Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getInt("Country_ID");
        }
        return 0;   // TODO:  Account for 0 in calls
    }

    // FIXME
    public static String checkPassword(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getString("Password");
        }
        return null;
    }

    // Updaters
    // TODO:  Current task
    public static int insertCustomer(String name, String address, String postalCode,
                                     String phone, int divId) throws SQLException {
        try {
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, " +
                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES (?, ?, ?, ?, NOW(), 'script', NOW(), 'script', ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divId);
            return ps.executeUpdate();
        } catch (Exception e) {
            AlertPopups.generateErrorMessage("Sorry, there was an error");
            e.printStackTrace();
            return -1;
        }
    }

    public static int update(int customerId, String customerName) throws SQLException {
        String sql = "UPDATE test_table SET customer = ? WHERE id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setInt(2, customerId);
        return ps.executeUpdate();
    }

    public static int delete(int customerId) throws SQLException {
        String sql = "DELETE FROM test_table WHERE id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        return ps.executeUpdate();
    }

    // Queries
    // FIXME:  Need to finish this one including ZDT return type
    public static void selectAppointmentDate() throws SQLException {
        String sql = "SELECT * FROM test_table";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int customerId = rs.getInt("id");
            String customerName = rs.getString("customer");
            System.out.print(customerId + " | ");
            System.out.print(customerName + "\n");
        }
    }

    public static ObservableList<String> getFirstLevelDiv(int countryId) throws SQLException {

        ObservableList<String> divisionList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String division = rs.getString("Division");
            divisionList.add(division);
        }
        return divisionList;
    }

    public static int getDivisionId(String division) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("Division_ID");
        }
        return 0;
    }

    // FIXME
    public static ZonedDateTime selectAppointmentDate(int appointmentId) throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        ResultSet rs = ps.executeQuery();
        String dateTimeFromDB = null;
        while (rs.next()) {     // TODO:  Need to address possibility of no entries found
            dateTimeFromDB = rs.getString("Start");
        }
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeFromDB, DTF);
        ZoneId zoneId = ZoneId.of("UTC");
        return ZonedDateTime.of(localDateTime, zoneId);
    }

    public static ResultSet selectAllCustomers() throws SQLException {
        String sql = "SELECT * FROM client_schedule.customers";
        return JDBC.connection.createStatement().executeQuery(sql);
    }



}
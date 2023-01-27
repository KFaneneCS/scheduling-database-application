package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsQueries {

    public static ObservableList<String> selectDistinctTypes() throws SQLException {

        ObservableList<String> typesList = FXCollections.observableArrayList();

        String sql = "SELECT DISTINCT Type FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            typesList.add(rs.getString("Type"));
        }
        return typesList;
    }

    public static int selectTotalByTypeMonth(int year, int month, String type) throws SQLException {

        String sql = "SELECT TotalValue FROM " +
                "(SELECT Type, COUNT(Appointment_ID) AS TotalValue FROM appointments " +
                "WHERE YEAR(Start)=? AND MONTH(Start)=? GROUP BY Type) AS value_table " +
                "WHERE Type = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, year);
        ps.setInt(2, month);
        ps.setString(3, type);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("TotalValue");
        }
        return 0;
    }

    public static int selectTotalByCountry(String country) throws SQLException {

        String sql = "SELECT COUNT(Customer_ID) AS TotalValue " +
                "FROM customers " +
                "INNER JOIN first_level_divisions as fld " +
                "INNER JOIN countries " +
                "ON customers.Division_ID = fld.Division_ID AND fld.Country_ID = countries.Country_ID " +
                "WHERE Country = ? " +
                "GROUP BY Country";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("TotalValue");
        }
        return 0;
    }

    public static ResultSet selectContactSchedule(int contactId) throws SQLException {

        String sql = "SELECT Appointment_ID, Title, Type, Description, Start, End, Customer_ID " +
                "FROM contacts " +
                "INNER JOIN appointments " +
                "ON contacts.Contact_ID = appointments.Contact_ID " +
                "WHERE contacts.Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactId);
        return ps.executeQuery();
    }

}

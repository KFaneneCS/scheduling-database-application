package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class that holds all database query methods pertaining to the Reports page.
 *
 * @author Kyle Fanene
 */
public class ReportsQueries {

    /**
     * Method that queries the database to select distinct types from appointments table.
     *
     * @return Returns an ObservableList of distinct appointment types as Strings.
     */
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

    /**
     * Method that queries the database to select total number of customers by appointment year, month,
     * and type.
     *
     * @param year      Appointment year int used in SELECT query.
     * @param month     Appointment month int used in SELECT query.
     * @param type      Appointment type String used in SELECT query.
     *
     * @return Returns integer number of customers by date and type.
     */
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

    /**
     * Method that queries the database to select total number of customers by country. INNER JOINS
     * are used on first_level_divisions and countries tables with customers table.
     *
     * @param country       Country name String used in SELECT query.
     *
     * @return Returns integer number of customers by country.
     */
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

    /**
     * Method that queries the database to select appointment ID, title, type, description, state date and
     * time, end date and time, and customer ID for selected contact.
     *
     * @param contactId     Contact ID int used in SELECT query.
     *
     * @return Returns the ResultSet from selecting various appointment information for given customer.
     */
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

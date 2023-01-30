package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class that holds all database query methods pertaining to the FirstLevelDivision class.
 *
 * @author Kyle Fanene
 */
public class FLDQueries {

    /**
     * Method that queries the database to select first-level divisions based on country ID.
     *
     * @param countryId     Country ID int used in SELECT query.
     *
     * @return Returns an ObservableList of first-level divisions as Strings based on country ID.
     */
    public static ObservableList<String> getDivisionList(int countryId) throws SQLException {

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

    /**
     * Method that queries the database to select all from the first_level_divisions table.
     *
     * @return Returns the ResultSet from selecting all first-level divisions query.
     */
    public static ResultSet selectAllFLDs() throws SQLException {
        String sql = "SELECT * FROM first_level_divisions";
        return JDBC.connection.createStatement().executeQuery(sql);
    }
}

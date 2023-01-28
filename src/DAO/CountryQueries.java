package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class that holds single database query method pertaining to the Country class.
 *
 * @author Kyle Fanene
 */
public class CountryQueries {

    /**
     * Method that queries the database to select all from the countries table.
     *
     * @return Returns the ResultSet from selecting all countries query.
     */
    public static ResultSet selectAllCountries() throws SQLException {
        String sql = "SELECT * FROM client_schedule.countries";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

}

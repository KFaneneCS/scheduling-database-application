package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class that holds single database query method pertaining to the User class.
 *
 * @author Kyle Fanene
 */
public class UserQueries {

    /**
     * Method that queries the database to select all from the users table.
     *
     * @return Returns the ResultSet from selecting all customers query.
     */
    public static ResultSet selectAllUsers() throws SQLException {
        String sql = "SELECT * FROM client_schedule.users";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

}

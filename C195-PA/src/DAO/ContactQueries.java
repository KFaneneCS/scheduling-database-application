package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class that holds single database query method pertaining to the Contact class.
 *
 * @author Kyle Fanene
 */
public class ContactQueries {

    /**
     * Method that queries the database to select all from the contacts table.
     *
     * @return Returns the ResultSet from selecting all contacts query.
     */
    public static ResultSet selectAllContacts() throws SQLException {
        String sql = "SELECT * FROM contacts";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

}


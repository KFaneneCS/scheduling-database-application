package DAO;

import utility.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactQueries {

    public static ResultSet selectAllContacts() throws SQLException {
        String sql = "SELECT * FROM client_schedule.contacts";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

}


package DAO;

import utility.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserQueries {

    public static int convertToUserId(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getInt("User_ID");
        }
        return -1;
    }

    public static ResultSet selectAllUsers() throws SQLException {
        String sql = "SELECT * FROM client_schedule.users";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

}

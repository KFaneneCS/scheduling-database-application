package utility;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Query {


    public static int TESTupdate(int customerId, String customerName) throws SQLException {
        String sql = "UPDATE test_table SET customer = ? WHERE id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setInt(2, customerId);
        return ps.executeUpdate();
    }

    public static int TESTdelete(int customerId) throws SQLException {
        String sql = "DELETE FROM test_table WHERE id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        return ps.executeUpdate();
    }


    //    public static ResultSet selectCustomer(String name, String address) throws SQLException {
//        String sql = "SELECT * FROM client_schedule.customers WHERE Customer_ID = ?";
//        int id = convertToCustomerId(name, address);
//        System.out.println("TEST:  id returned from conversion call = " + id);
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setInt(1, id);
//        return ps.executeQuery();
//    }

}
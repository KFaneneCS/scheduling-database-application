package utility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TestTableQuery {

    // Updaters
    public static int insert(int newId, String newCustomer) throws SQLException {
        String sql = "INSERT INTO test_table (id, customer) VALUES (?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, newId);
        ps.setString(2, newCustomer);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int update(int customerId, String customerName) throws SQLException {
        String sql = "UPDATE test_table SET customer = ? WHERE id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setInt(2, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int delete(int customerId) throws SQLException {
        String sql = "DELETE FROM test_table WHERE id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    // Queries
    public static void select() throws SQLException {
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

    public static void select(int id) throws SQLException {
        String sql = "SELECT * FROM test_table WHERE id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int customerId = rs.getInt("id");
            String customerName = rs.getString("customer");
            System.out.print(customerId + " | ");
            System.out.print(customerName + "\n");
        }
    }
}

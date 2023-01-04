package helper;

import java.sql.DriverManager;

public abstract class Connection {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static final String password = "Passw0rd!"; // Password
    public static java.sql.Connection connection;  // Connection Interface

    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("~Connection successful!\n");  // FIXME: DWC
        }
        catch(Exception e)
        {
            System.out.println("~Error:" + e.getMessage());  // FIXME: DWC
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("~Connection closed!");  // FIXME: DWC
        } catch(Exception e) {
            AlertPopups.generateErrorMessage(e.getMessage());
        }
    }
}

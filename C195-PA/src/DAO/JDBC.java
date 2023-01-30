package DAO;

import utility.AlertPopups;

import java.sql.DriverManager;

/**
 * Class that enables manipulation of and interaction with applicable database.
 *
 * @author Kyle Fanene
 */
public abstract class JDBC {

    /**
     * Protocol used to connect to database.
     */
    private static final String protocol = "jdbc";

    /**
     * Vendor used to connect to database.
     */
    private static final String vendor = ":mysql:";

    /**
     * Location name used to connect to database.
     */
    private static final String location = "//localhost/";

    /**
     * Database name used to connect to database.
     */
    private static final String databaseName = "client_schedule";

    /**
     * URL used to connect to local database using protocol, vendor, location, and database name.
     */
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL

    /**
     * Driver reference.
     */
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * Username to access database.
     */
    private static final String username = "sqlUser";

    /**
     * Password to access database.
     */
    private static final String password = "Passw0rd!";

    /**
     * JDBC interface.
     */
    public static java.sql.Connection connection;
    private static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Method which establishes connection.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, username, password); // Reference JDBC object
        }
        catch(Exception e)
        {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Method which terminates connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch(Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

package DAO;

import utility.AlertPopups;
import utility.TimeHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * Data access class that holds all database query methods pertaining to the Appointment class.
 *
 * @author Kyle Fanene
 */
public class AppointmentQueries {

    private static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Method that takes appointment information and inserts into appointments table in database.
     *
     * @param title         Appointment title String used in INSERT INTO query.
     * @param description   Appointment description String used in INSERT INTO query.
     * @param location      Appointment location String used in INSERT INTO query.
     * @param type          Appointment type String used in INSERT INTO query.
     * @param start         Appointment start ZonedDateTime object converted to String used in
     *                      INSERT INTO query.
     * @param end           Appointment end ZonedDateTime object converted to String used in
     *                      INSERT INTO query.
     * @param createdBy     User who is creating appointment, as a String, used in INSERT INTO query.
     * @param lastUpdatedBy User who is creating appointment, as a String, used in INSERT INTO query.
     * @param custId        Appointment's corresponding customer ID, as an int, used in INSERT INTO query.
     * @param userId        Appointment's corresponding user ID, as an int, used in INSERT INTO query.
     * @param contId        Appointment's corresponding contact ID, as an int, used in INSERT INTO query.
     *
     * @return              Returns 1 if query is successful, otherwise returns 0.
     */
    public static int insertAppointment(String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end,
                                        String createdBy, String lastUpdatedBy, int custId, int userId, int contId) {
        try {
            String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, " +
                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, TimeHelper.zdtToString(start));
            ps.setString(6, TimeHelper.zdtToString(end));
            ps.setString(7, createdBy);
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, custId);
            ps.setInt(10, userId);
            ps.setInt(11, contId);
            return ps.executeUpdate();
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Method that queries the database to select all from the appointments table.
     *
     * @return Returns the ResultSet from selecting all appointments query.
     */
    public static ResultSet selectAllAppointments() throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

    /**
     * Method that queries the database to select appointment by title, description, and start
     * DateTime.
     *
     * @param title         Appointment title String used in SELECT query.
     * @param description   Appointment description String used in SELECT query.
     * @param start         Appointment start ZonedDateTime object converted to string in SELECT query.
     *
     * @return Returns the ResultSet from selecting given appointment query.
     */
    public static ResultSet selectAppointmentByTable(String title, String description, ZonedDateTime start)
            throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments WHERE Title = ? AND Description = ? AND Start = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, TimeHelper.zdtToString(start));
        return ps.executeQuery();
    }

    /**
     * Method the queries the database to select appointment by ID and a particular table via a
     * tableNum int.
     *
     * @param id        Appointment ID int used in SELECT query.
     * @param tableNum  The desired appointment table from which to query.  Table 1 is "All," table
     *                  2 is "Current Month," and table 3 is "Current Week."
     *
     * @return Returns the ResultSet from selecting given appointment query.
     */
    public static ResultSet selectAppointmentByTable(int id, int tableNum) throws SQLException {

        String sql = "";
        LocalDate initial = LocalDate.now();
        String start;
        String end;
        PreparedStatement ps;
        switch (tableNum) {
            case 1:
                sql = "SELECT * FROM client_schedule.appointments WHERE Appointment_ID = ?";
                ps = JDBC.connection.prepareStatement(sql);
                ps.setInt(1, id);
                return ps.executeQuery();
            case 2:
                sql = "SELECT * FROM client_schedule.appointments " +
                        "WHERE (Appointment_ID = ?) AND (Start BETWEEN ? AND ?)";
                start = initial.with(firstDayOfMonth()).toString();
                end = initial.with(lastDayOfMonth()).atTime(23, 59).toString();
                ps = JDBC.connection.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, start);
                ps.setString(3, end);
                return ps.executeQuery();
            case 3:
                sql = "SELECT * FROM client_schedule.appointments " +
                        "WHERE (Appointment_ID = ?) AND (Start BETWEEN ? AND ?)";
                start = initial.with(DayOfWeek.MONDAY).toString();
                end = initial.with(DayOfWeek.SUNDAY).toString();
                ps = JDBC.connection.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, start);
                ps.setString(3, end);
                return ps.executeQuery();
            default:
                return null;
        }

    }

    /**
     * Method that queries the database to select all appointments for the current month
     * from the appointments table.
     *
     * @return Returns the ResultSet from selecting appointments query for the current month.
     */
    public static ResultSet selectCurrMonthAppointments() throws SQLException {
        LocalDate initial = LocalDate.now();
        String start = initial.with(firstDayOfMonth()).toString();
        String end = initial.with(lastDayOfMonth()).atTime(23, 59).toString();

        String sql = "SELECT * FROM client_schedule.appointments " + "WHERE Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, start);
        ps.setString(2, end);
        return ps.executeQuery();
    }

    /**
     * Method that queries the database to select all appointments for the current week
     * from the appointments table.
     *
     * @return Returns the ResultSet from selecting appointments query for the current week.
     */
    public static ResultSet selectCurrWeekAppointments() throws SQLException {
        LocalDate initial = LocalDate.now();
        String start = initial.with(DayOfWeek.MONDAY).toString();
        String end = initial.with(DayOfWeek.SUNDAY).toString();

        String sql = "SELECT * FROM client_schedule.appointments " + "WHERE Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, start);
        ps.setString(2, end);
        return ps.executeQuery();
    }

    /**
     * Method that queries the database to delete an appointment with a given ID from the database.
     *
     * @param id        Appointment ID int used in DELETE FROM query.
     *
     * @return Returns the ResultSet from executing delete query for particular appointment.
     */
    public static int deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    /**
     * Method that updates a given appointment from database's appointment table.
     *
     * @param apptId        Appointment ID used to identify appointment data to be queried.
     * @param title         Appointment title String used in UPDATE query.
     * @param description   Appointment description String used in UPDATE query.
     * @param location      Appointment location String used in UPDATE query.
     * @param type          Appointment type String used in UPDATE query.
     * @param start         Appointment start ZonedDateTime object converted to String used in
     *                      UPDATE query.
     * @param end           Appointment end ZonedDateTime object converted to String used in
     *                      UPDATE query.
     * @param lastUpdatedBy User who is updating appointment, as a String, used in UPDATE query.
     * @param custId        Appointment's corresponding customer ID, as an int, used in UPDATE query.
     * @param userId        Appointment's corresponding user ID, as an int, used in UPDATE query.
     * @param contId        Appointment's corresponding contact ID, as an int, used in UPDATE query.
     *
     * @return              Returns 1 if query is successful, otherwise returns 0.
     */
    public static int updateAppointment(int apptId, String title, String description, String location,
                                        String type, ZonedDateTime start, ZonedDateTime end, String lastUpdatedBy,
                                        int custId, int userId, int contId) throws SQLException {
        String sql = "UPDATE client_schedule.appointments SET " +
                "Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), " +
                "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, TimeHelper.zdtToString(start));
        ps.setString(6, TimeHelper.zdtToString(end));
        ps.setString(7, lastUpdatedBy);
        ps.setInt(8, custId);
        ps.setInt(9, userId);
        ps.setInt(10, contId);
        ps.setInt(11, apptId);
        return ps.executeUpdate();
    }

}

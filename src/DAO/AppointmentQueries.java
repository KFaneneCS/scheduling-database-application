package DAO;

import utility.AlertPopups;
import utility.JDBC;
import utility.TimeHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class AppointmentQueries {

    private static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";

    public static int insertAppointment(String ti, String d, String l, String ty, ZonedDateTime start,
                                        ZonedDateTime end, int custId, int userId, int contId) throws SQLException {
        try {
            String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, " +
                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NOW(), 'script', NOW(), 'script', ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, ti);
            ps.setString(2, d);
            ps.setString(3, l);
            ps.setString(4, ty);
            ps.setString(5, TimeHelper.zdtToString(start));
            System.out.println("TEST:  The String that is passed to the database when insertAppointment is called == " + TimeHelper.zdtToString(start));
            ps.setString(6, TimeHelper.zdtToString(end));
            ps.setInt(7, custId);
            ps.setInt(8, userId);
            ps.setInt(9, contId);
            return ps.executeUpdate();
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
            return -1;
        }
    }

    public static ResultSet selectAllAppointments() throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

    public static ResultSet selectAppointmentByTable(String title, String description, ZonedDateTime start) throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments WHERE Title = ? AND Description = ? AND Start = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, TimeHelper.zdtToString(start));
        return ps.executeQuery();
    }

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
                sql = "SELECT * FROM client_schedule.appointments WHERE (Appointment_ID = ?) AND (Start BETWEEN ? AND ?)";
                start = initial.with(firstDayOfMonth()).toString();
                end = initial.with(lastDayOfMonth()).atTime(23, 59).toString();
                ps = JDBC.connection.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, start);
                ps.setString(3, end);
                return ps.executeQuery();
            case 3:
                sql = "SELECT * FROM client_schedule.appointments WHERE (Appointment_ID = ?) AND (Start BETWEEN ? AND ?)";
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

    public static ResultSet selectCurrMonthAppointments() throws SQLException {
        LocalDate initial = LocalDate.now();
        String start = initial.with(firstDayOfMonth()).toString();
        String end = initial.with(lastDayOfMonth()).atTime(23, 59).toString();

        String sql = "SELECT * FROM client_schedule.appointments " +
                "WHERE Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, start);
        ps.setString(2, end);
        return ps.executeQuery();
    }

    public static ResultSet selectCurrWeekAppointments() throws SQLException {
        LocalDate initial = LocalDate.now();
        String start = initial.with(DayOfWeek.MONDAY).toString();
        String end = initial.with(DayOfWeek.SUNDAY).toString();

        String sql = "SELECT * FROM client_schedule.appointments " +
                "WHERE Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, start);
        ps.setString(2, end);
        return ps.executeQuery();
    }

    public static int deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    public static int updateAppointment(int apptId, String title, String description, String location,
                                        String type, ZonedDateTime startZDT, ZonedDateTime endZDT,
                                        int customerId, int userId, int contactID) throws SQLException {
        String sql = "UPDATE client_schedule.appointments SET " +
                "Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Last_Update = NOW(), Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, TimeHelper.zdtToString(startZDT));
        ps.setString(6, TimeHelper.zdtToString(endZDT));
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactID);
        ps.setInt(10, apptId);
        return ps.executeUpdate();
    }



    // FIXME:  Need to finish this one including ZDT return type
    public static void TESTselectAppointmentDate() throws SQLException {
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

    // FIXME
    public static ZonedDateTime selectAppointmentDate(int appointmentId) throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        ResultSet rs = ps.executeQuery();
        String dateTimeFromDB = null;
        while (rs.next()) {     // TODO:  Need to address possibility of no entries found
            dateTimeFromDB = rs.getString("Start");
        }
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeFromDB, DTF);
        ZoneId zoneId = ZoneId.of("UTC");
        return ZonedDateTime.of(localDateTime, zoneId);
    }
}

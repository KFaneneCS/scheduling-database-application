package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * Class which provides various means of access to Appointment class, such as retrieving appointment
 * object lists, adding, deleting, and updating Appointment objects, looking up appointments, and
 * converting data from database into Appointment object.
 *
 * @author Kyle Fanene
 */
public class AppointmentAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Observable list of all Appointment objects.
     */
    public static final ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     * Observable list of filtered Appointment objects.
     */
    public static final ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    /**
     * Method that returns allAppointments list.
     *
     * @return Returns allAppointments ObservableList.
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    /**
     * Method that returns filteredAppointments list.
     *
     * @return Returns filteredAppointments ObservableList.
     */
    public static ObservableList<Appointment> getFilteredAppointments() {
        return filteredAppointments;
    }

    /**
     * Method that initializes Appointment objects via appointments table in database.
     */
    public static void initializeAppointments() {

        try {
            ResultSet rs = AppointmentQueries.selectAllAppointments();

            while (rs.next()) {
                Appointment appointment = getApptObjFromDB(rs);
                addAppointment(appointment);
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Method that adds Appointment object to allAppointments list
     *
     * @param appointment       Appointment object to be added.
     */
    public static void addAppointment(Appointment appointment) {
        allAppointments.add(appointment);
    }

    /**
     * Method that removes Appointment object to allAppointments list
     *
     * @return Returns true if Appointment object was successfully removed, otherwise false.
     */
    public static boolean removeAppointment(Appointment appointment) {
        return allAppointments.remove(appointment);
    }

    /**
     * Method that takes appointment information from database and returns a new Appointment object
     * based on said data.
     *
     * @param rs        ResultSet used to query database for appointment data to use.
     *
     * @return Returns newly created Appointment object.
     */
    public static Appointment getApptObjFromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("Appointment_ID");
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String type = rs.getString("Type");
        String start = rs.getString("Start");
        String end = rs.getString("End");
        String createDate = rs.getString("Create_Date");
        String createdBy = rs.getString("Created_By");
        String lastUpdate = rs.getString("Last_Update");
        String lastUpdatedBy = rs.getString("Last_Updated_By");
        int customerId = rs.getInt("Customer_ID");
        int userId = rs.getInt("User_ID");
        int contactId = rs.getInt("Contact_ID");
        return new Appointment(id, title, description, location, type, start, end, createDate, createdBy, lastUpdate,
                lastUpdatedBy, customerId, userId, contactId);
    }

    /**
     * Method that updates Appointment object in allAppointments list.
     *
     * @param id                    Appointment ID to determine Appointment object's index position in list.
     *
     * @param updatedAppointment    Updated Appointment object.
     */
    public static void updateAppointment(int id, Appointment updatedAppointment) {

        int index = lookupIndex(id);
        allAppointments.set(index, updatedAppointment);
    }

    /**
     * Method that searches for allAppointments list index given an appointment ID and returns index.
     *
     * @param appointmentId       Appointment ID to search list against.
     *
     * @return Returns index of Appointment object in list; returns -1 if not found.
     */
    public static int lookupIndex(int appointmentId) {

        for (Appointment appointment : getAllAppointments()) {

            if (appointment.getId() == appointmentId) {
                return getAllAppointments().indexOf(appointment);
            }
        }
        AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        return -1;
    }

    /**
     * Method that searches allAppointments list using appointment ID and returns
     * Appointment object.
     *
     * @param appointmentId       Appointment ID to search list against.
     *
     * @return Returns Appointment object; returns null if not found.
     */
    public static Appointment lookupAppointment(int appointmentId) {

        for (Appointment appointment : getAllAppointments()) {

            if (appointment.getId() == appointmentId) {
                return appointment;
            }
        }
        return null;
    }

    /**
     * Method that searches allAppointments list via user text input and returns list
     * of Appointment objects whose name contains user text.
     *
     * @param userText       User text to search list against.
     *
     * @return Returns list of Appointment objects whose name contains user's text input.
     */
    public static ObservableList<Appointment> lookupAppointments(String userText) {

        if (!getFilteredAppointments().isEmpty()) {
            getFilteredAppointments().clear();
        }
        for (Appointment appointment : getAllAppointments()) {
            if (appointment.getTitle().contains(userText)) {
                getFilteredAppointments().add(appointment);
            }
        }
        return getFilteredAppointments();
    }

    /**
     * Method that contains main logic behind adding Appointment objects and data to database.
     *
     * @param title             Title of newly created appointment and database insert.
     * @param description       Description of newly created appointment and database insert.
     * @param location          Location of newly created appointment and database insert.
     * @param contactId         Corresponding contact ID of newly created appointment and database insert.
     * @param type              Type of newly created appointment and database insert.
     * @param startZDT          Start ZonedDateTime object of newly created appointment and database insert.
     * @param endZDT            End ZonedDateTime object of newly created appointment and database insert.
     * @param createdBy         User who created this appointment object.
     * @param lastUpdatedBy     User who created this appointment object.
     * @param customerId        Associated customer ID of newly created appointment and database insert.
     * @param userId            Associated user ID of newly created appointment and database insert.
     *
     */
    public static void executeAdd(String title, String description, String location, int contactId,
                                  String type, ZonedDateTime startZDT, ZonedDateTime endZDT, String createdBy,
                                  String lastUpdatedBy, int customerId, int userId) throws SQLException {

        if (AppointmentQueries.insertAppointment(title, description, location, type, startZDT, endZDT, createdBy,
                lastUpdatedBy, customerId, userId, contactId) < 1) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        } else {
            ResultSet rs = AppointmentQueries.selectAppointmentByTable(title, description, startZDT);

            while (rs.next()) {
                Appointment appointment = getApptObjFromDB(rs);
                addAppointment(appointment);
            }
        }
    }

    /**
     * Method that contains main logic behind deleting Appointment objects and data from database.
     *
     * @param appointmentId     Appointment ID to determine appointment data to be deleted.
     */
    public static void executeDelete(int appointmentId) throws SQLException {

        Appointment apptToDelete = lookupAppointment(appointmentId);

        if (apptToDelete == null) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        } else if (AppointmentQueries.deleteAppointment(appointmentId) < 1) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        } else {
            AlertPopups.generateCancelConfirmationMessage(apptToDelete.getId(), apptToDelete.getType());
            removeAppointment(apptToDelete);
        }
    }

    /**
     * Method that determines if user has any appointments within 15 minutes.
     *
     * @param user              User object against whose appointment times upcoming
     *                          appointments are compared.
     *
     * @return Returns Appointment object if one exists within 15 minutes of now,
     * otherwise returns null.
     */
    public static Appointment checkUpcomingAppointment(User user) {

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime offset = ZonedDateTime.now().plusMinutes(15);

        for (Appointment appointment : getAllAppointments()) {

            if (appointment.getUserId() == user.getId()) {

                ZonedDateTime start = appointment.getStart();

                if ((start.isAfter(now) || start.isEqual(now)) && (start.isBefore(offset) || start.isEqual(offset))) {
                    return appointment;
                }
            }
        }
        return null;
    }
}

package controller;

import DAO.AppointmentQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

public class AppointmentAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";
    public static final ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public static final ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public static ObservableList<Appointment> getFilteredAppointments() {
        return filteredAppointments;
    }

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

    public static void addAppointment(Appointment appointment) {
        allAppointments.add(appointment);
    }

    public static boolean removeAppointment(Appointment appointment) {
        return allAppointments.remove(appointment);
    }

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

    public static void updateAppointment(int id, Appointment updatedAppointment) {

        int index = lookupIndex(id);
        allAppointments.set(index, updatedAppointment);
    }

    public static int lookupIndex(int appointmentId) {

        for (Appointment appointment : getAllAppointments()) {

            if (appointment.getId() == appointmentId) {
                return getAllAppointments().indexOf(appointment);
            }
        }
        AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        return -1;
    }

    public static Appointment lookupAppointment(int appointmentId) {

        for (Appointment appointment : getAllAppointments()) {

            if (appointment.getId() == appointmentId) {
                return appointment;
            }
        }
        return null;
    }

    public static ObservableList<Appointment> lookupAppointments(String apptTitle) {

        if (!getFilteredAppointments().isEmpty()) {
            getFilteredAppointments().clear();
        }
        for (Appointment appointment : getAllAppointments()) {
            if (appointment.getTitle().contains(apptTitle)) {
                getFilteredAppointments().add(appointment);
            }
        }
        return getFilteredAppointments();
    }

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

}

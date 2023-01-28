package utility;

import DAO.AppointmentAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Utility class that handles various date and time matters, including conversions, lists of times in
 * necessary data types, and comparing dates and times.
 *
 * @author Kyle Fanene
 */
public class TimeHelper {

    /**
     * The default DateTime format used throughout program and in database.
     */
    private static final DateTimeFormatter STANDARD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * List of hours as ints; used for combo box selection.
     */
    private static final ObservableList<Integer> hoursList = FXCollections.observableArrayList();

    /**
     * List of minute options as Strings; used for combo box selection.
     */
    private static final ObservableList<String> minutesList = FXCollections.observableArrayList("00", "15", "30", "45");

    /**
     * Method that returns hoursList.
     *
     * @return Returns hoursList ObservableList.
     */
    public static ObservableList<Integer> getHoursList() {
        return hoursList;
    }

    /**
     * Method that sets hoursList.  Hours are between business's operating hours (8am - 10pm EST)
     * converted to local time.
     */
    public static void setHoursList() {

        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estOpen = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 0), estZoneId);
        int opening = estToLocal(estOpen).getHour();

        int midnightTracker = 0;
        for (int i = 0; i < 10; i++) {
            if (opening + i < 24) {
                hoursList.add(LocalTime.of(opening + i, 0).getHour());
                midnightTracker++;
            } else {
                hoursList.add(LocalTime.of(i - midnightTracker, 0).getHour());
            }
        }
    }

    /**
     * Method that returns minutesList.
     *
     * @return Returns minutesList ObservableList.
     */
    public static ObservableList<String> getMinutesList() {
        return minutesList;
    }

    /**
     * Method that converts ZonedDateTime object from UTC to user's local time.
     *
     * @param dateUTC       ZonedDateTime object to be converted.
     *
     * @return Returns local ZonedDateTime object.
     */
    public static ZonedDateTime utcToLocal(ZonedDateTime dateUTC) {
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        return dateUTC.toInstant().atZone(localZoneId);
    }

    /**
     * Method that converts ZonedDateTime object from EST to user's local time.
     *
     * @param dateEST       ZonedDateTime object to be converted.
     *
     * @return Returns local ZonedDateTime object.
     */
    public static ZonedDateTime estToLocal(ZonedDateTime dateEST) {
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        return dateEST.toInstant().atZone(localZoneId);
    }

    /**
     * Method that converts String from database into ZonedDateTime object in UTC.
     *
     * @param dbDateTimeString       DateTime String (from database).
     *
     * @return Returns UTC ZonedDateTime object.
     */
    public static ZonedDateTime stringToZDT(String dbDateTimeString) {

        LocalDateTime ldt = LocalDateTime.parse(dbDateTimeString, STANDARD_FORMATTER);
        return utcToLocal(ldt.atZone(ZoneId.of("UTC")));
    }

    /**
     * Method that converts String from database, which was in UTC, into a String of the
     * same format but displaying local time.  This is done by first converting String
     * from database into ZonedDateTime object, then making a UTC-to-local conversion, and
     * finally converting back to a String.
     *
     * @param dbDateTimeString       DateTime String (from database).
     *
     * @return Returns local date and time as String in same format.
     */
    public static String dbStringToLocalString(String dbDateTimeString) {

        LocalDateTime ldt = LocalDateTime.parse(dbDateTimeString, STANDARD_FORMATTER);
        ZonedDateTime dbZDT = ZonedDateTime.of(ldt, ZoneId.of("UTC"));
        ZonedDateTime localZDT = utcToLocal(dbZDT);
        return localZDT.format(STANDARD_FORMATTER);
    }

    /**
     * Method that converts ZonedDateTime object into formatted String.
     *
     * @param zdt       ZonedDateTime object to be converted.
     *
     * @return Returns formatted String.
     */
    public static String zdtToString(ZonedDateTime zdt) {

        return zdt.format(STANDARD_FORMATTER);
    }

    /**
     * Method that converts DateTime date, integer hour, and String minute from Appoinments
     * form into a ZonedDateTime object.
     *
     * @param localDate         LocalDate from DatePicker to be converted.
     * @param hour              Integer hour from hour combo box to be converted.
     * @param minuteString      String minute from minute combo box to be converted.
     *
     * @return Returns UTC ZonedDateTime object representing the LocalDate, hour and minute passed.
     */
    public static ZonedDateTime userInputToZDT(LocalDate localDate, int hour, String minuteString) {

        int minute = Integer.parseInt(minuteString);
        LocalTime localTime = LocalTime.of(hour, minute);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId localZoneId = ZoneId.systemDefault();
        ZonedDateTime zdtLocal = localDateTime.atZone(localZoneId);
        return zdtLocal.withZoneSameInstant(ZoneId.of("UTC"));
    }

    /**
     * Method that determines whether there is an overlapping appointment for a given Customer
     * on an add attempt.
     *
     * @param customerId            Customer ID of customer to check current appointments for.
     * @param checkStart            The starting date and time to check for overlap against.
     * @param checkEnd              The ending date and time to check for overlap against.
     *
     * @return Returns true if an appointment conflict exists for a given customer, otherwise false.
     */
    public static boolean addHasOverlap(int customerId, ZonedDateTime checkStart, ZonedDateTime checkEnd) {

        for (Appointment appointment : AppointmentAccess.getAllAppointments()) {

            if (appointment.getCustomerId() == customerId) {

                ZonedDateTime start = appointment.getStart();
                ZonedDateTime end = appointment.getEnd();

                if (isOverlapping(checkStart, checkEnd, start, end)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method that determines whether there is an overlapping appointment for a given Customer
     * on an update attempt.
     *
     * @param customerId            Customer ID of customer to check current appointments for.
     * @param appointmentId         Appointment ID of appointment being updated.  This should
     *                              not be considered when checking for conflicts.
     * @param checkStart            The starting date and time to check for overlap against.
     * @param checkEnd              The ending date and time to check for overlap against.
     *
     * @return Returns true if an appointment conflict exists for a given customer, otherwise false.
     */
    public static boolean updateHasOverlap(int customerId, int appointmentId, ZonedDateTime checkStart, ZonedDateTime checkEnd) {

        for (Appointment appointment : AppointmentAccess.getAllAppointments()) {

            if (appointment.getId() == appointmentId) {
                break;
            }

            if (appointment.getCustomerId() == customerId) {

                ZonedDateTime start = appointment.getStart();
                ZonedDateTime end = appointment.getEnd();

                if (isOverlapping(checkStart, checkEnd, start, end)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method that is the main logic for checking for date/time conflicts.
     *
     * @param checkS            Starting ZonedDateTime object of date and time to be checked.
     * @param checkE            Ending ZonedDateTime object of date and time to be checked.
     * @param start             Already-scheduled starting ZonedDateTime we are checking against.
     * @param end               Already-scheduled ending ZonedDateTime we are checking against.
     *
     * @return Returns true if an appointment conflict exists for a given customer, otherwise false.
     */
    public static boolean isOverlapping(ZonedDateTime checkS, ZonedDateTime checkE, ZonedDateTime start, ZonedDateTime end) {

        // Check #1
        if ((checkS.isAfter(start) || (checkS.isEqual(start))) && ((checkS.isBefore(end)))) {
            return true;
        }

        // Check #2
        if ((checkE.isAfter(start)) && (checkE.isBefore(end) || checkE.isEqual(end))) {
            return true;
        }

        // Check #3
        return (checkS.isBefore(start) || checkS.isEqual(start)) && (checkE.isAfter(end) || checkE.isEqual(end));
    }
}

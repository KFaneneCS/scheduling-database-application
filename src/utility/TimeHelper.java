package utility;

import controller.AppointmentAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class TimeHelper {

    private static final DateTimeFormatter STANDARD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ObservableList<Integer> hoursList = FXCollections.observableArrayList();
    private static final ObservableList<String> minutesList = FXCollections.observableArrayList("00", "15", "30", "45");


    public static ObservableList<Integer> getHoursList() {
        return hoursList;
    }

    public static ObservableList<String> getMinutesList() {
        return minutesList;
    }


    public static ZonedDateTime utcToLocal(ZonedDateTime dateUTC) {
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        return dateUTC.toInstant().atZone(localZoneId);
    }

    public static ZonedDateTime localToUTC(ZonedDateTime dateLocal) {
        ZoneId utcZoneId = ZoneId.of("UTC");
        return dateLocal.toInstant().atZone(utcZoneId);
    }

    public static ZonedDateTime utcToEST(ZonedDateTime dateUTC) {
        ZoneId estZoneId = ZoneId.of("America/New_York");
        return dateUTC.toInstant().atZone(estZoneId);
    }

    public static ZonedDateTime estToLocal(ZonedDateTime dateEST) {
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        return dateEST.toInstant().atZone(localZoneId);
    }

    public static ZonedDateTime stringToZDT(String dbDateTimeString) {

        LocalDateTime ldt = LocalDateTime.parse(dbDateTimeString, STANDARD_FORMATTER);
        return utcToLocal(ldt.atZone(ZoneId.of("UTC")));
    }

    public static String dbStringToLocalString(String dbDateTimeString) {

        LocalDateTime ldt = LocalDateTime.parse(dbDateTimeString, STANDARD_FORMATTER);
        ZonedDateTime dbZDT = ZonedDateTime.of(ldt, ZoneId.of("UTC"));
        ZonedDateTime localZDT = utcToLocal(dbZDT);
        return localZDT.format(STANDARD_FORMATTER);
    }

    public static String zdtToString(ZonedDateTime zdt) {

        return zdt.format(STANDARD_FORMATTER);
    }

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

    public static ZonedDateTime userInputToZDT(LocalDate localDate, int hour, String minuteString) {

        int minute = Integer.parseInt(minuteString);
        LocalTime localTime = LocalTime.of(hour, minute);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId localZoneId = ZoneId.systemDefault();
        ZonedDateTime zdtLocal = localDateTime.atZone(localZoneId);
        return zdtLocal.withZoneSameInstant(ZoneId.of("UTC"));
    }


    public static Appointment checkUpcomingAppointment(User user) {

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime offset = ZonedDateTime.now().plusMinutes(15);

        for (Appointment appointment : AppointmentAccess.getAllAppointments()) {

            if (appointment.getUserId() == user.getId()) {

                ZonedDateTime start = appointment.getStart();

                if ((start.isAfter(now) || start.isEqual(now)) && (start.isBefore(offset) || start.isEqual(offset))) {
                    return appointment;
                }
            }
        }
        return null;
    }


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

package utility;

import DAO.AppointmentAccess;
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
    private static final ObservableList<String> minutesList =
            FXCollections.observableArrayList("00", "15", "30", "45");


    public static ObservableList<Integer> getHoursList() {
        return hoursList;
    }

    public static ObservableList<String> getMinutesList() {
        return minutesList;
    }


    public static ZonedDateTime utcToLocal(ZonedDateTime dateUTC) {
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
//        ZonedDateTime utcToLocal = dateUTC.toInstant().atZone(localZoneId);
        ZonedDateTime utcToLocal = dateUTC.toInstant().atZone(localZoneId);
        return utcToLocal;
    }

    public static ZonedDateTime localToUTC(ZonedDateTime dateLocal) {
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime localToUTC = dateLocal.toInstant().atZone(utcZoneId);
        return localToUTC;
    }

    public static ZonedDateTime utcToEST(ZonedDateTime dateUTC) {
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime utcToEST = dateUTC.toInstant().atZone(estZoneId);
        return utcToEST;
    }

    public static ZonedDateTime estToLocal(ZonedDateTime dateEST) {
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime estToLocal = dateEST.toInstant().atZone(localZoneId);
        return estToLocal;
    }

    public static ZonedDateTime stringToZDT(String dbDateTimeString) {

//        LocalDateTime ldt = LocalDateTime.parse(dbDateTimeString, STANDARD_FORMATTER);
//        return ldt.atZone(ZoneId.systemDefault());
        LocalDateTime ldt = LocalDateTime.parse(dbDateTimeString, STANDARD_FORMATTER);
        return utcToLocal(ldt.atZone(ZoneId.of("UTC")));
    }

    public static String zdtToString(ZonedDateTime zdt) {
        zdt = localToUTC(zdt);
        return zdt.format(STANDARD_FORMATTER);
    }

    public static String currDateTimeAsString() {
        LocalDateTime currDT = LocalDateTime.now();
        return currDT.format(STANDARD_FORMATTER);
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
        ZoneId localZoneId = ZoneId.of("UTC");
        return ZonedDateTime.of(localDateTime, localZoneId);
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
        System.out.println("NO OVERLAP");
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
                };
            }
        }
        System.out.println("NO OVERLAP");
        return false;
    }


    public static boolean isOverlapping(ZonedDateTime checkS, ZonedDateTime checkE, ZonedDateTime start, ZonedDateTime end) {

        // Check #1
        if ((checkS.isAfter(start) || (checkS.isEqual(start))) && ((checkS.isBefore(end)))) {

            System.out.println("Confirmed OVERLAP");
            return true;
        }

        // Check #2
        if ((checkE.isAfter(start)) && (checkE.isBefore(end) || checkE.isEqual(end))) {

            System.out.println("Confirmed OVERLAP");
            return true;
        }

        // Check #3
        if ((checkS.isBefore(start) || checkS.isEqual(start)) && (checkE.isAfter(end) || checkE.isEqual(end))) {

            System.out.println("Confirmed OVERLAP");
            return true;
        }
        return false;
    }
}


//        switch (choiceId) {
//            case 0:
//                // return hour list;
//
//
//
//            case 1:
//                // return minute list
//
//
//
//
//            default:
//                AlertPopups.generateErrorMessage("Error");
//                return null;
//        }


        //    public static void experimenting() {        // FIXME:  Delete
//
////        // All user's current info
////        LocalDate date = LocalDate.now();
////        System.out.println("Current date: " + date);
////        LocalTime time = LocalTime.now();
////        System.out.println("Current time: " + time);
////        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
////        System.out.println("Current zoneId: " + zoneId);
////
////        // Creates ZDT object from user's info
////        ZonedDateTime localZDT = ZonedDateTime.of(date, time, zoneId);
////        System.out.println("ZonedDateTime using above: " + localZDT);
////
////        // Convert ZDT to Instant in order to make UTC conversion
////        Instant localToUTCInstant = localZDT.toInstant();
////        System.out.println("Local-to-UTC Instant Object: " + localToUTCInstant);
////
////        // Convert instant object to ZDT
////        ZonedDateTime utcLocalToZDT = localToUTCInstant.atZone(ZoneId.of("UTC"));
////        System.out.println("Changing Instant to ZDT object: " + utcLocalToZDT);
////
////        // Convert back to local time
////        ZonedDateTime utcToLocal = localToUTCInstant.atZone(zoneId);
////        System.out.println("UTC Instant back to local: " + utcToLocal);
////
////        System.out.println("\n");
////
////    }



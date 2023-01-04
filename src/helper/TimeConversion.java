package helper;

import java.time.*;
import java.util.TimeZone;

public class TimeConversion {

    public static void displayZoneIds() {
        ZoneId.getAvailableZoneIds().stream().forEach(System.out::println);
    }

    public static void displayZoneIds(String country) {
        ZoneId.getAvailableZoneIds().stream().filter(c -> c.contains(country)).forEach(System.out::println);
    }

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

    public static ZonedDateTime convertUTCToLocal(ZonedDateTime dateUTC) {
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
//        ZonedDateTime utcToLocal = dateUTC.toInstant().atZone(localZoneId);
        ZonedDateTime utcToLocal = dateUTC.toInstant().atZone(localZoneId);
        return utcToLocal;
    }

    public static ZonedDateTime convertLocalToUTC(ZonedDateTime dateLocal) {
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime localToUTC = dateLocal.toInstant().atZone(utcZoneId);
        return localToUTC;
    }

    public static ZonedDateTime convertUTCToEST(ZonedDateTime dateUTC) {
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime utcToEST = dateUTC.toInstant().atZone(estZoneId);
        return utcToEST;
    }

}

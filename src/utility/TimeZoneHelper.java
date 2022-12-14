package utility;

import java.time.ZoneId;

public class TimeZoneHelper {

    public static void displayZoneIds() {
        ZoneId.getAvailableZoneIds().stream().forEach(System.out::println);
    }

    public static void displayZoneIds(String country) {
        ZoneId.getAvailableZoneIds().stream().filter(c -> c.contains(country)).forEach(System.out::println);
    }
}

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utility.JDBC;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TimeZone;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        stage.setTitle("First Screen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    // FIXME:  TEST CHANGE~~~
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();

/*        // FIXME:  Testing
        int rowsAffected = TestTableQuery.delete(3);
        if(rowsAffected > 0) {
            System.out.println("Insert successful");
        }
        else {
            System.out.println("Insert FAILED");
        }

        TestTableQuery.select(2);*/

//        TimeZoneHelper.displayZoneIds();
//        TimeZoneHelper.displayZoneIds("Europe");

        // .of() used to explicitly create a ZonedDateTime object
        LocalDate parisDate = LocalDate.of(2022, 12, 13);
        LocalTime parisTime = LocalTime.of(5, 0);
        ZoneId parisZoneId = ZoneId.of("Europe/Paris");
        ZonedDateTime parisZDT = ZonedDateTime.of(parisDate, parisTime, parisZoneId);
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());

        //  toInstant() converts ZonedDateTime object to UTC
        Instant parisToGMTInstant = parisZDT.toInstant();
        // withZoneSameInstant() converts ZDT object to another using ZoneId
        ZonedDateTime parisToLocalZDT = parisZDT.withZoneSameInstant(localZoneId);
        // atZone() converts UTC to ZDT object using ZonedId
        ZonedDateTime gmtToLocalZDT = parisToGMTInstant.atZone(localZoneId);


        System.out.println("Local: " + ZonedDateTime.now());
        System.out.println("Paris: " + parisZDT);
        System.out.println("Paris->GMT: " + parisToGMTInstant);
        System.out.println("Paris->Local: " + parisToLocalZDT);
        System.out.println("GMT->Local: " + gmtToLocalZDT);
        System.out.println("GMT->LocalDate " + gmtToLocalZDT.toLocalDate());
        System.out.println("GMT->LocalTime " + gmtToLocalZDT.toLocalTime() + "\n");

//        String date = String.valueOf(gmtToLocalZDT.toLocalDate());
//        String time = String.valueOf(gmtToLocalZDT.toLocalTime());
//        System.out.println("Date: " + date + " Time: " + time);

        // Trying the conversion another way
        LocalDateTime parisDateTime = parisZDT.toLocalDateTime();
        System.out.println("BEFORE: " + parisDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedParisDateTime = parisDateTime.format(formatter);
        System.out.println("AFTER: " + formattedParisDateTime);













        launch(args);
        JDBC.closeConnection();
    }
}
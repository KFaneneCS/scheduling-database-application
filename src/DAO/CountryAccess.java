package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CountryAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";
    private static final ObservableList<Country> allCountries = FXCollections.observableArrayList();

    public static ObservableList<Country> getAllCountries() {
        return allCountries;
    }

    public static void initializeCountries() {

        try {
            ResultSet rs = CountryQueries.selectAllCountries();

            while (rs.next()) {
                Country country = getCountryObjectFromDB(rs);
                addCountry(country);
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void addCountry(Country country) {
        allCountries.add(country);
    }

    public static Country getCountryObjectFromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("Country_ID");
        String countryName = rs.getString("Country");
        String createDate = rs.getString("Create_Date");
        String createdBy = rs.getString("Created_By");
        String lastUpdate = rs.getString("Last_Update");
        String lastUpdatedBy = rs.getString("Last_Updated_By");

        return new Country(id, countryName, createDate, createdBy, lastUpdate, lastUpdatedBy);
    }

    public static Country lookupCountry(int countryId) {

        for (Country country : getAllCountries()) {

            if (country.getId() == countryId) {
                return country;
            }
        }
        return null;
    }

    public static int lookupCountryId(String countryName) {

        for (Country country : getAllCountries()) {

            if (Objects.equals(country.getName(), countryName)) {
                return country.getId();
            }
        }
        return -1;
    }

}

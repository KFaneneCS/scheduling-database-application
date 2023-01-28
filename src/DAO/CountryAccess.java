package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Class which provides various means of access to Country class, such as retrieving Country
 * object lists, adding User objects (at initialization), and looking up countries.
 *
 * @author Kyle Fanene
 */
public class CountryAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Observable list of all Country objects.
     */
    private static final ObservableList<Country> allCountries = FXCollections.observableArrayList();

    /**
     * Method that returns allCountries list.
     *
     * @return Returns allContacts ObservableList.
     */
    public static ObservableList<Country> getAllCountries() {
        return allCountries;
    }

    /**
     * Method that initializes Country objects via countries table in database.
     */
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

    /**
     * Method that adds Country object to allCountries list
     *
     * @param country       Country object to be added.
     */
    public static void addCountry(Country country) {
        allCountries.add(country);
    }

    /**
     * Method that takes country information from database and returns a new Country object
     * based on said data.
     *
     * @param rs        ResultSet used to query database for country data to use.
     *
     * @return Returns newly created Country object.
     */
    public static Country getCountryObjectFromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("Country_ID");
        String countryName = rs.getString("Country");
        String createDate = rs.getString("Create_Date");
        String createdBy = rs.getString("Created_By");
        String lastUpdate = rs.getString("Last_Update");
        String lastUpdatedBy = rs.getString("Last_Updated_By");

        return new Country(id, countryName, createDate, createdBy, lastUpdate, lastUpdatedBy);
    }

    /**
     * Method that searches allCountries list using country ID and returns
     * Country object.
     *
     * @param countryId       Country ID to search list against.
     *
     * @return Returns Country object; returns null if not found.
     */
    public static Country lookupCountry(int countryId) {

        for (Country country : getAllCountries()) {

            if (country.getId() == countryId) {
                return country;
            }
        }
        return null;
    }

    /**
     * Method that searches allCountries list using country name String and returns
     * Country ID.
     *
     * @param countryName       String country name to search list against.
     *
     * @return Returns Country ID integer; returns -1 if not found.
     */
    public static int lookupCountryId(String countryName) {

        for (Country country : getAllCountries()) {

            if (Objects.equals(country.getName(), countryName)) {
                return country.getId();
            }
        }
        return -1;
    }

}

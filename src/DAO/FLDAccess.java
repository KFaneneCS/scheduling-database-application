package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Class which provides various means of access to FirstLevelDivision class, such as retrieving
 * first-level division object lists, adding division objects (at initialization), and looking up
 * divisions.
 *
 * @author Kyle Fanene
 */
public class FLDAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Observable list of all First-Level Division objects.
     */
    private static final ObservableList<FirstLevelDivision> allFLDs = FXCollections.observableArrayList();

    /**
     * Method that returns all first-level divisions list.
     *
     * @return Returns allFLDS ObservableList.
     */
    public static ObservableList<FirstLevelDivision> getAllFLDs() {
        return allFLDs;
    }

    /**
     * Method that adds FirstLevelDivision object to allFLDs list
     *
     * @param fld       FirstLevelDivision object to be added.
     */
    public static void addFLD(FirstLevelDivision fld) {
        allFLDs.add(fld);
    }

    /**
     * Method that gets all FirstLevelDivision objects as a list of Strings corresponding
     * to passed country ID.
     *
     * @param countryId       Country ID to search against for its first-level divisions.
     *
     * @return Returns ObervableList of first-level divisions as Strings for corresponding country.
     */
    public static ObservableList<String> getFilteredFLDsAsStrings(int countryId) throws SQLException {
        return FLDQueries.getDivisionList(countryId);
    }

    /**
     * Method that initializes FirstLevelDivision objects via first_level_divisions table in database.
     */
    public static void initializeFLDs() {

        try {
            ResultSet rs = FLDQueries.selectAllFLDs();

            while (rs.next()) {
                FirstLevelDivision fld = getFLDObjectFromDB(rs);
                addFLD(fld);
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Method that takes first-level division information from database and returns a new FirstLevelDivision
     * object based on said data.
     *
     * @param rs        ResultSet used to query database for first-level division data to use.
     *
     * @return Returns newly created FirstLevelDivision object.
     */
    public static FirstLevelDivision getFLDObjectFromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("Division_ID");
        String division = rs.getString("Division");
        String createDate = rs.getString("Create_Date");
        String createdBy = rs.getString("Created_By");
        String lastUpdate = rs.getString("Last_Update");
        String lastUpdatedBy = rs.getString("Last_Updated_By");
        int countryId = rs.getInt("Country_ID");

        return new FirstLevelDivision(id, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
    }

    /**
     * Method that searches allFLDs list using first-level division ID and returns
     * FirstLevelDivision object.
     *
     * @param divId       First-level division ID to search list against.
     *
     * @return Returns FirstLevelDivision object; returns null if not found.
     */
    public static FirstLevelDivision lookupFLD(int divId) {

        for (FirstLevelDivision fld : getAllFLDs()) {

            if (fld.getId() == divId) {
                return fld;
            }
        }
        return null;
    }

    /**
     * Method that searches allFLDs list using division name String and returns
     * division ID.
     *
     * @param division       String first-level division name to search list against.
     *
     * @return Returns first-level division ID integer; returns -1 if not found.
     */
    public static int lookupFLDId(String division) {

        for (FirstLevelDivision fld : getAllFLDs()) {

            if (Objects.equals(fld.getDivision(), division)) {
                return fld.getId();
            }
        }
        return -1;
    }

    /**
     * Method that searches allFLDs list using division ID and returns
     * corresponding country ID.
     *
     * @param divId       First-level division ID with which to compare country ID.
     *
     * @return Returns corresponding country ID integer; returns -1 if not found.
     */
    public static int getCountryId(int divId) {

        for (FirstLevelDivision fld : getAllFLDs()) {
            if (fld.getId() == divId) {
                return fld.getCountryId();
            }
        }
        return -1;
    }
}

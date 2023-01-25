package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class FLDAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";
    private static final ObservableList<FirstLevelDivision> allFLDs = FXCollections.observableArrayList();

    public static ObservableList<FirstLevelDivision> getAllFLDs() {
        return allFLDs;
    }

    public static ObservableList<String> getFilteredFLDsAsStrings(int countryId) throws SQLException {
        return FLDQueries.getDivisionList(countryId);
    }

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

    public static void addFLD(FirstLevelDivision fld) {
        allFLDs.add(fld);
    }

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

    public static FirstLevelDivision lookupFLD(int divId) {

        for (FirstLevelDivision fld : getAllFLDs()) {

            if (fld.getId() == divId) {
                return fld;
            }
        }
        return null;
    }

    public static int lookupFLDId(String division) {

        for (FirstLevelDivision fld : getAllFLDs()) {

            if (Objects.equals(fld.getDivision(), division)) {
                return fld.getId();
            }
        }
        return -1;
    }

    public static int getCountryId(int divId) {

        for (FirstLevelDivision fld : getAllFLDs()) {
            if (fld.getId() == divId) {
                return fld.getCountryId();
            }
        }
        return -1;
    }
}

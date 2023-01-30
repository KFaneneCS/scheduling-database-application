package model;

/**
 * Models First-Level Divisions.  Primarily used to determine country and state/province names.
 *
 * @author Kyle Fanene
 */
public class FirstLevelDivision {

    /**
     * FirstLevelDivision's ID.
     */
    private final int id;

    /**
     * FirstLevelDivision's state/province name.
     */
    private final String division;

    /**
     * FirstLevelDivision's create date and time.
     */
    private final String createDate;

    /**
     * FirstLevelDivision's create-by user.
     */
    private final String createdBy;

    /**
     * FirstLevelDivision's last update date and time.
     */
    private final String lastUpdate;

    /**
     * FirstLevelDivision's last-updated-by user.
     */
    private final String lastUpdatedBy;

    /**
     * FirstLevelDivision's associated country ID.
     */
    private final int countryId;

    /**
     * Constructor for created instance of FirstLevelDivision.
     *
     * @param id            ID for the instantiated first-level division.
     * @param division      Division name (state or province) for the instantiated first-level division.
     * @param createDate    Create date and time for the instantiated first-level division.
     * @param createdBy     Name of user who instantiated first-level division.  Since first-level divisions are not
     *                      manipulable by this program, user is "script" unless applicable database is externally modified.
     * @param lastUpdate    Date and time instantiated first-level division was last updated.
     * @param lastUpdatedBy Name of user who last updated instantiated first-level division.  Since first-level divisions
     *                      are not manipulable by this program, user is "script" unless applicable database is
     *                      externally modified.
     * @param countryId     Associated country ID for the instantiated first-level division.
     */
    public FirstLevelDivision(int id, String division, String createDate, String createdBy, String lastUpdate,
                              String lastUpdatedBy, int countryId) {
        this.id = id;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    /**
     * Getter for first-level division ID.
     *
     * @return First-level division ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for first-level division state/province name.
     *
     * @return First-level division state/province name.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Getter for first-level division create date and time.
     *
     * @return First-level division create date and time.
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Getter for user ("script" and unless externally modified) who created first-level division.
     *
     * @return User "script" by default, otherwise user who created first-level division.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Getter for date and time first-level division was last updated.
     *
     * @return Date and time first-level division was last updated.
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Getter for user ("script" unless externally modified) who last updated first-level division.
     *
     * @return User "script" by default, otherwise user who last updated first-level division.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Getter for first-level division's associated country ID.
     *
     * @return First-level division's associated country ID.
     */
    public int getCountryId() {
        return countryId;
    }

}

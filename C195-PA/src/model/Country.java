package model;

/**
 * Models country pertaining to corresponding customer.
 *
 * @author Kyle Fanene
 */
public class Country {

    /**
     * Country's ID.
     */
    private final int id;

    /**
     * Country's name.
     */
    private final String name;

    /**
     * Country's create date and time.
     */
    private final String createDate;

    /**
     * Country's create-by user.
     */
    private final String createdBy;

    /**
     * Country's last update date and time.
     */
    private final String lastUpdate;

    /**
     * Country's last-updated-by user.
     */
    private final String lastUpdatedBy;

    /**
     * Constructor for created instance of Country.
     *
     * @param id            ID for instantiated country.
     * @param countryName   Name for instantiated country.
     * @param createDate    Create date and time for the instantiated country.
     * @param createdBy     Name of user who instantiated country. Since countries are not manipulable by this program,
     *                      user is "script" unless applicable database is externally modified.
     * @param lastUpdate    Date and time instantiated country was last updated.
     * @param lastUpdatedBy Name of user who last updated instantiated country.  Since countries are not manipulable by this program,
     *                      user is "script" unless applicable database is externally modified.
     */
    public Country(int id, String countryName, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = countryName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Getter for country ID.
     *
     * @return Country ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for country name
     *
     * @return Country name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for country created date and time.
     *
     * @return Country create date and time.
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Getter for user ("script" unless externally modified) who created country.
     *
     * @return User "script" by default, otherwise user who created country.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Getter for date and time country was last updated.
     *
     * @return Date and time country was last updated.
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Getter for user ("script" unless externally modified) who last updated country.
     *
     * @return User "script" by default, otherwise user who last updated country.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

}

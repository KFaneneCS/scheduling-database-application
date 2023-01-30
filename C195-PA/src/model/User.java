package model;

import utility.TimeHelper;

import java.time.ZonedDateTime;

/**
 * Models Users who log in to application.
 *
 * @author Kyle Fanene
 */
public class User {

    /**
     * User's ID.
     */
    private final int id;

    /**
     * User's name.
     */
    private final String name;

    /**
     * User's password.
     */
    private final String password;

    /**
     * User's create date and time.
     */
    private final ZonedDateTime createDate;

    /**
     * User's create-by user.
     */
    private final String createdBy;

    /**
     * User's last update date and time.
     */
    private final ZonedDateTime lastUpdate;

    /**
     * User's last-updated-by user.
     */
    private final String lastUpdatedBy;

    /**
     * Constructor for created instance of User.
     *
     * @param id            ID for instantiated user.
     * @param name          Name for instantiated user.
     * @param password      Password for instantiated user.
     * @param createDate    Create date and time for the instantiated user.
     * @param createdBy     Name of user who instantiated this user.  Since users are not manipulable by this program,
     *                      former user is "script" unless applicable database is externally modified.
     * @param lastUpdate    Date and time instantiated user was last updated.
     * @param lastUpdatedBy Name of user who last updated this instantiated user.  Since users are not manipulable by
     *                      this program, former user is "script" unless applicable database is externally modified.
     */
    public User(int id, String name, String password, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.createDate = TimeHelper.stringToZDT(createDate);
        this.createdBy = createdBy;
        this.lastUpdate = TimeHelper.stringToZDT(lastUpdate);
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Getter for user ID.
     *
     * @return User ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for user name.
     *
     * @return User name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for user password.
     *
     * @return User password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for user ID created date and time.
     *
     * @return User ID create date and time.
     */
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Getter for another user ("script" unless externally modified") who created this user.
     *
     * @return Another user "script" by default, otherwise another user who created this user.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Getter for date and time user was last updated.
     *
     * @return Date and time user was last updated.
     */
    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Getter for another user ("script" unless externally modified") who last updated this user.
     *
     * @return Another user "script" by default, otherwise another user who last updated this user.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

}

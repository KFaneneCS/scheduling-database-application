package model;

import utility.TimeHelper;

import java.time.ZonedDateTime;

/**
 * Models appointments pertaining to customer, user, and contact.
 *
 * @author Kyle Fanene
 */
public class Appointment {

    /**
     * Appointment's ID.
     */
    private final int id;

    /**
     * Appointment's title.
     */
    private final String title;

    /**
     * Appointment's description.
     */
    private final String description;

    /**
     * Appointment's location.
     */
    private final String location;

    /**
     * Appointment's type.
     */
    private final String type;

    /**
     * Appointment's start date and time.
     */
    private final ZonedDateTime start;

    /**
     * Appointment's end date and time.
     */
    private final ZonedDateTime end;

    /**
     * Appointment's create date and time.
     */
    private final ZonedDateTime createDate;

    /**
     * Appointment's create-by user.
     */
    private final String createdBy;

    /**
     * Appointment's last update date and time.
     */
    private final ZonedDateTime lastUpdate;

    /**
     * Appointment's last-updated-by user.
     */
    private final String lastUpdatedBy;

    /**
     * Appointment's associated customer ID.
     */
    private final int customerId;

    /**
     * Appointment's associated user ID.
     */
    private final int userId;

    /**
     * Appointment's associated customer ID.
     */
    private final int contactId;

    /**
     * Constructor for created instance of Appointment.
     *
     * @param id            ID for the instantiated appointment.
     * @param title         Title for the instantiated appointment.
     * @param description   Description for the instantiated appointment.
     * @param location      Location for the instantiated appointment.
     * @param type          Type for the instantiated appointment.
     * @param start         Start date and time for the instantiated appointment.
     * @param end           End date and time for the instantiated appointment.
     * @param createDate    Create date and time for the instantiated appointment.
     * @param createdBy     Name of user who created instantiated appointment.
     * @param lastUpdate    Date and time instantiated appointment was last updated.
     * @param lastUpdatedBy Name of user who last updated instantiated appointment.
     * @param customerId    ID of associated customer of instantiated appointment.
     * @param userId        ID of associated user of instantiated appointment.
     * @param contactId     ID of associated contact of instantiated appointment.
     */
    public Appointment(int id, String title, String description, String location, String type, String start,
                       String end, String createDate, String createdBy, String lastUpdate,
                       String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = TimeHelper.stringToZDT(start);
        this.end = TimeHelper.stringToZDT(end);
        this.createDate = TimeHelper.stringToZDT(createDate);
        this.createdBy = createdBy;
        this.lastUpdate = TimeHelper.stringToZDT(lastUpdate);
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Getter for appointment ID.
     *
     * @return Appointment ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for appointment title.
     *
     * @return Appointment title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for appointment description.
     *
     * @return Appointment description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for appointment location.
     *
     * @return Appointment location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for appointment type.
     *
     * @return Appointment type.
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for appointment start date and time.
     *
     * @return Appointment start date and time.
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * Getter for appointment end date and time.
     *
     * @return Appointment end date and time.
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * Getter for appointment created date and time.
     *
     * @return Appointment created date and time.
     */
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Getter for user who created appointment.
     *
     * @return User who created appointment.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Getter for date and time appointment was last updated.
     *
     * @return Date and time appointment was last updated.
     */
    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Getter for user who last updated appointment.
     *
     * @return User who last updated appointment.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Getter for appointment's associated customer ID.
     *
     * @return Appointment's associated customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Getter for appointment's associated user ID.
     *
     * @return Appointment's associated user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Getter for appointment's associated contact ID.
     *
     * @return Appointment's associated contact ID.
     */
    public int getContactId() {
        return contactId;
    }

}

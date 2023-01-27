package model;

import utility.TimeHelper;

import java.time.ZonedDateTime;

public class Appointment {

    private final int id;
    private final String title;
    private final String description;
    private final String location;
    private final String type;
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final ZonedDateTime createDate;
    private final String createdBy;
    private final ZonedDateTime lastUpdate;
    private final String lastUpdatedBy;
    private final int customerId;
    private final int userId;
    private final int contactId;

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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }


    public String getType() {
        return type;
    }


    public ZonedDateTime getStart() {
        return start;
    }


    public ZonedDateTime getEnd() {
        return end;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }

    public int getContactId() {
        return contactId;
    }

}

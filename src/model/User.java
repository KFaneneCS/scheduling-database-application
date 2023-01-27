package model;

import utility.TimeHelper;

import java.time.ZonedDateTime;

public class User {

    private final int id;
    private final String name;
    private final String password;
    private final ZonedDateTime createDate;
    private final String createdBy;
    private final ZonedDateTime lastUpdate;
    private final String lastUpdatedBy;

    public User(int id, String name, String password, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.createDate = TimeHelper.stringToZDT(createDate);
        this.createdBy = createdBy;
        this.lastUpdate = TimeHelper.stringToZDT(lastUpdate);
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
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

}

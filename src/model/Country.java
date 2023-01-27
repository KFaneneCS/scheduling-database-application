package model;

public class Country {

    private final int id;
    private final String name;
    private final String createDate;
    private final String createdBy;
    private final String lastUpdate;
    private final String lastUpdatedBy;


    public Country(int id, String countryName, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = countryName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

}

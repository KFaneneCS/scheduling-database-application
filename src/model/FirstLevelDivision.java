package model;

public class FirstLevelDivision {

    private final int id;
    private final String division;
    private final String createDate;
    private final String createdBy;
    private final String lastUpdate;
    private final String lastUpdatedBy;
    private final int countryId;


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

    public int getId() {
        return id;
    }

    public String getDivision() {
        return division;
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

    public int getCountryId() {
        return countryId;
    }

}

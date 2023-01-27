package model;

import DAO.CountryAccess;
import DAO.FLDAccess;
import utility.TimeHelper;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Customer {

    private final int id;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String phone;
    private final ZonedDateTime createDate;
    private final String createdBy;
    private final ZonedDateTime lastUpdate;
    private final String lastUpdatedBy;
    private final int divisionId;
    private final String country;
    private final String firstLevelDiv;

    public Customer(int id, String name, String address, String postalCode, String phone, String createDate,
                    String createdBy, String lastUpdate, String lastUpdatedBy, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = TimeHelper.stringToZDT(createDate);
        this.createdBy = createdBy;
        this.lastUpdate = TimeHelper.stringToZDT(lastUpdate);
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        this.country = Objects.requireNonNull(CountryAccess.lookupCountry(FLDAccess.getCountryId(divisionId))).getName();
        this.firstLevelDiv = Objects.requireNonNull(FLDAccess.lookupFLD(divisionId)).getDivision();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
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

    public int getDivisionId() {
        return divisionId;
    }

    public String getCountry() {
        return country;
    }

    public String getFirstLevelDiv() {
        return firstLevelDiv;
    }

}

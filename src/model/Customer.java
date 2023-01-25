package model;

import DAO.CountryAccess;
import DAO.FLDAccess;
import utility.TimeHelper;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;
    private String country;
    private String firstLevelDiv;

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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = TimeHelper.stringToZDT(createDate);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = TimeHelper.stringToZDT(lastUpdate);
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(int divisionId) {
        this.country = Objects.requireNonNull(CountryAccess.lookupCountry(FLDAccess.getCountryId(divisionId))).getName();
    }

    public String getFirstLevelDiv() {
        return firstLevelDiv;
    }

    public void setFirstLevelDiv(int divisionId) {
        this.firstLevelDiv = Objects.requireNonNull(FLDAccess.lookupFLD(divisionId)).getDivision();
    }
}

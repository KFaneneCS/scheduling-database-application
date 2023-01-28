package model;

import utility.TimeHelper;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Models customers.
 *
 * @author Kyle Fanene
 */
public class Customer {

    /**
     * Customer's ID.
     */
    private final int id;

    /**
     * Customer's name.
     */
    private final String name;

    /**
     * Customer's address.
     */
    private final String address;

    /**
     * Customer's postal code.
     */
    private final String postalCode;

    /**
     * Customer's phone number as a String.
     */
    private final String phone;

    /**
     * Customer's create date and time.
     */
    private final ZonedDateTime createDate;

    /**
     * Customer's create-by user.
     */
    private final String createdBy;

    /**
     * Customer's last update date and time.
     */
    private final ZonedDateTime lastUpdate;

    /**
     * Customer's last-updated-by user.
     */
    private final String lastUpdatedBy;

    /**
     * Customer's first-level division ID.
     */
    private final int divisionId;

    /**
     * Customer's country of origin per first-level division ID.
     */
    private final String country;

    /**
     * Customer's state or province per first-level division ID.
     */
    private final String firstLevelDiv;

    /**
     * Constructor for created instance of Customer.
     *
     * @param id            ID for the instantiated customer.
     * @param name          Name for the instantiated customer.
     * @param address       Address for the instantiated customer.
     * @param postalCode    Postal code for the instantiated customer.
     * @param phone         Phone number as a String for the instantiated customer.
     * @param createDate    Create date and time for the instantiated customer.
     * @param createdBy     Name of user who created instantiated customer.
     * @param lastUpdate    Date and time instantiated customer was last updated.
     * @param lastUpdatedBy Name of user who last updated instantiated customer.
     * @param divisionId    First-Level Division ID of instantiated customer.
     */
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

    /**
     * Getter for customer ID.
     *
     * @return Customer ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for customer name.
     *
     * @return Customer name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for customer address.
     *
     * @return Customer address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for customer postal code.
     *
     * @return Customer postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Getter for customer phone number as a String.
     *
     * @return Customer phone number as a String.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter for customer created date and time.
     *
     * @return Customer created date and time.
     */
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Getter for user who created customer.
     *
     * @return User who created customer.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Getter for date and time customer was last updated.
     *
     * @return Date and time customer was last updated.
     */
    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Getter for user who last updated customer.
     *
     * @return User who last updated customer.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Getter for customer first-level division ID.
     *
     * @return Customer first-level division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Getter for customer's country as determined by customer's first-level division ID.
     *
     * @return Customer country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Getter for customer's state/province as determined by customer's first-level division ID.
     *
     * @return Customer state/province.
     */
    public String getFirstLevelDiv() {
        return firstLevelDiv;
    }

}

package model;

/**
 * Models contacts associated with created appointments.
 *
 * @author Kyle Fanene
 */
public class Contact {

    /**
     * Contact's ID.
     */
    private final int id;

    /**
     * Contact's name.
     */
    private final String name;

    /**
     * Contact's email.
     */
    private final String email;

    /**
     * Constructor for created instance of Contact.
     *
     * @param contactId     ID for the instantiated contact.
     * @param contactName   Name for the instantiated contact.
     * @param email         Email for the instantiated contact.
     */
    public Contact(int contactId, String contactName, String email) {
        this.id = contactId;
        this.name = contactName;
        this.email = email;
    }

    /**
     * Getter for contact ID.
     *
     * @return Contact ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for contact name.
     *
     * @return Contact name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for contact email.
     *
     * @return Contact email.
     */
    public String getEmail() {
        return email;
    }

}

package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Class which provides various means of access to Contact class, such as retrieving contact
 * object lists, adding contact objects (at initialization), and looking up contacts.
 *
 * @author Kyle Fanene
 */
public class ContactAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Observable list of all contact objects.
     */
    public static final ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    /**
     * Method that returns allContacts list.
     *
     * @return Returns allContacts ObservableList.
     */
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    /**
     * Method that initializes Contact objects via contacts table in database.
     */
    public static void initializeContacts() {

        try {
            ResultSet rs = ContactQueries.selectAllContacts();

            while (rs.next()) {
                Contact contact = getContactObjFromDB(rs);
                addContact(contact);
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Method that adds Contact object to allContacts list
     *
     * @param contact       Contact object to be added.
     */
    public static void addContact(Contact contact) {
        allContacts.add(contact);
    }

    /**
     * Method that takes contact information from database and returns a new Contact object
     * based on said data.
     *
     * @param rs        ResultSet used to query database for contact data to use.
     *
     * @return Returns newly created Contact object.
     */
    public static Contact getContactObjFromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("Contact_ID");
        String name = rs.getString("Contact_Name");
        String email = rs.getString("Email");

        return new Contact(id, name, email);
    }

    /**
     * Method that searches allContacts list using contact ID and returns
     * Contact object.
     *
     * @param contactId       Contact ID to search list against.
     *
     * @return Returns Contact object; returns null if not found.
     */
    public static Contact lookupContact(int contactId) {

        for (Contact contact : getAllContacts()) {

            if (contact.getId() == contactId) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Method that searches allContacts list via contact name String and returns
     * Contact ID.
     *
     * @param contactName       String contact name to search list against.
     *
     * @return Returns Contact ID integer; returns -1 if not found.
     */
    public static int lookupContactId(String contactName) {

        for (Contact contact : getAllContacts()) {
            if (Objects.equals(contact.getName(), contactName)) {
                return contact.getId();
            }
        }
        return -1;
    }
}

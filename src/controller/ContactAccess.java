package controller;

import DAO.ContactQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";
    public static final ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

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

    public static void addContact(Contact contact) {
        allContacts.add(contact);
    }

    public static Contact getContactObjFromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("Contact_ID");
        String name = rs.getString("Contact_Name");
        String email = rs.getString("Email");

        return new Contact(id, name, email);
    }

    public static Contact lookupContactId(int contactId) {

        for (Contact contact : getAllContacts()) {

            if (contact.getId() == contactId) {
                return contact;
            }
        }
        return null;
    }

    public static int lookupContactId(String contactName) {

        for (Contact contact : getAllContacts()) {
            if (contact.getName() == contactName) {
                return contact.getId();
            }
        }
        return -1;
    }
}

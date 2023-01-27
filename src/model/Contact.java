package model;

public class Contact {

    private final int id;
    private final String name;
    private final String email;

    public Contact(int contactId, String contactName, String email) {
        this.id = contactId;
        this.name = contactName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}

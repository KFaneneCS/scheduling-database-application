package model;

public class Contact {

    private int id;
    private String name;
    private String email;

    public Contact(int contactId, String contactName, String email) {
        this.id = contactId;
        this.name = contactName;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

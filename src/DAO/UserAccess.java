package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";
    public static final ObservableList<User> allUsers = FXCollections.observableArrayList();

    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }

    public static void initializeUsers() {

        try {
            ResultSet rs = UserQueries.selectAllUsers();

            while (rs.next()) {
                User user = getUserObjFromDB(rs);
                addUser(user);
            }
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(ERROR_MESSAGE);
        }
    }

    public static void addUser(User user) {
        allUsers.add(user);
    }

    public static User lookupUser(int userId) {

        for (User user : getAllUsers()) {

            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    public static int lookupUserId(String userName) {

        for (User user : getAllUsers()) {

            if (Objects.equals(user.getName(), userName)) {
                return user.getId();
            }
        }
        return -1;
    }

    public static User getUserObjFromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("User_ID");
        String name = rs.getString("User_Name");
        String password = rs.getString("Password");
        String createDate = rs.getString("Create_Date");
        String createdBy = rs.getString("Created_By");
        String lastUpdate = rs.getString("Last_Update");
        String lastUpdatedBy = rs.getString("Last_Updated_By");

        return new User(id, name, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
    }
}

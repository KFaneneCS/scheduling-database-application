package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utility.AlertPopups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Class which provides various means of access to User class, such as retrieving User
 * object lists, adding User objects (at initialization), and looking up users.
 *
 * @author Kyle Fanene
 */
public class UserAccess {

    private static final String ERROR_MESSAGE = "Sorry, there was an error.";

    /**
     * Observable list of all User objects.
     */
    private static final ObservableList<User> allUsers = FXCollections.observableArrayList();

    /**
     * The active User for the session (as a User object).
     */
    private static User activeUser;

    /**
     * Method that returns allUsers list.
     *
     * @return Returns allUsers ObservableList.
     */
    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }

    /**
     * Method that returns active user.
     *
     * @return Returns activeUser User object.
     */
    public static String getActiveUser() {
        return activeUser.getName();
    }

    /**
     * Method that sets active user.
     *
     * @param user      Current user as a User object to set activeUser.
     */
    public static void setActiveUser(User user) {
        activeUser = user;
    }

    /**
     * Method that initializes User objects via users table in database.
     */
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

    /**
     * Method that adds User object to allUsers list
     *
     * @param user       User object to be added.
     */
    public static void addUser(User user) {
        allUsers.add(user);
    }

    /**
     * Method that searches allUsers list using user ID and returns
     * User object.
     *
     * @param userId       User ID to search list against.
     *
     * @return Returns User object; returns null if not found.
     */
    public static User lookupUser(int userId) {

        for (User user : getAllUsers()) {

            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    /**
     * Method that searches allUsers list using username String and returns
     * User ID.
     *
     * @param username       String username to search list against.
     *
     * @return Returns Country ID integer; returns -1 if not found.
     */
    public static int lookupUserId(String username) {

        for (User user : getAllUsers()) {

            if (Objects.equals(user.getName(), username)) {
                return user.getId();
            }
        }
        return -1;
    }

    /**
     * Method that takes user information from database and returns a new User object
     * based on said data.
     *
     * @param rs        ResultSet used to query database for user data to use.
     *
     * @return Returns newly created User object.
     */
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

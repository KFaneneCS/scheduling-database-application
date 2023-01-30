package controller;

import DAO.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import utility.AlertPopups;
import utility.SceneChanger;
import utility.TimeHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class which provides control logic for the various components of the Appointments screen, including: adding,
 * updating, selecting, and deleting appointments; viewing all, current month, and current week appointments in a table
 * that directly corresponds to the applicable appointments table from the database (except that date/times are
 * displayed in user's local time); filtering said table by ID or Title; and viewing three relevant reports in a
 * separate form.  Appointments are added to, updated to, and deleted from both the database and their corresponding
 * Appointment objects.
 *
 * @author Kyle Fanene
 */
public class AppointmentController implements Initializable {

    private static final String GENERAL_ERROR_MESSAGE = "Sorry, there was an error.";
    private static final String EMPTY_ERROR_MESSAGE = "Sorry, all fields must be filled in.";
    private static final String NO_RESULTS_ERROR_MESSAGE = "Sorry, could not find any results.";
    private static final String INVALID_TIMES_MESSAGE = "Sorry, appointment cannot end before start time.";
    private static final String SELECTION_ERROR_MESSAGE = "Sorry, no appointment was selected.";
    private static final String OVERLAP_ERROR_MESSAGE =
            "Sorry, the proposed timeframe overlaps with an existing appointment.";
    private static final String CHANGE_CONFIRMATION = "Confirm changes?";
    private static final String DELETE_CONFIRMATION = "Are you sure you want to delete this appointment?";
    private static final String ADD_SUCCESS_MESSAGE = "Appointment successfully added.";
    // Disables dates before current date for datepicker
    SceneChanger sceneChanger = new SceneChanger();
    boolean startAddListenerTriggered = false;
    boolean endAddListenerTriggered = false;
    boolean startDateChosen = false;
    boolean dateMatchListenerPaused = false;
    boolean appointmentSelected = false;
    @FXML
    private Button addButton;
    @FXML
    private TableView<Appointment> allAppointmentsTableView;
    @FXML
    private Tab allTab;
    @FXML
    private TextField appointmentFilterTextField;
    @FXML
    private Label appointmentsLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button clearButton;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private Label contactLabel;
    @FXML
    private Tab currMonthTab;
    @FXML
    private TableView<Appointment> currMonthTableView;
    @FXML
    private Tab currWeekTab;
    @FXML
    private TableView<Appointment> currWeekTableView;
    @FXML
    private ComboBox<String> customerNameComboBox;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private Label endDateLabel;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<Integer> endHourComboBox;
    @FXML
    private Label endHourLabel;
    @FXML
    private Label endLabel;
    @FXML
    private ComboBox<String> endMinuteComboBox;
    @FXML
    private Label endMinuteLabel;
    @FXML
    private Label idLabel;
    @FXML
    private TextField idTextField;
    @FXML
    private Label locationLabel;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private Button selectButton;
    @FXML
    private Label startDateLabel;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<Integer> startHourComboBox;
    @FXML
    private Label startHourLabel;
    @FXML
    private Label startLabel;
    @FXML
    private ComboBox<String> startMinuteComboBox;
    @FXML
    private Label startMinuteLabel;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private TextField typeTextField;
    @FXML
    private Button updateButton;
    @FXML
    private ComboBox<String> userNameComboBox;
    @FXML
    private Label userNameLabel;
    @FXML
    private Button viewReportsButton;

    /**
     * Adds appointment object and inserts information into database per user's input.
     * <p>
     * Logical validations are included to prevent empty fields or non-sequential time inputs.
     *
     * @param event Button to add appointment.  Disabled when an appointment is selected for update or deletion.
     */
    @FXML
    void onActionAddAppointment(ActionEvent event) {

        try {
            String title = titleTextField.getText().strip();
            String description = descriptionTextField.getText().strip();
            String location = locationTextField.getText().strip();
            String contact = contactComboBox.getValue();
            String type = typeTextField.getText().strip();
            LocalDate startLocalDate = startDatePicker.getValue();
            int startHour = startHourComboBox.getValue();
            String startMinute = startMinuteComboBox.getValue();
            LocalDate endLocalDate = endDatePicker.getValue();
            int endHour = endHourComboBox.getValue();
            String endMinute = endMinuteComboBox.getValue();
            String createdBy = UserAccess.getActiveUser();
            String lastUpdatedBy = UserAccess.getActiveUser();
            String customer = customerNameComboBox.getValue();
            String user = userNameComboBox.getValue();

            ZonedDateTime startZDT = TimeHelper.userInputToZDT(startLocalDate, startHour, startMinute);
            ZonedDateTime endZDT = TimeHelper.userInputToZDT(endLocalDate, endHour, endMinute);

            if (startZDT.isAfter(endZDT)) {
                AlertPopups.generateErrorMessage(INVALID_TIMES_MESSAGE);
                return;
            }

            if (title.isEmpty() || description.isEmpty() || location.isEmpty() ||
                    type.isEmpty()) {
                AlertPopups.generateErrorMessage(EMPTY_ERROR_MESSAGE);
                return;
            }

            int contactId = ContactAccess.lookupContactId(contact);
            int customerId = CustomerAccess.lookupCustomerId(customer);
            int userId = UserAccess.lookupUserId(user);

            if (TimeHelper.addHasOverlap(customerId, startZDT, endZDT)) {
                AlertPopups.generateErrorMessage(OVERLAP_ERROR_MESSAGE);
                return;
            }

            AppointmentAccess.executeAdd(title, description, location, contactId,
                    type, startZDT, endZDT, createdBy, lastUpdatedBy, customerId, userId);
            fillAllAppointmentTables();
            AlertPopups.generateInfoMessage("Add successful", ADD_SUCCESS_MESSAGE);
            clearFields();


        } catch (NullPointerException npe) {
            AlertPopups.generateErrorMessage(EMPTY_ERROR_MESSAGE);
            npe.printStackTrace();
        } catch (SQLException sqle) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            sqle.printStackTrace();
        }

    }

    /**
     * Clears text fields and resets all combo boxes.
     * <p>
     * Sets appointmentSelected boolean to false.
     *
     * @param event Button to clear fields/combo boxes.
     */
    @FXML
    void onActionClearTextFields(ActionEvent event) throws SQLException {

        clearFields();
        appointmentSelected = false;

    }

    /**
     * Removes selected appointment object and deletes information from database.
     * <p>
     * Logical validation checks that an appointment was selected.
     *
     * @param event Button to delete appointment.
     */
    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws SQLException {

        if (!appointmentSelected) {
            AlertPopups.generateErrorMessage(SELECTION_ERROR_MESSAGE);
            return;
        }

        int appointmentId = Integer.parseInt(idTextField.getText());

        if (AlertPopups.receiveConfirmation("Delete", DELETE_CONFIRMATION)) {
            AppointmentAccess.executeDelete(appointmentId);
            fillAllAppointmentTables();
            clearFields();
        }

    }

    /**
     * Changes screen to home (Welcome).
     *
     * @param event Back button, which returns to home screen.
     */
    @FXML
    void onActionDisplayWelcome(ActionEvent event) throws IOException {
        sceneChanger.changeScreen(event, "Welcome");
    }

    /**
     * Filters selected appointment table by Appointment ID or Title.
     *
     * @param event Filter text field when user presses Enter.
     */
    @FXML
    void onActionFilterAppointments(ActionEvent event) throws SQLException {
        String searchText = appointmentFilterTextField.getText();
        filterTable(searchText);
    }

    /**
     * Populates text fields, DatePickers, and combo boxes with selected appointment's information.
     * <p>
     * Logical validation checks that an appointment was selected.
     *
     * @param event Button to select appointment.
     */
    @FXML
    void onActionSelectAppointment(ActionEvent event) {

        try {

            TableView tableView;
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

            if (allTab.equals(selectedTab)) {
                tableView = allAppointmentsTableView;
            } else if (currMonthTab.equals(selectedTab)) {
                tableView = currMonthTableView;
            } else if (currWeekTab.equals(selectedTab)) {
                tableView = currWeekTableView;
            } else {
                tableView = null;
            }

            ObservableList<Appointment> appointmentList =
                    (ObservableList<Appointment>) tableView.getSelectionModel().getSelectedItem();
            int apptIdForSearch = Integer.parseInt(String.valueOf(appointmentList.get(0)));
            Appointment appointment = AppointmentAccess.lookupAppointment(apptIdForSearch);
            String startMin;
            String endMin;

            if (appointment.getStart().getMinute() == 0) {
                startMin = "00";
            } else {
                startMin = String.valueOf(appointment.getStart().getMinute());
            }

            if (appointment.getEnd().getMinute() == 0) {
                endMin = "00";
            } else {
                endMin = String.valueOf(appointment.getEnd().getMinute());
            }

            idTextField.setText(String.valueOf(appointment.getId()));
            titleTextField.setText(appointment.getTitle());
            descriptionTextField.setText(appointment.getDescription());
            locationTextField.setText(appointment.getLocation());
            contactComboBox.setValue(ContactAccess.lookupContact(appointment.getContactId()).getName());
            typeTextField.setText(appointment.getType());
            startDatePicker.setValue(appointment.getStart().toLocalDate());
            startHourComboBox.setValue(appointment.getStart().getHour());
            startMinuteComboBox.setValue(startMin);
            endDatePicker.setValue(appointment.getEnd().toLocalDate());
            endHourComboBox.setValue(appointment.getEnd().getHour());
            endMinuteComboBox.setValue(endMin);
            customerNameComboBox.setValue(CustomerAccess.lookupCustomer(appointment.getCustomerId()).getName());
            userNameComboBox.setValue(UserAccess.lookupUser(appointment.getUserId()).getName());


            addButton.setDisable(true);

            appointmentSelected = true;

        } catch (NullPointerException npe) {
            AlertPopups.generateErrorMessage(SELECTION_ERROR_MESSAGE);
        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
        }
    }

    /**
     * Updates appointment object and applicable information in database for selected appointment.
     * <p>
     * Logical validations check that an appointment was selected, fields are not left empty, and
     * times are sequential.
     *
     * @param event Button to update appointment.
     */
    @FXML
    void onActionUpdateAppointment(ActionEvent event) throws SQLException {

        if (!appointmentSelected) {
            AlertPopups.generateErrorMessage(SELECTION_ERROR_MESSAGE);
        } else {

            int id = Integer.parseInt(idTextField.getText());
            String title = titleTextField.getText().strip();
            String description = descriptionTextField.getText().strip();
            String location = locationTextField.getText().strip();
            String contact = contactComboBox.getValue();
            String type = typeTextField.getText();
            LocalDate startLocalDate = startDatePicker.getValue();
            int startHour = startHourComboBox.getValue();
            String startMinute = startMinuteComboBox.getValue();
            LocalDate endLocalDate = endDatePicker.getValue();
            int endHour = endHourComboBox.getValue();
            String endMinute = endMinuteComboBox.getValue();
            String lastUpdatedBy = UserAccess.getActiveUser();
            String customer = customerNameComboBox.getValue();
            String user = userNameComboBox.getValue();

            ZonedDateTime startZDT = TimeHelper.userInputToZDT(startLocalDate, startHour, startMinute);
            ZonedDateTime endZDT = TimeHelper.userInputToZDT(endLocalDate, endHour, endMinute);

            if (startZDT.isAfter(endZDT)) {
                AlertPopups.generateErrorMessage(INVALID_TIMES_MESSAGE);
                return;
            }

            if (title.isEmpty() || description.isEmpty() || location.isEmpty() ||
                    type.isEmpty()) {
                AlertPopups.generateErrorMessage(EMPTY_ERROR_MESSAGE);
                return;
            }

            int customerId = CustomerAccess.lookupCustomerId(customer);
            int userId = UserAccess.lookupUserId(user);
            int contactId = ContactAccess.lookupContactId(contact);

            if (TimeHelper.updateHasOverlap(customerId, id, startZDT, endZDT)) {
                AlertPopups.generateErrorMessage(OVERLAP_ERROR_MESSAGE);
                return;
            }

            if (AlertPopups.receiveConfirmation("Confirm Update", CHANGE_CONFIRMATION)) {
                if (AppointmentQueries.updateAppointment(id, title, description, location, type, startZDT, endZDT,
                        lastUpdatedBy, customerId, userId, contactId) == 1) {
                    fillAllAppointmentTables();
                    ResultSet rs = AppointmentQueries.selectAppointmentByTable(id, 1);
                    while (rs.next()) {
                        Appointment updatedAppointment = AppointmentAccess.getApptObjFromDB(rs);
                        AppointmentAccess.updateAppointment(id, updatedAppointment);
                    }
                } else {
                    AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
                }
            }


        }
    }

    /**
     * Changes screen to Reports page.
     *
     * @param event View Reports button pressed.
     */
    @FXML
    void onActionViewReports(ActionEvent event) throws IOException {
        sceneChanger.changeScreen(event, "Reports");
    }

    /**
     * dayCellFactory allows for the desired formatting of the Start DatePicker such that dates prior to the current
     * date are disabled.
     */
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (MonthDay.from(item).isBefore(MonthDay.now())) {
                        setDisable(true);
                    }
                }
            };
        }
    };

    /**
     * Method that sets up selected TableView with columns and rows corresponding
     * to the applicable database table per the passed ResultSet.  "populated" boolean ensures
     * columns are not populated multiple times.  Calls refreshTable() method to fill rows.
     *
     * @param table The desired table to set up/populate from the three available
     *              ("All", "Current Month", "Current Week").
     * @param rs    The ResultSet from a database select query that pulls the relevant
     *              database table information.
     */
    public void fillAppointmentTable(TableView table, ResultSet rs) {

        // checks whether table columns were already populated
        boolean populated = table.getColumns().size() > 0;

        table.getItems().clear();

        try {

            if (!populated) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn column = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            try {
                                return new SimpleStringProperty(param.getValue().get(j).toString());
                            } catch (NullPointerException npe) {
                                return new SimpleStringProperty("null");
                            }
                        }
                    });
                    table.getColumns().addAll(column);
                }
            }

            refreshTable(table, rs);

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Overloaded method that takes rows of data from database per passed ResultSet and populates the
     * desired table accordingly.  All DateTimes are converted from UTC to user's local time
     * zone.
     *
     * @param table The desired table to set up/populate from the three available
     *              ("All", "Current Month", "Current Week").
     * @param rs    The ResultSet from a database select query that pulls the relevant
     *              database table information.
     */
    public void refreshTable(TableView table, ResultSet rs) throws SQLException {


        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        table.getItems().clear();

        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                if ((Objects.equals(rs.getMetaData().getColumnName(i), "Start")) ||
                        (Objects.equals(rs.getMetaData().getColumnName(i), "End")) ||
                        (Objects.equals(rs.getMetaData().getColumnName(i), "Create_Date")) ||
                        (Objects.equals(rs.getMetaData().getColumnName(i), "Last_Update"))) {
                    row.add(TimeHelper.dbStringToLocalString(rs.getString(i)));
                } else {
                    row.add(rs.getString(i));
                }
            }
            data.add(row);
        }

        table.setItems(data);
    }

    /**
     * Overloaded method that takes ObservableList of ResultSets stemming from multiple
     * SELECT-FROM-WHERE queries and filters the desired table accordingly.  All
     * DateTimes are converted from UTC to user's local time zone.
     *
     * @param table     The desired table to set up/populate from the three available
     *                  ("All", "Current Month", "Current Week").
     * @param rsList    The ResultSet ObservableList from database select queries that pull the relevant
     *                  database table information to filter the applicable table.
     */
    public void refreshTable(TableView table, ObservableList<ResultSet> rsList) throws SQLException {

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        table.getItems().clear();

        for (ResultSet rs : rsList) {
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if ((Objects.equals(rs.getMetaData().getColumnName(i), "Start")) ||
                            (Objects.equals(rs.getMetaData().getColumnName(i), "End")) ||
                            (Objects.equals(rs.getMetaData().getColumnName(i), "Create_Date")) ||
                            (Objects.equals(rs.getMetaData().getColumnName(i), "Last_Update"))) {
                        row.add(TimeHelper.dbStringToLocalString(rs.getString(i)));
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                data.add(row);
            }
        }
        table.setItems(data);

    }

    /**
     * Populates Contact, Customer, and User combo boxes using applicable ObservableLists.
     * Also sets up DatePicker and Hour and Minute combo boxes to default values.
     */
    public void fillComboAndDates() {

        ObservableList<String> contactList = FXCollections.observableArrayList();
        ObservableList<String> customerList = FXCollections.observableArrayList();
        ObservableList<String> userList = FXCollections.observableArrayList();

        // Contact combo box
        for (Contact contact : ContactAccess.getAllContacts()) {
            contactList.add(contact.getName());
        }
        contactComboBox.setItems(contactList);
        contactComboBox.setValue(contactList.get(0));

        // Customer combo box
        for (Customer customer : CustomerAccess.getAllCustomers()) {
            customerList.add(customer.getName());
        }
        customerNameComboBox.setItems(customerList);
        customerNameComboBox.setValue(customerList.get(0));


        // User combo box
        for (User user : UserAccess.getAllUsers()) {
            userList.add(user.getName());
        }
        userNameComboBox.setItems(userList);
        userNameComboBox.setValue(userList.get(0));

        // Dates, hours, and minutes
        startDatePicker.setDayCellFactory(dayCellFactory);

        startHourComboBox.setItems(TimeHelper.getHoursList());
        startHourComboBox.setValue(TimeHelper.getHoursList().get(0));
        startMinuteComboBox.setItems(TimeHelper.getMinutesList());
        startMinuteComboBox.setValue(TimeHelper.getMinutesList().get(0));

        endHourComboBox.setItems(TimeHelper.getHoursList());
        endHourComboBox.setValue(TimeHelper.getHoursList().get(0));
        endMinuteComboBox.setItems(TimeHelper.getMinutesList());
        endMinuteComboBox.setValue(TimeHelper.getMinutesList().get(0));

    }

    /**
     * Filters displayed appointment table by String provided in the "Search by" text field.
     * Method will first check if user provided an integer, in which case it will search by
     * appointment ID.  Otherwise, it will search by appointment title. If no results are
     * found, an error alert will pop up.
     *
     * @param text  The text input from user.
     */
    public void filterTable(String text) throws SQLException {

        TableView tableView = null;
        int tableNum = 0;
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (allTab.equals(selectedTab)) {
            tableView = allAppointmentsTableView;
            tableNum = 1;
        } else if (currMonthTab.equals(selectedTab)) {
            tableView = currMonthTableView;
            tableNum = 2;
        } else if (currWeekTab.equals(selectedTab)) {
            tableView = currWeekTableView;
            tableNum = 3;
        } else {
        }
        ObservableList<ResultSet> rsList = FXCollections.observableArrayList();
        try {

            if (text.isBlank()) {
                fillAllAppointmentTables();
                return;
            }
            int id = Integer.parseInt(text.strip());
            Appointment appointment = AppointmentAccess.lookupAppointment(id);
            if (appointment == null) {
                throw new NullPointerException();
            } else {
                ResultSet rs = AppointmentQueries.selectAppointmentByTable(appointment.getId(), tableNum);
                refreshTable(tableView, rs);
            }

        } catch (NumberFormatException numberFormatException) {
            ObservableList<Appointment> appointmentList = AppointmentAccess.lookupAppointments(text.strip());
            if (appointmentList.size() < 1) {
                fillAllAppointmentTables();
                AlertPopups.generateErrorMessage(NO_RESULTS_ERROR_MESSAGE);
            } else {
                for (Appointment appointment : appointmentList) {
                    ResultSet rs = AppointmentQueries.selectAppointmentByTable(appointment.getTitle(),
                            appointment.getDescription(), appointment.getStart());
                    rsList.add(rs);
                }
                refreshTable(tableView, rsList);
            }

        } catch (NullPointerException nullPointerException) {
            AlertPopups.generateErrorMessage(NO_RESULTS_ERROR_MESSAGE);
        }
    }

    /**
     * Method that fetches the necessary ResultSets and calls fillAppointmentTable()
     * methods for all three appointment TableViews ("All", "Current Month", "Current Week")
     * to reduce some redundancy.
     */
    public void fillAllAppointmentTables() throws SQLException {

        ResultSet rsAll = AppointmentQueries.selectAllAppointments();
        ResultSet rsMonth = AppointmentQueries.selectCurrMonthAppointments();
        ResultSet rsWeek = AppointmentQueries.selectCurrWeekAppointments();

        fillAppointmentTable(allAppointmentsTableView, rsAll);
        fillAppointmentTable(currMonthTableView, rsMonth);
        fillAppointmentTable(currWeekTableView, rsWeek);
    }

    /**
     * Clears all Appointment text fields, sets combo boxes and DatePickers to default,
     * and enables the Add button.
     */
    public void clearFields() throws SQLException {
        idTextField.setText("auto-generated");
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        typeTextField.clear();
        contactComboBox.setItems(null);
        customerNameComboBox.setItems(null);
        userNameComboBox.setItems(null);
        fillComboAndDates();

        addButton.setDisable(false);
    }

    /**
     * Initializes the Appointment controller class. Populates default data for combo boxes, dates, and tables.
     * <p>
     * Six lambda expressions are used, each being a listener.  Collectively, they manage the various date and
     * time constraints of the business requirements, as well as to maintain a logical experience for the user.
     * These constraints include ensuring that the DatePicker displays the proper date if user's timezone
     * resulted in spilling over into midnight the following day per our business's 8am-10pm EST opening hours,
     * and ensuring no minute beyond :00 is available for the final hour (10pm EST).
     * <p>
     * Each lambda expression allows for conciseness and is the most efficient way to handle listeners.
     *
     * @param url            Per Initializable javadoc reference: "The location used to resolve relative
     *                       paths for the root object, or null if the location is not known."
     * @param resourceBundle Per Initializable javadoc reference: "The resources used to
     *                       localize the root object, or null if the root object was not localized."
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            fillComboAndDates();
            fillAllAppointmentTables();

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Listener that sets end date to equal start date once start date is chosen initially
        startDatePicker.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!dateMatchListenerPaused) {
                endDatePicker.setValue(startDatePicker.getValue());
                startHourComboBox.setValue(startHourComboBox.getItems().get(0));
                endHourComboBox.setValue(endHourComboBox.getItems().get(0));
                startDateChosen = true;
            }
        });

        // Listener that enables choosing start and end hour and minute once start date is chosen
        startDatePicker.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (startDateChosen) {
                startHourComboBox.setDisable(false);
                startMinuteComboBox.setDisable(false);
                endHourComboBox.setDisable(false);
                endMinuteComboBox.setDisable(false);
            }
        });

        // Listener that ensures user cannot choose start minute beyond 10:00pm Eastern
        startHourComboBox.valueProperty().addListener((observableValue, oldValue, newvalue) -> {
            if (startHourComboBox.getValue().equals(startHourComboBox.getItems().get(startHourComboBox.getItems().size() - 1))) {
                startMinuteComboBox.setValue(startMinuteComboBox.getItems().get(0));
                startMinuteComboBox.setDisable(true);
            } else {
                startMinuteComboBox.setDisable(false);
            }
        });

        // Listener that ensures user cannot choose end minute beyond 10:00pm Eastern
        endHourComboBox.valueProperty().addListener((observableValue, oldValue, newvalue) -> {
            if (endHourComboBox.getValue().equals(endHourComboBox.getItems().get(endHourComboBox.getItems().size() - 1))) {
                endMinuteComboBox.setValue(endMinuteComboBox.getItems().get(0));
                endMinuteComboBox.setDisable(true);
            } else {
                endMinuteComboBox.setDisable(false);
            }
        });

        // Listener that accounts for potential spillover to following day depending on user's location
        startHourComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if ((TimeHelper.hasDateSpillover()) && (startHourComboBox.getValue() < startHourComboBox.getItems().get(0)) && !startAddListenerTriggered) {
                dateMatchListenerPaused = true;
                startDatePicker.setValue(startDatePicker.getValue().plusDays(1));
                startAddListenerTriggered = true;
                dateMatchListenerPaused = false;
            }
            if ((TimeHelper.hasDateSpillover()) && (startHourComboBox.getValue() >= startHourComboBox.getItems().get(0)) && startAddListenerTriggered) {
                dateMatchListenerPaused = true;
                startDatePicker.setValue(startDatePicker.getValue().minusDays(1));
                startAddListenerTriggered = false;
                dateMatchListenerPaused = false;
            }
        });

        // Second listener that accounts for potential spillover to following day depending on user's location
        endHourComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if ((TimeHelper.hasDateSpillover()) && (endHourComboBox.getValue() < endHourComboBox.getItems().get(0)) &&
                    (!endAddListenerTriggered) && (startDateChosen)) {
                endDatePicker.setValue(endDatePicker.getValue().plusDays(1));
                endAddListenerTriggered = true;
            }
            if ((TimeHelper.hasDateSpillover()) && (endHourComboBox.getValue() >= endHourComboBox.getItems().get(0)) && (endAddListenerTriggered)) {
                endDatePicker.setValue(endDatePicker.getValue().minusDays(1));
                endAddListenerTriggered = false;
            }
        });

    }
}

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
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    SceneChanger sceneChanger = new SceneChanger();
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
    boolean addListenerTriggered = false;
    boolean startDateChosen = false;
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
                    type, startZDT, endZDT, customerId, userId);
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

    @FXML
    void onActionClearTextFields(ActionEvent event) throws SQLException {

        clearFields();
        addButton.setDisable(false);
        appointmentSelected = false;

    }

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
        }

    }

    @FXML
    void onActionDisplayWelcome(ActionEvent event) throws IOException {
        sceneChanger.changeScreen(event, "Welcome");
    }

    @FXML
    void onActionFilterAppointments(ActionEvent event) throws SQLException {
        String searchText = appointmentFilterTextField.getText();
        filterTable(searchText);
    }

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
            contactComboBox.setValue(ContactAccess.lookupContactId(appointment.getContactId()).getName());
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
                if (AppointmentQueries.updateAppointment(id, title, description, location, type, startZDT, endZDT, customerId, userId, contactId) == 1) {
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

    @FXML
    void onActionViewReports(ActionEvent event) throws IOException {
        sceneChanger.changeScreen(event, "Reports");
    }


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

    public void refreshTable(TableView table, ResultSet rs) throws SQLException {


        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        table.getItems().clear();

        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }

        table.setItems(data);
    }

    public void refreshTable(TableView table, ObservableList<ResultSet> rsList) throws SQLException {

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        table.getItems().clear();

        for (ResultSet rs : rsList) {
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
        }
        table.setItems(data);

    }

    public void fillComboAndDates() throws SQLException {

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
//        startDatePicker.setValue(LocalDate.now());
//        endDatePicker.setValue(LocalDate.now());

        startHourComboBox.setItems(TimeHelper.getHoursList());
        startHourComboBox.setValue(TimeHelper.getHoursList().get(0));
        startMinuteComboBox.setItems(TimeHelper.getMinutesList());
        startMinuteComboBox.setValue(TimeHelper.getMinutesList().get(0));

        endHourComboBox.setItems(TimeHelper.getHoursList());
        endHourComboBox.setValue(TimeHelper.getHoursList().get(0));
        endMinuteComboBox.setItems(TimeHelper.getMinutesList());
        endMinuteComboBox.setValue(TimeHelper.getMinutesList().get(0));

    }

    // Disables dates before current date for datepicker
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (MonthDay.from(item).isBefore(MonthDay.now())) {
                        setDisable(true);
                    }
                }
            };
        }
    };

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
                    ResultSet rs = AppointmentQueries.selectAppointmentByTable(appointment.getTitle(), appointment.getDescription(), appointment.getStart());
                    rsList.add(rs);
                }
                refreshTable(tableView, rsList);
            }

        } catch (NullPointerException nullPointerException) {
            AlertPopups.generateErrorMessage(NO_RESULTS_ERROR_MESSAGE);
        }
    }

    public void fillAllAppointmentTables() throws SQLException {

        ResultSet rsAll = AppointmentQueries.selectAllAppointments();
        ResultSet rsMonth = AppointmentQueries.selectCurrMonthAppointments();
        ResultSet rsWeek = AppointmentQueries.selectCurrWeekAppointments();

        fillAppointmentTable(allAppointmentsTableView, rsAll);
        fillAppointmentTable(currMonthTableView, rsMonth);
        fillAppointmentTable(currWeekTableView, rsWeek);
    }

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
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            fillComboAndDates();
            fillAllAppointmentTables();

        } catch (Exception e) {
            AlertPopups.generateErrorMessage(GENERAL_ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Listener that sets end date to equal start date once start date is chosen
        startDatePicker.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            endDatePicker.setValue(startDatePicker.getValue());
            startDateChosen = true;
        });

        // Listener that enables choosing end hour and minute once start date is chosen
        startDatePicker.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (startDateChosen) {
                endHourComboBox.setDisable(false);
                endMinuteComboBox.setDisable(false);
            }
        });

        // Listener that accounts for potential spillover to following day depending on user's location
        endHourComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if ((endHourComboBox.getValue() < 14) && (endHourComboBox.getValue() < startHourComboBox.getValue()) &&
                    (!addListenerTriggered) && (startDateChosen)) {
                endDatePicker.setValue(endDatePicker.getValue().plusDays(1));
                addListenerTriggered = true;
            }
            if ((endHourComboBox.getValue() >= startHourComboBox.getValue()) && addListenerTriggered) {
                endDatePicker.setValue(endDatePicker.getValue().minusDays(1));
                addListenerTriggered = false;
            }
        });

    }
}

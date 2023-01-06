package controller;

import helper.Query;
import helper.ScreenChanger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private ObservableList<ObservableList> data;
    ScreenChanger screenChanger = new ScreenChanger();

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private Label customersLabel;

    @FXML
    private TableView<ObservableList> customersTableView;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    void onActionAddCustomer(ActionEvent event) {
        System.out.println("Add customer button pressed");
    }

    @FXML
    void onActionDeleteCustomer(ActionEvent event) {
        System.out.println("Delete customer button pressed");
    }

    @FXML
    void onActionDisplayWelcome(ActionEvent event) throws IOException {
        screenChanger.changeScreen(event, "Welcome");
    }

    @FXML
    void onActionUpdateCustomer(ActionEvent event) {
        System.out.println("Update customer button pressed");
    }


    public void fillCustomersTable() {
        data = FXCollections.observableArrayList();
        try {

            ResultSet rs = Query.selectAllCustomers();

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        try {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        } catch (NullPointerException npe) {
                            return new SimpleStringProperty("null");
                        }
                    }
                });
                customersTableView.getColumns().addAll(col);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            //Added to customerTableview
            customersTableView.setItems(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("~CustomerController initialized");
        fillCustomersTable();
    }

}


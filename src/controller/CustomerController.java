package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class CustomerController {

    Stage stage;
    private TableView tableview;
    private ObservableList<ObservableList> data;
    private static Connection connection;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private Label customersLabel;

    @FXML
    private TableView<?> customersTableView;

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
    void onActionDisplayWelcome(ActionEvent event) {
        System.out.println("Back button pressed");
    }

    @FXML
    void onActionUpdateCustomer(ActionEvent event) {
        System.out.println("Update customer button pressed");
    }

    public void fillCustomersTable() {
        data = FXCollections.observableArrayList();
        try {
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * FROM client_schedule.customers";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn column = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableview.getColumns().addAll(column);
                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableview = new TableView();
        fillCustomersTable();
        Scene scene = new Scene(tableview);
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
    }

}


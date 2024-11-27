package com.example.sda_project.controller;
import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Orders;
import com.example.sda_project.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ViewSalesReportController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Orders> salesTable;
    @FXML
    private TableColumn<Orders, Integer> orderIdColumn;
    @FXML
    private TableColumn<Orders, Integer> customerIdColumn;
    @FXML
    private TableColumn<Orders, String> orderDateColumn;
    @FXML
    private TableColumn<Orders, Integer> deliveryBoyIdColumn;
    @FXML
    private TableColumn<Orders, String> orderStatusColumn;
    @FXML
    private Label messageLabel;

    private final ObservableList<Orders> orders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up columns for the sales table
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        deliveryBoyIdColumn.setCellValueFactory(new PropertyValueFactory<>("DeliveryBoy_id"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    public void viewSales(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            messageLabel.setText("Please select a date.");
            return;
        }

        loadSales(selectedDate);
    }

    private void loadSales(LocalDate date) {
        orders.clear();
        try (Connection connection = DBUtil.getConnection()) {
            String query = "SELECT order_id, customer_id, order_date, DeliveryBoy_id, status " +
                    "FROM Orders WHERE DATE(order_date) = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, java.sql.Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Orders order = new Orders();
                order.setOrder_id(resultSet.getInt("order_id"));
                order.setCustomer_id(resultSet.getInt("customer_id"));
                order.setOrder_date(resultSet.getTimestamp("order_date"));
                order.setDeliveryBoy_id(resultSet.getInt("DeliveryBoy_id"));
                order.setStatus(resultSet.getString("status"));

                orders.add(order);
            }

            salesTable.setItems(orders);

            if (orders.isEmpty()) {
                messageLabel.setText("No orders found for the selected date.");
            } else {
                messageLabel.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading sales data.");
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Admin Portal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Failed");
            alert.setContentText("Unable to load the admin page.");
            alert.showAndWait();
        }
    }
}

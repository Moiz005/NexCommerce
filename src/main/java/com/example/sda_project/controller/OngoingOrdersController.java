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

public class OngoingOrdersController {

    @FXML
    private TableView<Orders> ongoingOrdersTable;

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

    private final ObservableList<Orders> ongoingOrders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up columns for the ongoing orders table
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        deliveryBoyIdColumn.setCellValueFactory(new PropertyValueFactory<>("DeliveryBoy_id"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadOngoingOrders();
    }

    private void loadOngoingOrders() {
        ongoingOrders.clear();
        try (Connection connection = DBUtil.getConnection()) {
            String query = "SELECT order_id, customer_id, order_date, DeliveryBoy_id, status " +
                    "FROM Orders WHERE status != 'Completed'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Orders order = new Orders();
                order.setOrder_id(resultSet.getInt("order_id"));
                order.setCustomer_id(resultSet.getInt("customer_id"));
                order.setOrder_date(resultSet.getTimestamp("order_date"));
                order.setDeliveryBoy_id(resultSet.getInt("DeliveryBoy_id"));
                order.setStatus(resultSet.getString("status"));

                ongoingOrders.add(order);
            }

            ongoingOrdersTable.setItems(ongoingOrders);

            if (ongoingOrders.isEmpty()) {
                messageLabel.setText("No ongoing orders found.");
            } else {
                messageLabel.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading ongoing orders.");
        }
    }

    @FXML
    private void markAsDelivered(ActionEvent event) {
        Orders selectedOrder = ongoingOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            messageLabel.setText("No order selected.");
            return;
        }

        try (Connection connection = DBUtil.getConnection()) {
            String query = "UPDATE Orders SET status = 'Completed' WHERE order_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, selectedOrder.getOrder_id());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ongoingOrders.remove(selectedOrder);
                messageLabel.setText("Order marked as delivered.");
            } else {
                messageLabel.setText("Failed to update order status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error updating order status.");
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
            showError("Unable to load the admin page.");
        }
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error Occurred");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}

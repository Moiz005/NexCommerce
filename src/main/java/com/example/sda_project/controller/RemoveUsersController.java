package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Customer;
import com.example.sda_project.model.DeliveryBoy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.sda_project.util.DBUtil;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveUsersController {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    private TableColumn<Customer, String> customerUsernameColumn;

    @FXML
    private TableColumn<Customer, String> customerAddressColumn;

    @FXML
    private TableView<DeliveryBoy> deliveryBoyTable;

    @FXML
    private TableColumn<DeliveryBoy, Integer> deliveryBoyIdColumn;

    @FXML
    private TableColumn<DeliveryBoy, String> deliveryBoyUsernameColumn;

    @FXML
    private TableColumn<DeliveryBoy, String> deliveryBoyAddressColumn;

    @FXML
    private TableColumn<DeliveryBoy, String> deliveryBoyAvailabilityColumn;

    @FXML
    private Label messageLabel;

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<DeliveryBoy> deliveryBoys = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up columns for Customer table
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        customerUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Set up columns for DeliveryBoy table
        deliveryBoyIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        deliveryBoyUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        deliveryBoyAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        deliveryBoyAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));

        loadCustomers();
        loadDeliveryBoys();
    }

    private void loadCustomers() {
        customers.clear();
        try (Connection connection = DBUtil.getConnection()) {
            // Query without Address
            String query = "SELECT customer_id, username FROM Customer";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setID(resultSet.getInt("customer_id"));
                customer.setUsername(resultSet.getString("username"));
                // Do not set address if it doesn't exist in the schema

                customers.add(customer);
            }

            customerTable.setItems(customers);
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading customers.");
        }
    }


    private void loadDeliveryBoys() {
        deliveryBoys.clear();
        try (Connection connection = DBUtil.getConnection()) {
            // Query without Available
            String query = "SELECT DeliveryBoy_ID, username, Address FROM DeliveryBoy";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                DeliveryBoy deliveryBoy = new DeliveryBoy();
                deliveryBoy.setID(resultSet.getInt("DeliveryBoy_ID"));
                deliveryBoy.setUsername(resultSet.getString("username"));
                deliveryBoy.setAddress(resultSet.getString("Address"));
                // Do not set availability if it doesn't exist in the schema

                deliveryBoys.add(deliveryBoy);
            }

            deliveryBoyTable.setItems(deliveryBoys);
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading delivery boys.");
        }
    }


    @FXML
    public void removeSelectedCustomer(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            messageLabel.setText("Select a customer to remove.");
            return;
        }

        try (Connection connection = DBUtil.getConnection()) {
            String deleteQuery = "DELETE FROM Customer WHERE customer_id = ?";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, selectedCustomer.getID());
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                customers.remove(selectedCustomer);
                messageLabel.setText("Customer removed successfully.");
            } else {
                messageLabel.setText("Failed to remove customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error removing customer.");
        }
    }

    @FXML
    public void removeSelectedDeliveryBoy(ActionEvent event) {
        DeliveryBoy selectedDeliveryBoy = deliveryBoyTable.getSelectionModel().getSelectedItem();
        if (selectedDeliveryBoy == null) {
            messageLabel.setText("Select a delivery boy to remove.");
            return;
        }

        try (Connection connection = DBUtil.getConnection()) {
            String deleteQuery = "DELETE FROM DeliveryBoy WHERE DeliveryBoy_ID = ?";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, selectedDeliveryBoy.getID());
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                deliveryBoys.remove(selectedDeliveryBoy);
                messageLabel.setText("Delivery boy removed successfully.");
            } else {
                messageLabel.setText("Failed to remove delivery boy.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error removing delivery boy.");
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

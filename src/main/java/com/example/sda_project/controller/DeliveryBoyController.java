package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Customer;
import com.example.sda_project.model.Orders;
import com.example.sda_project.model.User;
import com.example.sda_project.util.DBUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryBoyController {
    @FXML
    private VBox orderList;
    User user = LoginController.getGlobalUser();
    Orders orders = new Orders();

    @FXML
    private void goToReviews(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Reviews.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("DeliveryBoy.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'DeliveryBoy.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Reviews");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Error loading the Reviews page: " + ex.getMessage());
        }
    }

    @FXML
    public void initialize() {
        handleAssignedOrders();
    }

    void handleAssignedOrders(){
        String query = "Select * from Orders where DeliveryBoy_id = ? and status = 'Not Completed'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, user.getID());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                orders.setOrder_id(resultSet.getInt("order_id"));
                orders.setCustomer_id(resultSet.getInt("customer_id"));
                orders.setOrder_date(resultSet.getDate("order_date"));
                orders.setDeliveryBoy_id(resultSet.getInt("DeliveryBoy_id"));
            }

        }
        catch (SQLException err) {
            err.printStackTrace();
        }

        if(orders.getOrder_id() == -1){
            return;
        }
        setDeliveryBoyAvailability("Unavailable");
        query = "Select * from Customer where customer_id = ?";
        Customer customer = new Customer();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, orders.getCustomer_id());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                customer.setID(resultSet.getInt("customer_id"));
                customer.setUsername(resultSet.getString("username"));
                customer.setAddress(resultSet.getString("Address"));
            }
        }
        catch (SQLException err) {
            err.printStackTrace();
        }

        displayOrders(orders, customer);
    }

    void displayOrders(Orders orders, Customer customer){
        orderList.getChildren().clear();
        GridPane orderGrid = new GridPane();
        orderGrid.setHgap(15);
        orderGrid.setVgap(10);
        orderGrid.setPadding(new Insets(15));
        orderGrid.setStyle("-fx-border-color: #e55b3c; -fx-border-radius: 10; -fx-background-color: #ffe4e1; -fx-background-radius: 10;");

        Label order_id = new Label("Order ID: "+String.valueOf(orders.getOrder_id()));
        order_id.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        order_id.setTextFill(Color.web("#ff6347"));
        GridPane.setConstraints(order_id, 0, 0);

        Label order_date = new Label("Order Date: " + String.valueOf(orders.getOrder_date()));
        order_date.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        order_date.setTextFill(Color.web("#e55b3c"));
        GridPane.setConstraints(order_date, 0, 1);

        Label customer_name = new Label("Customer Name: " + customer.getUserName());
        customer_name.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        customer_name.setTextFill(Color.web("#e55b3c"));
        GridPane.setConstraints(customer_name, 0, 2);

        Label Address = new Label("Customer Address: " + customer.getAddress());
        Address.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        Address.setTextFill(Color.web("#e55b3c"));
        GridPane.setConstraints(Address, 0, 3);

        Button orderCompleteBtn = new Button("Complete Order");
        orderCompleteBtn.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-weight: bold;");
        orderCompleteBtn.setOnAction(e -> {
            setDeliveryBoyAvailability("Available");
            orderList.getChildren().clear();
            setOrderStatus("Completed");
        });
        GridPane.setConstraints(orderCompleteBtn, 1, 2);

        orderGrid.getChildren().addAll(order_id, order_date, customer_name, Address, orderCompleteBtn);
        orderList.getChildren().add(orderGrid);
    }

    void setDeliveryBoyAvailability(String availability){
        user.setAvailability(availability);
        String query = "Update DeliveryBoy Set isAvailable = ? where id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, availability);
            stmt.setInt(2, user.getID());
            stmt.executeUpdate();
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
    }

    void setOrderStatus(String status){
        String query = "Update Orders Set status = ? where order_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, status);
            stmt.setInt(2, orders.getOrder_id());
            stmt.executeUpdate();
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
    }

    @FXML
    private void gotoTotalOrders(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TotalOrders.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("DeliveryBoy.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'DeliveryBoy.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Total Orders");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Error loading the Total Order page: " + ex.getMessage());
        }
    }
}
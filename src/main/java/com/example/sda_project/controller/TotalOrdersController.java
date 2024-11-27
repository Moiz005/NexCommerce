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
import java.util.ArrayList;
import java.util.List;

public class TotalOrdersController {
    @FXML
    private VBox orderList;
    User user = LoginController.getGlobalUser();
    List<Customer> customers = new ArrayList<>();
    List<Orders> orders = new ArrayList<>();

    @FXML
    private void gotoAssignedOrders(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DeliveryBoy.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("DeliveryBoy.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'DeliveryBoy.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Assigned Orders");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Error loading the Assigned Orders page: " + ex.getMessage());
        }
    }

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
        handleTotalOrders();
    }

    private void handleTotalOrders(){
        String query = "Select * from Orders where DeliveryBoy_id = ? and status = 'Completed'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, user.getID());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                Orders order = new Orders();
                order.setOrder_id(resultSet.getInt("order_id"));
                order.setCustomer_id(resultSet.getInt("customer_id"));
                order.setOrder_date(resultSet.getDate("order_date"));
                order.setDeliveryBoy_id(resultSet.getInt("DeliveryBoy_id"));
                orders.add(order);
            }

        }
        catch (SQLException err) {
            err.printStackTrace();
        }
        if(orders.isEmpty()){
            return;
        }

        query = "Select * from Customer c Join Orders o on c.customer_id = o.customer_id where DeliveryBoy_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, user.getID());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                Customer customer = new Customer();
                customer.setID(resultSet.getInt("customer_id"));
                customer.setUsername(resultSet.getString("username"));
                customer.setAddress(resultSet.getString("Address"));
                customers.add(customer);
            }
        }
        catch (SQLException err) {
            err.printStackTrace();
        }

        for (Customer customer : customers){
            displayTotalOrders(customer);
        }
    }

    private void displayTotalOrders(Customer customer){
        for (Orders order : orders){
            orderList.getChildren().clear();
            GridPane orderGrid = new GridPane();
            orderGrid.setHgap(15);
            orderGrid.setVgap(10);
            orderGrid.setPadding(new Insets(15));
            orderGrid.setStyle("-fx-border-color: #e55b3c; -fx-border-radius: 10; -fx-background-color: #ffe4e1; -fx-background-radius: 10;");

            Label order_id = new Label("Order ID: "+String.valueOf(order.getOrder_id()));
            order_id.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            order_id.setTextFill(Color.web("#ff6347"));
            GridPane.setConstraints(order_id, 0, 0);

            Label order_date = new Label("Order Date: " + String.valueOf(order.getOrder_date()));
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

            orderGrid.getChildren().addAll(order_id, order_date, customer_name, Address);
            orderList.getChildren().add(orderGrid);
        }
    }
}
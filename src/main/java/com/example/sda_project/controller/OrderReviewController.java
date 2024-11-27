package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
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
import javafx.scene.control.Alert;
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
import java.util.ArrayList;
import java.util.List;

public class OrderReviewController {
    @FXML
    private VBox orderList;
    List<Orders> orders = new ArrayList<>();
    User user = LoginController.getGlobalUser();

    @FXML
    protected void goToHome(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Home.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Error loading the Home page: " + ex.getMessage());
        }
    }

    @FXML
    protected void goToAbout(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("About.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("About");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Error loading the About page: " + ex.getMessage());
        }
    }

    @FXML
    private void goToCart(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Cart.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Cart");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Error loading the Cart page: " + ex.getMessage());
        }
    }

    @FXML
    private void goToWishList(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WishList.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Wish List");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Error loading the Wish List page: " + ex.getMessage());
        }
    }

    @FXML
    public void initialize(){
        getOrders();
    }

    private void getOrders(){
        String query = "Select o.* from Orders o Left Join Reviews r on o.order_id = r.order_id where r.order_id is NULL and o.customer_id = ? and o.status = 'Completed'";
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

        displayOrders();
    }

    private void displayOrders(){
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

            Label customer_name = new Label("Customer Name: " + user.getUserName());
            customer_name.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            customer_name.setTextFill(Color.web("#e55b3c"));
            GridPane.setConstraints(customer_name, 0, 2);

            Label Address = new Label("Customer Address: " + user.getAddress());
            Address.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            Address.setTextFill(Color.web("#e55b3c"));
            GridPane.setConstraints(Address, 0, 3);

            Button giveReviewBtn = new Button("Give Review");
            giveReviewBtn.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-weight: bold;");
            giveReviewBtn.setOnAction(e -> goToGiveReviews(e));
            GridPane.setConstraints(giveReviewBtn, 1, 2);

            orderGrid.getChildren().addAll(order_id, order_date, customer_name, Address, giveReviewBtn);
            orderList.getChildren().add(orderGrid);
        }
    }

    private void goToGiveReviews(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GiveReviews.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            // Load the CSS file
            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            }
            else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Give Reviews");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            showError("Error Loading the Give Reviews Page");
        }
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error Occurred");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    @FXML
    private void handleLogOut(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Error loading the Login page: " + ex.getMessage());
        }
    }
}
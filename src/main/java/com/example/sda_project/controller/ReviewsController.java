package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Review;
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

public class ReviewsController {
    @FXML
    private VBox reviewList;
    List<Review> reviews = new ArrayList<>();
    User user = LoginController.getGlobalUser();

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

    @FXML
    private void goToAssignedOrders(ActionEvent e){
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
    public void initialize(){
        getReviews();
    }

    private void getReviews(){
        String query = "Select * from Reviews where DeliveryBoy_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, user.getID());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                Review review = new Review();
                review.setReview_id(resultSet.getInt("review_id"));
                review.setCustomer_id(resultSet.getInt("customer_id"));
                review.setOrder_id(resultSet.getInt("order_id"));
                review.setDeliveryBoy_id(resultSet.getInt("DeliveryBoy_id"));
                review.setReview(resultSet.getString("review"));
                reviews.add(review);
            }

        }
        catch (SQLException err) {
            err.printStackTrace();
        }
        if(reviews.isEmpty()){
            return;
        }

        displayReviews();
    }

    private void displayReviews(){
        reviewList.getChildren().clear();
        for (Review review : reviews){
            GridPane reviewGrid = new GridPane();
            reviewGrid.setHgap(15);
            reviewGrid.setVgap(10);
            reviewGrid.setPadding(new Insets(15));
            reviewGrid.setStyle("-fx-border-color: #e55b3c; -fx-border-radius: 10; -fx-background-color: #ffe4e1; -fx-background-radius: 10;");

            Label review_id = new Label("Review ID: "+String.valueOf(review.getReview_id()));
            review_id.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            review_id.setTextFill(Color.web("#ff6347"));
            GridPane.setConstraints(review_id, 0, 0);

            Label order_id = new Label("Order ID: "+String.valueOf(review.getOrder_id()));
            order_id.setFont(Font.font("Arial", FontWeight.MEDIUM, 13));
            order_id.setTextFill(Color.web("#ff6347"));
            GridPane.setConstraints(order_id, 0, 0);

            Label customer_id = new Label("Customer ID: " + review.getCustomer_id());
            customer_id.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            customer_id.setTextFill(Color.web("#e55b3c"));
            GridPane.setConstraints(customer_id, 0, 2);

            Label reviewString = new Label("Review: " + review.getReview());
            reviewString.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            reviewString.setTextFill(Color.web("#e55b3c"));
            GridPane.setConstraints(reviewString, 0, 3);

            reviewGrid.getChildren().addAll(review_id, order_id, customer_id, reviewString);
            reviewList.getChildren().add(reviewGrid);
        }
    }
}

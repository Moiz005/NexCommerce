package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddCategoryController {

    @FXML
    private TextField categoryField;

    @FXML
    private Label messageLabel;

    public void saveCategory() {
        String categoryName = categoryField.getText().trim();
        System.out.println("Attempting to add category: " + categoryName);

        if (categoryName.isEmpty()) {
            messageLabel.setText("Category name cannot be empty!");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/newSupermarket", "root", "1234")) {
            String checkQuery = "SELECT COUNT(*) FROM Categories WHERE category_name = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, categoryName);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                messageLabel.setText("Error: Category already exists!");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            String insertQuery = "INSERT INTO Categories (category_name) VALUES (?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, categoryName);
            int rowsInserted = insertStatement.executeUpdate();


            if (rowsInserted > 0) {
                messageLabel.setText("Category added successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
                categoryField.clear(); // Clear input field
            } else {
                messageLabel.setText("Error: Unable to add category.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            // Load the admin.fxml file
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/com/example/sda_project/admin.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Admin Portal");
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}

package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Category;
import com.example.sda_project.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddProductController {
    @FXML private TextField productNameField;
    @FXML private TextField categoryTreeField;
    @FXML private TextField retailPriceField;
    @FXML private TextField discountedPriceField;
    @FXML private TextField imageField;
    @FXML private TextField descriptionField;
    @FXML private TextField productRatingField;
    @FXML private TextField overallRatingField;
    @FXML private TextField brandField;
    @FXML private ComboBox<Category> categoryComboBox;

    @FXML private Label messageLabel;
    @FXML
    public void initialize() {
        loadCategories();
    }
    private void loadCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();

        try (Connection connection = DBUtil.getConnection()) {
            String query = "SELECT category_id, category_name FROM Categories";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("category_id");
                String name = resultSet.getString("category_name");
                categories.add(new Category(id, name));
            }

            categoryComboBox.setItems(categories);
        } catch (SQLException e) {
            messageLabel.setText("Error loading categories: " + e.getMessage());
        }
    }
    @FXML
    public void addProduct() {
        String productName = productNameField.getText().trim();
        String categoryTree = categoryTreeField.getText().trim();
        String retailPrice = retailPriceField.getText().trim();
        String discountedPrice = discountedPriceField.getText().trim();
        String image = imageField.getText().trim();
        String description = descriptionField.getText().trim();
        String productRating = productRatingField.getText().trim();
        String overallRating = overallRatingField.getText().trim();
        String brand = brandField.getText().trim();
        Category selectedCategory = categoryComboBox.getValue();
        if (productName.isEmpty() || selectedCategory == null) {
            messageLabel.setText("Product Name and Category are required.");
            return;
        }

        try (Connection connection = DBUtil.getConnection()) {
            String insertQuery = "INSERT INTO Products (product_name, category_name, retail_price, discounted_price, image, description, product_rating, overall_rating, brand, category_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);

            statement.setString(1, productName);
            statement.setString(2, categoryTree);
            statement.setString(3, retailPrice);
            statement.setString(4, discountedPrice);
            statement.setString(5, image);
            statement.setString(6, description);
            statement.setString(7, productRating);
            statement.setString(8, overallRating);
            statement.setString(9, brand);
            statement.setInt(10, selectedCategory.getId());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                messageLabel.setText("Product added successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
                clearFields();
            } else {
                messageLabel.setText("Error: Unable to add product.");
            }
        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }
    private void clearFields() {
        productNameField.clear();
        categoryTreeField.clear();
        retailPriceField.clear();
        discountedPriceField.clear();
        imageField.clear();
        descriptionField.clear();
        productRatingField.clear();
        overallRatingField.clear();
        brandField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
    }
    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/com/example/sda_project/admin.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Admin Portal");
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Error loading Admin Portal");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Category;
import com.example.sda_project.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveCategoryController {

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, String> categoryNameColumn;

    @FXML
    private Label messageLabel;

    private final ObservableList<Category> categories = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        categoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        loadCategories();
    }

    private void loadCategories() {
        categories.clear();

        try (Connection connection = DBUtil.getConnection()) {
            String query = "SELECT category_id, category_name FROM Categories"; // Fetch both ID and Name
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("category_id"); // Retrieve category_id
                String name = resultSet.getString("category_name"); // Retrieve category_name
                categories.add(new Category(id, name)); // Use updated constructor
            }

            categoryTable.setItems(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading categories.");
        }
    }

    @FXML
    public void removeCategory(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) {
            messageLabel.setText("Select a category to remove.");
            return;
        }

        try (Connection connection = DBUtil.getConnection()) {
            String deleteQuery = "DELETE FROM Categories WHERE category_id = ?"; // Use category_id for deletion
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, selectedCategory.getId()); // Use ID for removal

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                categories.remove(selectedCategory);
                messageLabel.setText("Category removed successfully.");
            } else {
                messageLabel.setText("Failed to remove category.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Database error.");
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            // Load the admin.fxml file
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Admin Portal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Failed");
            alert.setContentText("Unable to load the admin page.");
            alert.showAndWait();
        }
    }
}

package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Product;
import com.example.sda_project.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveProductController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    @FXML
    private TableColumn<Product, String> imageUrlColumn;

    @FXML
    private Label messageLabel;

    private final ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        imageUrlColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        // Load data into the table
        loadProducts();
    }

    private void loadProducts() {
        products.clear();
        try (Connection connection = DBUtil.getConnection()) {
            String query = "SELECT product_name, retail_price, description, image FROM Products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int product_id = resultSet.getInt("product_id");
                String name = resultSet.getString("product_name");
                double price = resultSet.getDouble("retail_price");
                String description = resultSet.getString("description");
                String imageUrl = resultSet.getString("image");
                int  capacity = resultSet.getInt("quantity");

                products.add(new Product(product_id, name, price, description, imageUrl, capacity));
            }

            productTable.setItems(products);
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading products.");
        }
    }

    @FXML
    public void removeSelectedProduct(ActionEvent event) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            messageLabel.setText("Select a product to remove.");
            return;
        }

        try (Connection connection = DBUtil.getConnection()) {
            String deleteQuery = "DELETE FROM Products WHERE product_name = ?";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setString(1, selectedProduct.getName());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                products.remove(selectedProduct);
                messageLabel.setText("Product removed successfully.");
            } else {
                messageLabel.setText("Failed to remove product.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Database error.");
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

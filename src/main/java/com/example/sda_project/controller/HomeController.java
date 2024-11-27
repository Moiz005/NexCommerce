package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Product;
import com.example.sda_project.model.WishList;
import com.example.sda_project.util.DBUtil;
import com.example.sda_project.model.Cart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.net.HttpURLConnection;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomeController {
    @FXML
    private VBox productList;

    private static final Cart cart = new Cart();

    private static final WishList wishList = new WishList();

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
            showError(ex.getMessage());
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
            showError(ex.getMessage());
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
            showError(ex.getMessage());
        }
    }

    @FXML
    private void goToOrderReviews(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("OrderReviews.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Order Reviews");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            showError(ex.getMessage());
        }
    }

    @FXML
    protected void goToBrowse(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Browse.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Browse");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (IOException ex){
            showError(ex.getMessage());
        }
    }


    private List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT Top 10 * FROM Products";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                int product_id = resultSet.getInt("product_id");
                String name = resultSet.getString("product_name");
                double price = resultSet.getDouble("discounted_price");
                String description = resultSet.getString("description");
                String imageArrayString = resultSet.getString("image");
                int capacity = resultSet.getInt("quantity");

                String imageUrl = imageArrayString.replaceAll("\\[|\\]|\"", "").split(",\\s*")[0];
                if (isImageUrlValid(imageUrl)) {
                    products.add(new Product(product_id, name, price, description, imageUrl, capacity));
                }
            }

        } catch (SQLException e) {
            showError(e.getMessage());
        }

        return products;
    }

    private boolean isImageUrlValid(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        }
    }

    @FXML
    public void initialize() {
        List<Product> products = loadProducts();
        displayProducts(products);
    }

    private void displayProducts(List<Product> products) {
        productList.getChildren().clear();

        for (Product product : products) {
            GridPane productGrid = new GridPane();
            productGrid.setHgap(15);
            productGrid.setVgap(10);
            productGrid.setPadding(new Insets(15));
            productGrid.setStyle("-fx-border-color: #e55b3c; -fx-border-radius: 10; -fx-background-color: #ffe4e1; -fx-background-radius: 10;");

            ImageView imageView = new ImageView();
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Image image = new Image(product.getImageUrl(), 120, 120, true, true); // Adjusted image size
                imageView.setImage(image);
            }
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            GridPane.setConstraints(imageView, 0, 0, 1, 3); // Image occupies first column

            Label nameLabel = new Label("Name: " + product.getName());
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            nameLabel.setTextFill(Color.web("#ff6347"));
            GridPane.setConstraints(nameLabel, 1, 0);

            Label priceLabel = new Label("Price: $" + product.getPrice());
            priceLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            priceLabel.setTextFill(Color.web("#e55b3c"));
            GridPane.setConstraints(priceLabel, 1, 1);

            Label descriptionLabel = new Label("Description: " + product.getDescription());
            descriptionLabel.setWrapText(true);
            descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            descriptionLabel.setTextFill(Color.web("#c55a3b"));
            GridPane.setConstraints(descriptionLabel, 1, 2);

            Button addToCartButton = new Button("Add to Cart");
            addToCartButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
            addToCartButton.setOnAction(e -> addToCart(product)); // Method to handle adding to cart
            GridPane.setConstraints(addToCartButton, 1, 3);
            GridPane.setMargin(addToCartButton, new Insets(10, 0, 0, 0)); // Adds margin above button

            Button addToWishListButton = new Button("Add to Wish List");
            addToWishListButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
            addToWishListButton.setOnAction(e -> addToWishList(product)); // Method to handle adding to cart
            GridPane.setConstraints(addToWishListButton, 1, 4);
            GridPane.setMargin(addToWishListButton, new Insets(10, 0, 0, 0));

            productGrid.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, addToCartButton, addToWishListButton);

            productList.getChildren().add(productGrid);
        }
    }

    public static Cart getGlobalCart() {
        return cart;
    }

    public static WishList getGlobalWishList() {
        return wishList;
    }

    private void addToCart(Product product) {
        cart.addProducts(product);
    }

    private void addToWishList(Product product) {
        wishList.addProducts(product);
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
            showError(ex.getMessage());
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
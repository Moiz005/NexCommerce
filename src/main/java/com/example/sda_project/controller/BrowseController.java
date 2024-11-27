package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Cart;
import com.example.sda_project.model.Product;
import com.example.sda_project.model.WishList;
import com.example.sda_project.util.UIComponentFactory;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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

public class BrowseController {
    @FXML
    private TextField searchField;
    @FXML
    private VBox searchResults;
    List<Product> products = new ArrayList<>();
    Cart cart = HomeController.getGlobalCart();
    WishList wishList = HomeController.getGlobalWishList();

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
            ex.printStackTrace();
            System.out.println("Error loading the Order Reviews page: " + ex.getMessage());
        }
    }

    @FXML
    private void handleSearchButton(){
        String searchText = searchField.getText().trim();
        if(searchText.isEmpty()){
            showError("Please enter a product name to search");
            return;
        }
        searchProduct(searchText);
    }

    private void searchProduct(String productName){
        String query = "SELECT product_name, discounted_price, description, image FROM Products WHERE product_name LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + productName + "%");

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setName(resultSet.getString("product_name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getInt("discounted_price"));
                String imageArrayString = resultSet.getString("image");

                String imageUrl = imageArrayString.replaceAll("\\[|\\]|\"", "").split(",\\s*")[0];
                product.setImageUrl(imageUrl);
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error occurred while searching for the product.");
        }
        if(products.isEmpty()){
            showError("No products with name " + productName);
        }
        displayProducts();
    }

    private void displayProducts() {
        searchResults.getChildren().clear();

        for (Product product : products) {
            GridPane productGrid = UIComponentFactory.createProductGrid();

            ImageView imageView = UIComponentFactory.createImageView(product.getImageUrl(), 100, 100);
            GridPane.setConstraints(imageView, 0, 0, 1, 3);

            Label nameLabel = UIComponentFactory.createLabel("Name: " + product.getName(), "#ff6347", true, 14);
            GridPane.setConstraints(nameLabel, 1, 0);

            Label priceLabel = UIComponentFactory.createLabel("Price: $" + product.getPrice(), "#e55b3c", false, 12);
            GridPane.setConstraints(priceLabel, 1, 1);

            Label descriptionLabel = UIComponentFactory.createLabel("Description: " + product.getDescription(), "#c55a3b", false, 12);
            descriptionLabel.setWrapText(true);
            GridPane.setConstraints(descriptionLabel, 1, 2);

            Button addToCartButton = UIComponentFactory.createButton("Add to Cart", "#ff6347", () -> addToCart(product));
            GridPane.setConstraints(addToCartButton, 1, 3);
            GridPane.setMargin(addToCartButton, new Insets(10, 0, 0, 0));

            Button addToWishListButton = UIComponentFactory.createButton("Add to Wish List", "#ff6347", () -> addToWishList(product));
            GridPane.setConstraints(addToWishListButton, 1, 4);
            GridPane.setMargin(addToWishListButton, new Insets(10, 0, 0, 0));

            productGrid.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, addToCartButton, addToWishListButton);

            searchResults.getChildren().add(productGrid);
        }
    }

    private void addToCart(Product product) {
        cart.addProducts(product);
    }

    private void addToWishList(Product product) {
        wishList.addProducts(product);
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
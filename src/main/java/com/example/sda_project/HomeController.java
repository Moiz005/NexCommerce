package com.example.sda_project;

import com.example.sda_project.util.DBUtil;
import com.example.sda_project.Product;
import com.example.sda_project.Cart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomeController {
    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Label searchResultsLabel;
    @FXML
    private VBox productList;

    private static Cart cart = new Cart();

    @FXML
    protected void goToAbout(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("About.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            // Load the CSS file
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

            // Load the CSS file
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
    private void handleSearchButton(){
        String searchText = searchField.getText().trim();
        if(searchText.isEmpty()){
            searchResultsLabel.setText("Please enter a product name to search!!!");
            return;
        }
        searchProduct(searchText);
    }

    private void searchProduct(String productName){
        String query = "SELECT product_name, discounted_price, description FROM Products WHERE product_name LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + productName + "%");

            ResultSet resultSet = stmt.executeQuery();

            // Check if any result exists
            if (resultSet.next()) {
                // Fetch the product details from the result set
                String productDetails = "Product: " + resultSet.getString("product_name") + "\n"
                        + "Discounted Price: " + resultSet.getDouble("discounted_price") + "\n"
                        + "Description: " + resultSet.getString("description");

                searchResultsLabel.setText(productDetails); // Display product details
            } else {
                searchResultsLabel.setText("No products found matching: " + productName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            searchResultsLabel.setText("Error occurred while searching for the product.");
        }
    }

    private List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT Top 10 product_name, discounted_price, description, image FROM Products";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("product_name");
                double price = resultSet.getDouble("discounted_price");
                String description = resultSet.getString("description");
                String imageArrayString = resultSet.getString("image");

                String imageUrl = imageArrayString.replaceAll("\\[|\\]|\"", "").split(",\\s*")[0];
//                if (isImageUrlValid(imageUrl)) {
                    products.add(new Product(name, price, description, imageUrl));
//                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            searchResultsLabel.setText("Error occurred while loading products.");
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

            productGrid.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, addToCartButton);

            productList.getChildren().add(productGrid);
        }
    }

    public static Cart getGlobalCart() {
        return cart;
    }

    private void addToCart(Product product) {
        cart.addProducts(product);
        System.out.println(product.getName());
    }
}
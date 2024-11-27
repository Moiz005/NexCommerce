package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Product;
import com.example.sda_project.model.WishList;
import com.example.sda_project.util.UIComponentFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class WishListController {
    @FXML
    private VBox productList;

    @FXML
    protected void goToHome(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Home.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            }
            else {
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

    private void goToWishList(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WishList.fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            // Load the CSS file
            URL cssFile = HelloApplication.class.getResource("style.css");
            if (cssFile != null) {
                scene.getStylesheets().add(cssFile.toExternalForm());
            } else {
                System.out.println("Warning: 'style.css' not found.");
            }

            Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);;
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
    public void initialize() {
        WishList wishList = HomeController.getGlobalWishList();
        displayProducts(wishList);
    }

    private void displayProducts(WishList wishList) {
        List<Product> products = wishList.getProducts();
        productList.getChildren().clear();

        for (Product product : products) {
            GridPane productGrid = UIComponentFactory.createProductGrid();

            // ImageView
            ImageView imageView = UIComponentFactory.createImageView(product.getImageUrl(), 100, 100);
            GridPane.setConstraints(imageView, 0, 0, 1, 3);

            // Labels
            Label nameLabel = UIComponentFactory.createLabel("Name: " + product.getName(), "#ff6347", true, 14);
            GridPane.setConstraints(nameLabel, 1, 0);

            Label priceLabel = UIComponentFactory.createLabel("Price: $" + product.getPrice(), "#e55b3c", false, 12);
            GridPane.setConstraints(priceLabel, 1, 1);

            Label descriptionLabel = UIComponentFactory.createLabel("Description: " + product.getDescription(), "#c55a3b", false, 12);
            descriptionLabel.setWrapText(true);
            GridPane.setConstraints(descriptionLabel, 1, 2);

            Label quantityLabel = UIComponentFactory.createLabel("Quantity: " + product.getQuantity(), "#ff6347", false, 12);
            GridPane.setConstraints(quantityLabel, 1, 3);

            // Buttons
            Button incrementButton = UIComponentFactory.createButton("+", "#ff6347", () -> {
                product.setQuantity(product.getQuantity() + 1);
                quantityLabel.setText("Quantity: " + product.getQuantity());
                wishList.setProducts(products);
                displayProducts(wishList);
            });
            GridPane.setConstraints(incrementButton, 2, 0);

            Button decrementButton = UIComponentFactory.createButton("-", "#e55b3c", () -> {
                int currentQuantity = product.getQuantity();
                if (currentQuantity > 0) {
                    product.setQuantity(currentQuantity - 1);
                    quantityLabel.setText("Quantity: " + product.getQuantity());
                    if (product.getQuantity() == 0) {
                        products.remove(product);  // Remove product if quantity is 0
                        wishList.setProducts(products);
                    }
                    displayProducts(wishList);
                }
            });
            GridPane.setConstraints(decrementButton, 2, 1);

            productGrid.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, quantityLabel, incrementButton, decrementButton);
            productList.getChildren().add(productGrid);
        }
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

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error Occurred");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}

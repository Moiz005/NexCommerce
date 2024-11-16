package com.example.sda_project;

import com.example.sda_project.HomeController;
import com.example.sda_project.Customer;
import com.example.sda_project.LoginController;
import com.example.sda_project.Product;
import com.example.sda_project.util.DBUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import javafx.stage.Window;
import java.time.LocalDate;

public class CartController {
    private double currentSubTotal = 0.0;

    @FXML
    private VBox productList;

    @FXML
    private Button checkOut;

    @FXML
    private Label subTotal;

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
    public void initialize() {
        Cart cart = HomeController.getGlobalCart();
        displayProducts(cart);
    }

    private void displayProducts(Cart cart) {
        List<Product> products = cart.getProducts();
        productList.getChildren().clear();
        currentSubTotal = 0.0;  // Reset subtotal at the beginning
        subTotal.setText("0.0");  // Reset the displayed subtotal to 0

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

            // Quantity Label
            Label quantityLabel = new Label("Quantity: " + product.getQuantity());
            quantityLabel.setWrapText(true);
            quantityLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            quantityLabel.setTextFill(Color.web("#ff6347"));
            GridPane.setConstraints(quantityLabel, 1, 3);

            // Update subtotal
            currentSubTotal += product.getPrice() * product.getQuantity();  // Add product price times quantity to subtotal
            subTotal.setText(String.format("%.2f", currentSubTotal));  // Update the display

            // Increment Button
            Button incrementButton = new Button("+");
            incrementButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-weight: bold;");
            incrementButton.setOnAction(e -> {
                product.setQuantity(product.getQuantity() + 1);
                quantityLabel.setText("Quantity: " + product.getQuantity());
                cart.setProducts(products);
                currentSubTotal += product.getPrice();  // Add the price of the product to subtotal
                subTotal.setText(String.format("%.2f", currentSubTotal));  // Update the display
                goToCart();
            });
            GridPane.setConstraints(incrementButton, 2, 0);

            // Decrement Button
            Button decrementButton = new Button("-");
            decrementButton.setStyle("-fx-background-color: #e55b3c; -fx-text-fill: white; -fx-font-weight: bold;");
            decrementButton.setOnAction(e -> {
                int currentQuantity = product.getQuantity();
                if (currentQuantity > 0) {
                    product.setQuantity(currentQuantity - 1);
                    quantityLabel.setText("Quantity: " + product.getQuantity());
                    if (product.getQuantity() == 0) {
                        products.remove(product);  // Remove product if quantity is 0
                        cart.setProducts(products);
                    }
                    currentSubTotal -= product.getPrice();  // Subtract the product price from subtotal
                    subTotal.setText(String.format("%.2f", currentSubTotal));  // Update the display
                    goToCart();
                }
            });
            GridPane.setConstraints(decrementButton, 2, 1);

            productGrid.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, quantityLabel, incrementButton, decrementButton);

            productList.getChildren().add(productGrid);
        }
    }

    private void goToCart(){
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

            Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);;
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
    private void handleCheckOut(ActionEvent e){
        Customer customer = LoginController.getGlobalCustomer();
        List<Product> productList = HomeController.getGlobalCart().getProducts();
        if(!productList.isEmpty()){
            String query = "insert into Orders(customer_id, order_date) Values(?,?)";
            Cart cart = HomeController.getGlobalCart();
            cart.getProducts().clear();
            goToCart();
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)){

                stmt.setInt(1, customer.getID());
                stmt.setDate(2, Date.valueOf(LocalDate.now()));

                stmt.executeUpdate();
            }
            catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }
}
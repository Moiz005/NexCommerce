package com.example.sda_project.util;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UIComponentFactory {

    public static GridPane createProductGrid() {
        GridPane productGrid = new GridPane();
        productGrid.setHgap(15);
        productGrid.setVgap(10);
        productGrid.setPadding(new Insets(15));
        productGrid.setStyle("-fx-border-color: #e55b3c; -fx-border-radius: 10; -fx-background-color: #ffe4e1; -fx-background-radius: 10;");
        return productGrid;
    }

    public static Label createLabel(String text, String color, boolean bold, int fontSize) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize));
        label.setTextFill(Color.web(color));
        return label;
    }

    public static Button createButton(String text, String backgroundColor, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        button.setOnAction(e -> action.run());
        return button;
    }

    public static ImageView createImageView(String imageUrl, double width, double height) {
        ImageView imageView = new ImageView();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl, width, height, true, true);
            imageView.setImage(image);
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }
}

package com.example.sda_project;

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
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.HBox;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void nextPage(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Home.fxml"));
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
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error loading the Home page: " + ex.getMessage());
        }
    }
}
package com.example.sda_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;

public class AboutController {
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
}

package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.model.Admin;
import com.example.sda_project.model.Customer;
import com.example.sda_project.model.DeliveryBoy;
import com.example.sda_project.model.User;
import com.example.sda_project.util.DBUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Button cancelbutton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private RadioButton DBoyRadioButton;

    @FXML
    private RadioButton CustomerRadioButton;

    @FXML
    private RadioButton AdminRadioButton;

    private static User user;

    public static User getGlobalUser(){
        return user;
    }

    public void cancelbuttonOnAction()
    {
        Stage stage=(Stage) cancelbutton.getScene().getWindow();
        stage.close();
    }

    public void LoginbuttonOnAction(ActionEvent e)
    {
        if(usernameTextField.getText().isBlank() || passwordPasswordField.getText().isBlank() )
        {
            showError("Please enter username and password to login");
        }
        else
        {
            String username = usernameTextField.getText().trim();
            String password = passwordPasswordField.getText().trim();
            if(CustomerRadioButton.isSelected()){
                CustomerLogin(e, username, password);
            }
            else if(DBoyRadioButton.isSelected()){
                DBoyLogin(e, username, password);
            }
            else if(AdminRadioButton.isSelected()){
                AdminLogin(e, username, password);
            }
        }
    }

    private void CustomerLogin(ActionEvent e, String username, String password) {
        user = new Customer();
        String query = "Select * from Customer where username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()){
                String p1 = resultSet.getString("password");
                user.setID(resultSet.getInt("customer_id"));
                user.setUsername(resultSet.getString("username"));
                user.setAddress(resultSet.getString("address"));
                if(BCrypt.checkpw(password, p1)){
                    try{
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Home.fxml"));
                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

                        // Load the CSS file
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
                        showError(ex.getMessage());
                    }
                }
                else{
                    showError("Wrong Password");
                }
            }
        }
        catch(SQLException ex){
            showError(ex.getMessage());
        }
    }

    private void DBoyLogin(ActionEvent e, String username, String password) {
        user = new DeliveryBoy();
        String query = "Select * from DeliveryBoy where username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                String p1 = resultSet.getString("password");
                user.setID(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setAddress(resultSet.getString("address"));
                if(BCrypt.checkpw(password, p1)){
                    try{
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DeliveryBoy.fxml"));
                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

                        URL cssFile = HelloApplication.class.getResource("DeliveryBoy.css");
                        if (cssFile != null) {
                            scene.getStylesheets().add(cssFile.toExternalForm());
                        }
                        else {
                            System.out.println("Warning: 'DeliveryBoy.css' not found.");
                        }

                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.setTitle("Delivery Boy");
                        stage.setScene(scene);
                        stage.setMaximized(true);
                        stage.show();
                    }
                    catch (IOException ex){
                        showError("Error Loading the Delivery Boy Page");
                    }
                }
                else{
                    showError("Wrong Password");
                }
            }
        }
        catch(SQLException ex){
            showError("Error occurred while logging in.");
        }
    }

    private void AdminLogin(ActionEvent e, String username, String password){
        user = new Admin();
        String query = "Select * from Admin where username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                String p1 = resultSet.getString("password");
                user.setID(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setAddress(resultSet.getString("address"));
                if(BCrypt.checkpw(password, p1)){
                    try{
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
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
                        stage.setTitle("Admin");
                        stage.setScene(scene);
                        stage.setMaximized(true);
                        stage.show();
                    }
                    catch (IOException ex){
                        showError("Error Loading the Admin Page");
                    }
                }
            }
        }
        catch(SQLException ex){
            showError("Error occurred while logging in.");
        }
    }

    @FXML
    public void createnewaccountbuttonOnAction(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException ex) {
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
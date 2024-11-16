package com.example.sda_project;

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
    private Label loginmessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private RadioButton StaffRadioButton;

    @FXML
    private RadioButton CustomerRadioButton;

    @FXML
    private RadioButton AdminRadioButton;

    private static Customer customer;

    public static Customer getGlobalCustomer(){
        return customer;
    }

    public void cancelbuttonOnAction(ActionEvent e)
    {
        Stage stage=(Stage) cancelbutton.getScene().getWindow();
        stage.close();
    }
    public void LoginbuttonOnAction(ActionEvent e)
    {
        if(usernameTextField.getText().isBlank()==true || passwordPasswordField.getText().isBlank()==true )
        {
            loginmessageLabel.setText("Please write a username and password!");
        }
        else
        {
            String username = usernameTextField.getText().trim();
            String password = passwordPasswordField.getText().trim();
            if(CustomerRadioButton.isSelected()){
                CustomerLogin(e, username, password);
            }
        }
    }
    private void CustomerLogin(ActionEvent e, String username, String password) {
        customer = new Customer();
        String query = "Select * from Customer where username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()){
                String p1 = resultSet.getString("password");
                customer.setID(resultSet.getInt("customer_id"));
                customer.setUsername(resultSet.getString("username"));
                if(p1.equals(password)){
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
                        ex.printStackTrace();
                        System.out.println("Error loading the Home page: " + ex.getMessage());
                    }
                }
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
            loginmessageLabel.setText("Error occurred while logging in.");
        }
    }

    @FXML
    public void createnewaccountbuttonOnAction(ActionEvent e) {
        // Load the registration form
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
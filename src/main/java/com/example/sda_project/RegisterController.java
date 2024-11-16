package com.example.sda_project;

import com.example.sda_project.util.DBUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.RadioButton;

public class RegisterController {
    @FXML
    private Button registerButton;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private RadioButton StaffRadioButton;

    @FXML
    private RadioButton CustomerRadioButton;

    @FXML
    private Label registerError;

    @FXML
    public void registerButtonOnAction(ActionEvent e) {
        String name = username.getText().trim();
        String pass = password.getText().trim();
        String userType;
        if(StaffRadioButton.isSelected()){
            userType = StaffRadioButton.getText();
        }
        else if(CustomerRadioButton.isSelected()){
            userType = CustomerRadioButton.getText();
        }
        else{
            userType = "";
        }

        try {
            if(name.isEmpty() || pass.isEmpty()){
                registerError.setText("Enter valid username and password for registration!!!");
            }
            else{
                if(CustomerRadioButton.isSelected()){
                    CustomerRegister(e, name, pass, userType);
                }
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void CustomerRegister(ActionEvent e, String username, String password, String userType){
        String query = "Insert into Customer(username, password) Values(?,?);";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, username);
            stmt.setString(2, password);

            stmt.executeUpdate();
        }
        catch(SQLException ex){
            ex.printStackTrace();
            registerError.setText("Error occurred while logging in.");
        }
    }
}
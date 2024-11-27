package com.example.sda_project.controller;

import com.example.sda_project.HelloApplication;
import com.example.sda_project.util.DBUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterController {
    @FXML
    private Button registerButton;

    @FXML
    private TextField username;

    @FXML
    private TextField address;

    @FXML
    private PasswordField password;

    @FXML
    private RadioButton DeliveryBoyRadioButton;

    @FXML
    private RadioButton CustomerRadioButton;

    @FXML
    private RadioButton AdminRadioButton;

    @FXML
    private Label registerError;

    @FXML
    public void registerButtonOnAction(ActionEvent e) {
        String name = username.getText().trim();
        String pass = password.getText().trim();
        String addr = address.getText().trim();

        try {
            if(name.isEmpty() || pass.isEmpty() || addr.isEmpty()){
                showError("Enter valid username and password for registration!!!");
            }
            else{
                if(CustomerRadioButton.isSelected()){
                    CustomerRegister(e, name, pass, addr);
                }
                else if(DeliveryBoyRadioButton.isSelected()){
                    DeliveryBoyRegister(e, name, pass, addr);
                }
                else if(AdminRadioButton.isSelected()){
                    AdminRegister(e, name, pass, addr);
                }
            }
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void CustomerRegister(ActionEvent e, String username, String password, String address){
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "Insert into Customer(username, password, address) Values(?,?,?);";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, address);

            stmt.executeUpdate();
        }
        catch(SQLException ex){
            ex.printStackTrace();
            showError(ex.getMessage());
        }
    }

    private void DeliveryBoyRegister(ActionEvent e, String username, String password, String address){
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "Insert into DeliveryBoy(username, password, address) Values(?,?,?);";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, address);

            stmt.executeUpdate();
        }
        catch(SQLException ex){
            ex.printStackTrace();
            showError(ex.getMessage());
        }
    }

    private void AdminRegister(ActionEvent e, String username, String password, String address){
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "Insert into Admin(username, password, address) Values(?,?,?);";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, address);

            stmt.executeUpdate();
        }
        catch(SQLException ex){
            ex.printStackTrace();
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
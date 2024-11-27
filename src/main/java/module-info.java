module com.example.sda_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jBCrypt;


    opens com.example.sda_project to javafx.fxml;
    exports com.example.sda_project;
    exports com.example.sda_project.util;
    opens com.example.sda_project.util to javafx.fxml;
    exports com.example.sda_project.model;
    opens com.example.sda_project.model to javafx.fxml;
    exports com.example.sda_project.controller;
    opens com.example.sda_project.controller to javafx.fxml;
}
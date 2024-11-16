module com.example.sda_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sda_project to javafx.fxml;
    exports com.example.sda_project;
}
module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens p5.bank to javafx.fxml, com.google.gson;
    exports p5.bank;
}
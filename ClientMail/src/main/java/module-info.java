module com.clientmail.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.clientmail.demo to javafx.fxml;
    exports com.clientmail.demo;
}

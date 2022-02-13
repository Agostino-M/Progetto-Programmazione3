module com.prog3.clientmail {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.prog3.clientmail to javafx.fxml;
    exports com.prog3.clientmail;

    opens com.prog3.clientmail.controller to javafx.fxml;
    exports com.prog3.clientmail.controller;

    opens com.prog3.clientmail.model to javafx.fxml;
    exports com.prog3.clientmail.model;

    opens com.prog3.clientmail.connection to javafx.fxml;
    exports com.prog3.clientmail.connection;

    opens com.prog3.common to javafx.fxml;
    exports com.prog3.common;
}

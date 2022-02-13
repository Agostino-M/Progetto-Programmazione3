module com.prog3.servermail {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.prog3.servermail to javafx.fxml;
    exports com.prog3.servermail;
    exports com.prog3.servermail.controller;
    opens com.prog3.servermail.controller to javafx.fxml;
    exports com.prog3.servermail.model;
    opens com.prog3.servermail.model to javafx.fxml;
    exports com.prog3.servermail.exception;
    opens com.prog3.servermail.exception to javafx.fxml;
    exports com.prog3.common;
    opens com.prog3.common to javafx.fxml;
}

module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.junit.jupiter.api;
    requires junit;

    opens com.example.demo.entity to javafx.base;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.login;
    exports com.example.demo.qlns;
    exports com.example.demo.leaddepartment;
    exports com.example.demo.userview;
    exports com.example.demo.userprofile;
    opens com.example.demo.login to javafx.fxml;
    opens com.example.demo.qlns to javafx.fxml;
    opens com.example.demo.leaddepartment to javafx.fxml;
    opens com.example.demo.userview to javafx.fxml;
    opens com.example.demo.userprofile to javafx.fxml;
    opens com.example.demo.departmentdata to javafx.fxml;
}
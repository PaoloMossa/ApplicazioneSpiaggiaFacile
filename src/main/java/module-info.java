module com.example.applicazionespiaggiafacile {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.graphics;

    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.applicazionespiaggiafacile to javafx.fxml;
    exports com.example.applicazionespiaggiafacile;
}
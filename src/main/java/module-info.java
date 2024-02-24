module com.example.ims {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.example.ims.scene;
    opens com.example.ims to javafx.fxml;

    exports com.example.ims;
    exports com.example.ims.scene;
}

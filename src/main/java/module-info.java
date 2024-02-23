module com.example.desktopappinventorymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.desktopappinventorymanagementsystem to javafx.fxml;
    exports com.example.desktopappinventorymanagementsystem;
}

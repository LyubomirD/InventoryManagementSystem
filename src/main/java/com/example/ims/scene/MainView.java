package com.example.ims.scene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class MainView {

    @FXML
    private StackPane contentPane;

    @FXML
    private void loadScene(String fxmlPath) {
        try {
            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void loadProductScene() {
        loadScene("/com/example/ims/product-view.fxml");
    }

    @FXML
    private void loadSupplierScene() {
        loadScene("/com/example/ims/supplier-view.fxml");
    }

    @FXML
    public void loadOrderScene() {
        loadScene("/com/example/ims/order-view.fxml");
    }
}

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

    private boolean productViewLoaded = false;

    @FXML
    private void loadProductScene() {
        if (!productViewLoaded) {
            try {
                AnchorPane productView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/ims/product-view.fxml")));
                contentPane.getChildren().setAll(productView);
                productViewLoaded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

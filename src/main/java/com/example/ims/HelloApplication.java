package com.example.ims;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Inventory Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}










/*
    public static void main(String[] args) {
//        launch();

        DatabaseConnection connection = new DatabaseConnection();

        // Create a ProductDTO instance
        ProductDTO productDTO = new ProductDTO(connection);

        // Create a Product object with sample data
        Product product = new Product("Test1", "Description1", 11.23, 21);

        try {
            // Add the product to the database
            productDTO.addProduct(product);
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

 */

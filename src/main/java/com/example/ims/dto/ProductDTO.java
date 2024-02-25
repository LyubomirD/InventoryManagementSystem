package com.example.ims.dto;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private final DatabaseConnection databaseConnection;

    public ProductDTO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, description, quantity_of_stock, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getQuantityOfStock());
            statement.setDouble(4, product.getPrice());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Product added successfully.");
            } else {
                System.err.println("Failed to add product.");
            }
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> productList = new ArrayList<>();

        String query = "SELECT * FROM products";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Integer productId = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Double quantityOfStock = resultSet.getDouble("quantity_of_stock");
                Double price = resultSet.getDouble("price");

                Product product = new Product(productId, name, description, quantityOfStock, price);
                productList.add(product);
            }
        }

        return productList;
    }
}

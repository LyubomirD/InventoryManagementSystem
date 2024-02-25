package com.example.ims.dto;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private final DatabaseConnection databaseConnection;



    public ProductDTO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> productList = new ArrayList<>();

        String query = "SELECT * FROM products";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Integer product_id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Double quantityOfStock = resultSet.getDouble("quantity_of_stock");
                Double price = resultSet.getDouble("price");

                Product product = new Product(name, description, quantityOfStock, price);
                product.setProduct_id(product_id);

                productList.add(product);
            }
        }

        return productList;
    }

    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, description, quantity_of_stock, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getQuantityOfStock());
            statement.setDouble(4, product.getPrice());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int productId = generatedKeys.getInt(1);
                    System.out.println("Product added successfully with id: " + productId);
                    product.setProduct_id(productId); // Set the generated product_id
                } else {
                    System.err.println("Failed to retrieve generated product ID.");
                }
            } else {
                System.err.println("Failed to add product.");
            }
        }
    }


    public void updateProduct(Product selectedProduct) throws SQLException {
        String query = "UPDATE products SET name=?, description=?, quantity_of_stock=?, price=? WHERE product_id=?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, selectedProduct.getName());
            statement.setString(2, selectedProduct.getDescription());
            statement.setDouble(3, selectedProduct.getQuantityOfStock());
            statement.setDouble(4, selectedProduct.getPrice());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.err.println("Failed to update product.");
            }
        }
    }

}

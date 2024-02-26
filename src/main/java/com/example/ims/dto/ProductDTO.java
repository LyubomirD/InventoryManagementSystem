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

    public void addProduct(Product newProduct) throws SQLException {
        String query = "INSERT INTO products (name, description, quantity_of_stock, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, newProduct.getName());
            statement.setString(2, newProduct.getDescription());
            statement.setDouble(3, newProduct.getQuantityOfStock());
            statement.setDouble(4, newProduct.getPrice());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted <= 0) {
                System.err.println("Failed to add newProduct.");
                return;
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (!generatedKeys.next()) {
                System.err.println("Failed to retrieve generated newProduct ID.");
                return;
            }

            int productId = generatedKeys.getInt(1);
            System.out.println("Product added successfully with id: " + productId);
            newProduct.setProduct_id(productId);
        }
    }

    public boolean isProductExistingAlready(Product newProduct) throws SQLException {
        String query = "SELECT * FROM products WHERE name=? AND description=? AND quantity_of_stock=? AND price=?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, newProduct.getName());
            statement.setString(2, newProduct.getDescription());
            statement.setDouble(3, newProduct.getQuantityOfStock());
            statement.setDouble(4, newProduct.getPrice());

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void updateProduct(Product updatedProduct, Integer productIdentificationNumber) throws SQLException {
        String query = "UPDATE products SET name=?, description=?, quantity_of_stock=?, price=? WHERE product_id=?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, updatedProduct.getName());
            statement.setString(2, updatedProduct.getDescription());
            statement.setDouble(3, updatedProduct.getQuantityOfStock());
            statement.setDouble(4, updatedProduct.getPrice());
            statement.setInt(5, productIdentificationNumber);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                updatedProduct.setProduct_id(productIdentificationNumber);
                System.out.println("Product updated successfully.");
            } else {
                System.err.println("Failed to update product.");
            }
        }
    }

    public void deleteProduct(int productIdentificationNumber) throws SQLException {
        String query = "DELETE FROM products WHERE product_id=?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, productIdentificationNumber);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.err.println("Failed to delete product.");
            }
        }
    }
}

package com.example.ims.dto;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

            statement.executeUpdate();
        }
    }
}

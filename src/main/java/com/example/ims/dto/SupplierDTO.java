package com.example.ims.dto;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.models.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDTO {

    private final DatabaseConnection databaseConnection;

    public SupplierDTO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> supplierList = new ArrayList<>();

        String query = "SELECT * FROM supplier";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Integer supplier_id = resultSet.getInt("supplier_id");
                String name = resultSet.getString("name");
                String contactInf = resultSet.getString("contact_inf");
                Integer productId = resultSet.getInt("product_id");

                Supplier supplier = new Supplier(name, contactInf, productId);
                supplier.setSupplier_id(supplier_id);

                supplierList.add(supplier);
            }
        }

        return supplierList;
    }

    public void addSupplier(Supplier newSupplier) throws SQLException {
        String query = "INSERT INTO supplier (name, contact_inf, product_id) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, newSupplier.getName());
            statement.setString(2, newSupplier.getContact_inf());
            statement.setDouble(3, newSupplier.getProduct_id());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int supplierId = generatedKeys.getInt(1);
                    System.out.println("Supplier added successfully with id: " + supplierId);
                    newSupplier.setSupplier_id(supplierId);
                } else {
                    System.err.println("Failed to retrieve generated supplier ID.");
                }
            } else {
                System.err.println("Failed to add supplier.");
            }
        }
    }

    public boolean productExists(Integer productId) throws SQLException {
        String query = "SELECT 1 FROM products WHERE product_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

}

package com.example.ims.dto;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.models.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private final DatabaseConnection databaseConnection;

    public OrderDTO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orderList = new ArrayList<>();

        String query = "SELECT * FROM orders";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Integer order_id = resultSet.getInt("order_id");
                Integer product_id = resultSet.getInt("product_id");
                Double quantity = resultSet.getDouble("quantity");
                Double total_price = resultSet.getDouble("total_price");

                Order order = new Order(product_id, quantity, total_price);
                order.setOrder_id(order_id);

                orderList.add(order);
            }
        }

        return orderList;
    }

    public void addOrder(Order newOrder) throws SQLException {
        String query = "INSERT INTO orders (product_id, quantity, total_price) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, newOrder.getProduct_id());
            statement.setDouble(2, newOrder.getQuantity());
            statement.setDouble(3, newOrder.getTotal_price());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted <= 0) {
                System.err.println("Failed to add order.");
                return;
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (!generatedKeys.next()) {
                System.err.println("Failed to retrieve generated order ID.");
                return;
            }

            int orderId = generatedKeys.getInt(1);
            System.out.println("Order added successfully with id: " + orderId);
            newOrder.setOrder_id(orderId);
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

    public Double getProductQuantityOfStock(Integer productId) throws SQLException{
        String query = "SELECT quantity_of_stock FROM products WHERE product_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                } else {
                    return null;
                }
            }
        }
    }

    public Double getProductPrice(Integer productId) throws SQLException {
        String query = "SELECT price FROM products WHERE product_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                } else {
                    return null;
                }
            }
        }
    }

    public void updateProductQuantityOfStockAfterOrder(Double newQuantityAfterParches, Integer productId) throws SQLException {
        String query = "UPDATE products SET quantity_of_stock=? WHERE product_id=? ";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setDouble(1, newQuantityAfterParches);
            statement.setInt(2, productId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product quantity_of_stock updated successfully.");
            } else {
                System.err.println("Failed to update product quantity_of_stock.");
            }
        }
    }

    public void updateOrder(Order updatedOrder, int orderIdentificationNumber) throws SQLException {
        String query = "UPDATE orders SET product_id=?, quantity=?, total_price=? WHERE order_id=?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, updatedOrder.getProduct_id());
            statement.setDouble(2, updatedOrder.getQuantity());
            statement.setDouble(3, updatedOrder.getTotal_price());
            statement.setInt(4, orderIdentificationNumber);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                updatedOrder.setOrder_id(orderIdentificationNumber);
                System.out.println("Order updated successfully.");
            } else {
                System.err.println("Failed to update order.");
            }
        }
    }

    public void deleteOrder(int orderIdentificationNumber) throws SQLException {
        String query = "DELETE FROM orders WHERE order_id=?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, orderIdentificationNumber);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Order deleted successfully.");
            } else {
                System.err.println("Failed to delete order.");
            }
        }
    }
}

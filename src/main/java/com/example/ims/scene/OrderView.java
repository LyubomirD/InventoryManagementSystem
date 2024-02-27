package com.example.ims.scene;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.dto.OrderDTO;

import com.example.ims.dto.SupplierDTO;
import com.example.ims.models.Order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class OrderView implements Initializable {

    @FXML
    private TextField orderQuantity;
    @FXML
    private TextField orderProductId;

    @FXML
    private Button orderAdd;
    @FXML
    private Button orderUpdate;
    @FXML
    private Button orderDelete;
    @FXML
    private Button orderClear;


    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private TableColumn<Order, Integer> orderColumnId;
    @FXML
    private TableColumn<Order, Integer> orderColumnProductId;
    @FXML
    private TableColumn<Order, Double> orderColumnQuantity;
    @FXML
    private TableColumn<Order, Double> orderColumnTotalPrice;

    @FXML
    private TextField orderSearchField;
    @FXML
    private Label orderErrorMessageLabel;


    private final ObservableList<Order> orderList = FXCollections.observableArrayList();
    private final DatabaseConnection databaseConnection;
    private final OrderDTO orderDTO;
    private final FilteredList<Order> filteredData;

    public OrderView() {
        databaseConnection = new DatabaseConnection();
        orderDTO = new OrderDTO(databaseConnection);
        filteredData = new FilteredList<>(orderList, p -> true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderAdd.setOnAction(this::handleButtonClickAdd);

        orderColumnId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        orderColumnProductId.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        orderColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderColumnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total_price"));

        loadDataFromDatabase();
    }

    private Double calculateTotalPrice(Double quantity, Integer productId) {
        try {
            Double productQuantityOfStock = orderDTO.getProductQuantityOfStock(productId);
            Double productPrice = orderDTO.getProductPrice(productId);

            if (productQuantityOfStock == null && productPrice == null) {
                orderErrorMessageLabel.setText("Product with ID " + productId + " not found.");
                orderErrorMessageLabel.setVisible(true);
                return null;
            }

            Double newProductQuantityOfStockMinusQuantity = productQuantityOfStock - quantity;
            orderDTO.updateProductQuantityOfStockAfterOrder(newProductQuantityOfStockMinusQuantity, productId);

            return productPrice * quantity;
        } catch (SQLException e) {
            System.err.println("Error calculating total price: " + e.getMessage());
            return null;
        }
    }

    private void handleButtonClickAdd(ActionEvent event) {
        System.out.println("Add Button is clicked");

        Integer productId = Integer.parseInt(orderProductId.getText());
        Double quantityOrder = Double.parseDouble(orderQuantity.getText());
        Double totalPriceOfOrder = calculateTotalPrice(quantityOrder, productId);

        Order newOrder = new Order(productId, quantityOrder, totalPriceOfOrder);

        try {
            if (orderDTO.productExists(productId)) {
                orderErrorMessageLabel.setVisible(false);


                orderDTO.addOrder(newOrder);
                orderList.add(newOrder);
                orderTableView.setItems(orderList);
                System.out.println("Order added to database successfully.");
            } else {
                orderErrorMessageLabel.setText("Product with ID " + productId + " not found.");
                orderErrorMessageLabel.setVisible(true);
            }
        } catch (SQLException e) {
            System.err.println("Error adding newOrder to database: " + e.getMessage());
        }
    }

    private void loadDataFromDatabase() {
        try {
            List<Order> orders = orderDTO.getAllOrders();
            orderList.addAll(orders);
            orderTableView.setItems(filteredData);
        } catch (SQLException e) {
            System.err.println("Error loading data from database: " + e.getMessage());
        }
    }

}

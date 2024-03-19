package com.example.ims.scene;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.dto.OrderDTO;

import com.example.ims.dto.SupplierDTO;
import com.example.ims.models.Order;

import com.example.ims.models.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
        orderUpdate.setOnAction(this::handleButtonClickUpdate);
        orderDelete.setOnAction(this::handleButtonClickDelete);
        orderClear.setOnAction(this::clearTextFields);
        selectRowInTheTableView();

        orderSearchField.setOnKeyPressed(this::setSearchWhenButtonEnterIsPressed);

        orderColumnId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        orderColumnProductId.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        orderColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderColumnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total_price"));

        loadDataFromDatabase();
    }

    private Double calculateTotalPrice(Double newQuantity, Integer productId) {
        try {
            Double productQuantityOfStock = orderDTO.getProductQuantityOfStock(productId);
            Double productPrice = orderDTO.getProductPrice(productId);

            if (productQuantityOfStock == null && productPrice == null) {
                orderErrorMessageLabel.setText("Product with ID " + productId + " not found.");
                orderErrorMessageLabel.setVisible(true);
                return null;
            }

            return productPrice * newQuantity;
        } catch (SQLException e) {
            System.err.println("Error calculating total price: " + e.getMessage());
            return null;
        }
    }

    private boolean calculateOrderQuantityAndStockQuantity(Double previousQuantity, Double newQuantity, Integer productId) {
        try {
            Double productQuantityOfStock = orderDTO.getProductQuantityOfStock(productId);

            if (productQuantityOfStock == null) {
                orderErrorMessageLabel.setText("Product with ID " + productId + " not found.");
                orderErrorMessageLabel.setVisible(true);
                return false;
            }

            double difference = newQuantity - previousQuantity;
            double productFinalQuantityOfStock = productQuantityOfStock - difference;

            if (productFinalQuantityOfStock < 0) {
                orderErrorMessageLabel.setText("Product with ID " + productId + " quantity in stock is less than the wanted amount.");
                orderErrorMessageLabel.setVisible(true);
                return false;
            }

            orderDTO.updateProductQuantityInStock(productFinalQuantityOfStock, productId);
            return true;
        } catch (SQLException e) {
            System.err.println("Error calculating total quantity: " + e.getMessage());
        }
        return false;
    }

    private void handleButtonClickAdd(ActionEvent event) {
        System.out.println("Add Button is clicked");

        Integer productId = Integer.parseInt(orderProductId.getText());
        Double quantityOrder = Double.parseDouble(orderQuantity.getText());
        Double totalPriceOfOrder = calculateTotalPrice(quantityOrder, productId);

        boolean isOrderPossibleWithExistingProductQuantity = calculateOrderQuantityAndStockQuantity(0.0, quantityOrder, productId);

        if (!isOrderPossibleWithExistingProductQuantity) {
            return;
        }

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

    private void handleButtonClickUpdate(ActionEvent event) {
        System.out.println("Update Button is clicked");

        Order selectedOrder = orderTableView.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            return;
        }

        int orderIdentificationNumber = selectedOrder.getOrder_id();
        double orderPreviousQuantity = selectedOrder.getQuantity();

        Integer productId = Integer.parseInt(orderProductId.getText());
        Double newQuantityOrder = Double.parseDouble(orderQuantity.getText());
        Double totalPriceOfOrder = calculateTotalPrice(newQuantityOrder, productId);

        Order updatedOrder = new Order(productId, newQuantityOrder, totalPriceOfOrder);
        try {
            orderDTO.updateOrder(updatedOrder, orderIdentificationNumber);
            System.out.println("Order updated successfully.");
            int selectedIndex = orderTableView.getSelectionModel().getSelectedIndex();
            orderList.set(selectedIndex, updatedOrder);

            calculateOrderQuantityAndStockQuantity(orderPreviousQuantity, newQuantityOrder, productId);
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
        }
    }

    private void handleButtonClickDelete(ActionEvent event) {
        System.out.println("Delete Button is clicked");

        Order selectedOrder = orderTableView.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            System.out.println("No order selected for deletion.");
            return;
        }

        int orderIdentificationNumber = selectedOrder.getOrder_id();
        int productId = selectedOrder.getProduct_id();
        double orderPreviousQuantity = selectedOrder.getQuantity();

        try {
            orderDTO.deleteOrder(orderIdentificationNumber);
            orderList.remove(selectedOrder);
            calculateOrderQuantityAndStockQuantity(orderPreviousQuantity, 0.0, productId);
            System.out.println("Order deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
        }
    }

    private void selectRowInTheTableView() {
        orderTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            if (newSelection != null) {
                orderQuantity.setText(String.valueOf(newSelection.getQuantity()));
                orderProductId.setText(String.valueOf(newSelection.getProduct_id()));
            } else {
                clearTextFields();
            }
        });
    }

    private void clearTextFields(ActionEvent event) {
        clearTextFields();
    }

    private void clearTextFields() {
        orderQuantity.clear();
        orderProductId.clear();
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

    private void setSearchWhenButtonEnterIsPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            searchSupplier();
        }
    }

    private void searchSupplier() {
        String searchText = orderSearchField.getText().toLowerCase();

        filteredData.setPredicate(order -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return order.getOrder_id().toString().contains(searchText)
                    || order.getProduct_id().toString().contains(searchText)
                    || order.getQuantity().toString().contains(searchText)
                    || order.getTotal_price().toString().contains(searchText);
        });

        orderTableView.setItems(filteredData);
    }
}

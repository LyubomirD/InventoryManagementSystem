package com.example.ims.scene;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.dto.ProductDTO;
import com.example.ims.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ProductView implements Initializable {

    @FXML
    private TextField productName;
    @FXML
    private TextField productDescription;
    @FXML
    private TextField productQuantityOfStock;
    @FXML
    private TextField productPrice;

    @FXML
    private Button productAdd;
    @FXML
    private Button productUpdate;
    @FXML
    private Button productDelete;

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> productColumnId;
    @FXML
    private TableColumn<Product, String> productColumnName;
    @FXML
    private TableColumn<Product, String> productColumnDesc;
    @FXML
    private TableColumn<Product, Double> productColumnQuantity;
    @FXML
    private TableColumn<Product, Double> productColumnPrice;

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productAdd.setOnAction(this::handleButtonClickAdd);

        productColumnId.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        productColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productColumnDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        productColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantityOfStock"));
        productColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadDataFromDatabase();
    }

    @FXML
    private void handleButtonClickAdd(ActionEvent event) {
        System.out.println("Button is clicked");
        Product product = new Product(productName.getText(), productDescription.getText(), Double.parseDouble(productQuantityOfStock.getText()), Double.parseDouble(productPrice.getText()));

        productList.add(product);
        productTableView.setItems(productList);

        DatabaseConnection databaseConnection = new DatabaseConnection();
        ProductDTO productDTO = new ProductDTO(databaseConnection);
        try {
            productDTO.addProduct(product);
            System.out.println("Product added to database successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding product to database: " + e.getMessage());
        }
    }

    private void loadDataFromDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        ProductDTO productDTO = new ProductDTO(databaseConnection);
        try {
            List<Product> products = productDTO.getAllProducts();
            productList.addAll(products);
            productTableView.setItems(productList);
        } catch (SQLException e) {
            System.err.println("Error loading data from database: " + e.getMessage());
        }
    }
}

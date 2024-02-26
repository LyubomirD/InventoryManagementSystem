package com.example.ims.scene;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.dto.ProductDTO;
import com.example.ims.dto.SupplierDTO;
import com.example.ims.models.Product;
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

public class SupplierView implements Initializable {

    @FXML
    private TextField supplierName;
    @FXML
    private TextField supplierContInf;
    @FXML
    private TextField supplierProductId;

    @FXML
    private Button supplierAdd;
    @FXML
    private Button supplierUpdate;
    @FXML
    private Button supplierDelete;
    @FXML
    private Button supplierClear;


    @FXML
    private TableView<Supplier> supplierTableView;
    @FXML
    private TableColumn<Supplier, Integer> supplierColumnId;
    @FXML
    private TableColumn<Supplier, String> supplierColumnName;
    @FXML
    private TableColumn<Supplier, String> supplierColumnConfInf;
    @FXML
    private TableColumn<Supplier, Integer> supplierColumnProductId;
    @FXML
    private Label errorMessageLabel;

    @FXML
    private TextField supplierSearchField;


    private final ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
    private final DatabaseConnection databaseConnection;
    private final SupplierDTO supplierDTO;
    private final FilteredList<Supplier> filteredData;


    public SupplierView() {
        databaseConnection = new DatabaseConnection();
        supplierDTO = new SupplierDTO(databaseConnection);
        filteredData = new FilteredList<>(supplierList, p -> true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supplierAdd.setOnAction(this::handleButtonClickAdd);
//        supplierUpdate.setOnAction(this::handleButtonClickUpdate);
//        supplierDelete.setOnAction(this::handleButtonClickDelete);
//        supplierClear.setOnAction(this::clearTextFields);
//        selectRowInTheTableView();

      //  supplierSearchField.setOnKeyPressed(this::setSearchWhenButtonEnterIsPressed);

        supplierColumnId.setCellValueFactory(new PropertyValueFactory<>("supplier_id"));
        supplierColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        supplierColumnConfInf.setCellValueFactory(new PropertyValueFactory<>("contact_inf"));
        supplierColumnProductId.setCellValueFactory(new PropertyValueFactory<>("product_id"));

        loadDataFromDatabase();
    }


    private void handleButtonClickAdd(ActionEvent event) {
        System.out.println("Add Button is clicked");
        Integer productId = Integer.parseInt(supplierProductId.getText());
        Supplier newSupplier = new Supplier(supplierName.getText(), supplierContInf.getText(), productId);


        try {
            if (supplierDTO.productExists(productId)) {
                errorMessageLabel.setVisible(false);

                supplierDTO.addSupplier(newSupplier);
                supplierList.add(newSupplier);
                supplierTableView.setItems(supplierList);
                System.out.println("Supplier added to database successfully.");
            } else {
                errorMessageLabel.setText("Product with ID " + productId + " not found.");
                errorMessageLabel.setVisible(true);
            }
        } catch (SQLException e) {
            System.err.println("Error adding newSupplier to database: " + e.getMessage());
        }
    }
//
//    private void handleButtonClickUpdate(ActionEvent event) {
//        System.out.println("Update Button is clicked");
//
//        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
//
//        if (selectedProduct == null) {
//            return;
//        }
//
//        int productIdentificationNumber = selectedProduct.getProduct_id();
//
//        Product updatedProduct = new Product(productName.getText(), productDescription.getText(), Double.parseDouble(productQuantityOfStock.getText()), Double.parseDouble(productPrice.getText()));
//        try {
//            supplierDTO.updateProduct(updatedProduct, productIdentificationNumber);
//            System.out.println("Product updated successfully.");
//            int selectedIndex = productTableView.getSelectionModel().getSelectedIndex();
//            productList.set(selectedIndex, updatedProduct);
//        } catch (SQLException e) {
//            System.err.println("Error updating product: " + e.getMessage());
//        }
//    }
//
//    private void handleButtonClickDelete(ActionEvent event) {
//        System.out.println("Delete Button is clicked");
//
//        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
//
//        if (selectedProduct == null) {
//            System.out.println("No product selected for deletion.");
//            return;
//        }
//
//        int productIdentificationNumber = selectedProduct.getProduct_id();
//
//        try {
//            supplierDTO.deleteProduct(productIdentificationNumber);
//            productList.remove(selectedProduct);
//            System.out.println("Product deleted successfully.");
//        } catch (SQLException e) {
//            System.err.println("Error deleting product: " + e.getMessage());
//        }
//    }
//
//    private void selectRowInTheTableView() {
//        productTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//
//            if (newSelection != null) {
//                productName.setText(newSelection.getName());
//                productDescription.setText(newSelection.getDescription());
//                productQuantityOfStock.setText(String.valueOf(newSelection.getQuantityOfStock()));
//                productPrice.setText(String.valueOf(newSelection.getPrice()));
//            } else {
//                clearTextFields();
//            }
//        });
//    }
//
//    private void clearTextFields(ActionEvent event) {
//        clearTextFields();
//    }
//
//    private void clearTextFields() {
//        productName.clear();
//        productDescription.clear();
//        productQuantityOfStock.clear();
//        productPrice.clear();
//    }
//

    private void loadDataFromDatabase() {

        try {
            List<Supplier> suppliers = supplierDTO.getAllSuppliers();
            supplierList.addAll(suppliers);
            supplierTableView.setItems(supplierList);
        } catch (SQLException e) {
            System.err.println("Error loading data from database: " + e.getMessage());
        }
    }

//    private void setSearchWhenButtonEnterIsPressed(KeyEvent keyEvent) {
//        if (keyEvent.getCode() == KeyCode.ENTER) {
//            searchProducts();
//        }
//    }
//
//    private void searchProducts() {
//        String searchText = supplierSearchField.getText().toLowerCase();
//
//        filteredData.setPredicate(product -> {
//            if (searchText.isEmpty()) {
//                return true;
//            }
//
//            return product.getProduct_id().toString().toLowerCase().contains(searchText)
//                    || product.getName().toLowerCase().contains(searchText)
//                    || product.getDescription().toLowerCase().contains(searchText)
//                    || String.valueOf(product.getQuantityOfStock()).contains(searchText)
//                    || String.valueOf(product.getPrice()).contains(searchText);
//        });
//
//        supplierTableView.setItems(filteredData);
//    }
}


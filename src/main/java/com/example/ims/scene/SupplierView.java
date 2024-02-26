package com.example.ims.scene;

import com.example.ims.db_connection.DatabaseConnection;
import com.example.ims.dto.SupplierDTO;
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
        supplierUpdate.setOnAction(this::handleButtonClickUpdate);
        supplierDelete.setOnAction(this::handleButtonClickDelete);
        supplierClear.setOnAction(this::clearTextFields);
        selectRowInTheTableView();

        supplierSearchField.setOnKeyPressed(this::setSearchWhenButtonEnterIsPressed);

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
            if (supplierDTO.isSupplierExistingAlready(newSupplier)) {
                System.out.println("Supplier already existing in database.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error adding newSupplier to database: " + e.getMessage());
        }

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

    private void handleButtonClickUpdate(ActionEvent event) {
        System.out.println("Update Button is clicked");

        Supplier selectedSupplier = supplierTableView.getSelectionModel().getSelectedItem();

        if (selectedSupplier == null) {
            return;
        }

        int supplierIdentificationNumber = selectedSupplier.getSupplier_id();

        Integer productId = Integer.parseInt(supplierProductId.getText());
        Supplier updatedSupplier = new Supplier(supplierName.getText(), supplierContInf.getText(), productId);
        try {
            supplierDTO.updateSupplier(updatedSupplier, supplierIdentificationNumber);
            System.out.println("Supplier updated successfully.");
            int selectedIndex = supplierTableView.getSelectionModel().getSelectedIndex();
            supplierList.set(selectedIndex, updatedSupplier);
        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
        }
    }


    private void handleButtonClickDelete(ActionEvent event) {
        System.out.println("Delete Button is clicked");

        Supplier selectedSupplier = supplierTableView.getSelectionModel().getSelectedItem();

        if (selectedSupplier == null) {
            System.out.println("No supplier selected for deletion.");
            return;
        }

        int productIdentificationNumber = selectedSupplier.getProduct_id();

        try {
            supplierDTO.deleteSupplier(productIdentificationNumber);
            supplierList.remove(selectedSupplier);
            System.out.println("Product deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }

    private void selectRowInTheTableView() {
        supplierTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            if (newSelection != null) {
                supplierName.setText(newSelection.getName());
                supplierContInf.setText(newSelection.getContact_inf());
                supplierProductId.setText(String.valueOf(newSelection.getProduct_id()));
            } else {
                clearTextFields();
            }
        });
    }

    private void clearTextFields(ActionEvent event) {
        clearTextFields();
    }

    private void clearTextFields() {
        supplierName.clear();
        supplierContInf.clear();
        supplierProductId.clear();
    }


    private void loadDataFromDatabase() {

        try {
            List<Supplier> suppliers = supplierDTO.getAllSuppliers();
            supplierList.addAll(suppliers);
            supplierTableView.setItems(supplierList);
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
        String searchText = supplierSearchField.getText().toLowerCase();

        filteredData.setPredicate(supplier -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return supplier.getSupplier_id().toString().contains(searchText)
                    || supplier.getName().toLowerCase().contains(searchText)
                    || supplier.getContact_inf().toLowerCase().contains(searchText)
                    || supplier.getProduct_id().toString().contains(searchText);
        });

        supplierTableView.setItems(filteredData);
    }
}


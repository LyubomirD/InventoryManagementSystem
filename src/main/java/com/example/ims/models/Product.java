package com.example.ims.models;

public class Product {

    private Integer product_id;
    private String name;
    private String description;
    private Double quantityOfStock;
    private Double price;

    public Product(String name, String description, Double quantityOfStock, Double price) {
        this.name = name;
        this.description = description;
        this.quantityOfStock = quantityOfStock;
        this.price = price;
    }
    
    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getQuantityOfStock() {
        return quantityOfStock;
    }

    public void setQuantityOfStock(double quantityOfStock) {
        this.quantityOfStock = quantityOfStock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

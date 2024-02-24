package com.example.ims.models;

public class Product {

    private Long product_id;
    private String name;
    private String description;
    private double quantityOfStock;
    private double price;

    public Product(String name, String description, double quantityOfStock, double price) {
        this.name = name;
        this.description = description;
        this.quantityOfStock = quantityOfStock;
        this.price = price;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
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

    public double getQuantityOfStock() {
        return quantityOfStock;
    }

    public void setQuantityOfStock(double quantityOfStock) {
        this.quantityOfStock = quantityOfStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

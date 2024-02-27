package com.example.ims.models;

public class Order {

    private Integer order_id;
    private Integer product_id;
    private Double quantity;
    private Double total_price;

    public Order(Integer product_id, Double quantity, Double total_price) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.total_price = total_price;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }
}

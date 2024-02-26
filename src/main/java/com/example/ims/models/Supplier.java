package com.example.ims.models;

public class Supplier {

    private Integer supplier_id;
    private String name;
    private String contact_inf;
    private Integer product_id;

    public Supplier(String name, String contact_inf, Integer product_id) {
        this.name = name;
        this.contact_inf = contact_inf;
        this.product_id = product_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Integer supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_inf() {
        return contact_inf;
    }

    public void setContact_inf(String contact_inf) {
        this.contact_inf = contact_inf;
    }
}

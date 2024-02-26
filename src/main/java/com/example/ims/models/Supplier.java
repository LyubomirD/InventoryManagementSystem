package com.example.ims.models;

public class Supplier {

    private Integer supplier_id;
    private String name;
    private String contactInf;
    private Integer phoneNumber;

    public Supplier(Integer supplier_id, String name, String contactInf, int phoneNumber) {
        this.supplier_id = supplier_id;
        this.name = name;
        this.contactInf = contactInf;
        this.phoneNumber = phoneNumber;
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

    public String getContactInf() {
        return contactInf;
    }

    public void setContactInf(String contactInf) {
        this.contactInf = contactInf;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

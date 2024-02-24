package com.example.ims.models;

import java.time.LocalDateTime;

public class Order {

    private LocalDateTime dateTime;

    public Order(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}

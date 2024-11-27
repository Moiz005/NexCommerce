package com.example.sda_project.model;

import java.util.Date;

public class Orders {
    private int order_id = -1;
    private int customer_id;
    private Date order_date;
    private int DeliveryBoy_id;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public int getDeliveryBoy_id() {
        return DeliveryBoy_id;
    }

    public void setDeliveryBoy_id(int deliveryBoy_id) {
        DeliveryBoy_id = deliveryBoy_id;
    }
}

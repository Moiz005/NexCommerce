package com.example.sda_project.model;

public class Review {
    private int review_id;
    private int customer_id;
    private int order_id;
    private int DeliveryBoy_id;
    private String review;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getDeliveryBoy_id() {
        return DeliveryBoy_id;
    }

    public void setDeliveryBoy_id(int deliveryBoy_id) {
        DeliveryBoy_id = deliveryBoy_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }
}

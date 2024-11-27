package com.example.sda_project.model;

public class DeliveryBoy implements User {
    private int deliveryBoy_id = -1;
    private String username;
    private int orderCount = 0;
    private String Address;
    private String Available = "Available";

    public DeliveryBoy(){

    }

    public DeliveryBoy(int id, String uname, String Address){
        this.deliveryBoy_id = id;
        this.username = uname;
        this.Address = Address;
    }

    @Override
    public String getAvailability() {
        return Available;
    }

    @Override
    public void setAvailability(String availability) {
        Available = availability;
    }

    @Override
    public String getAddress() {
        return Address;
    }

    @Override
    public int getID(){
        return deliveryBoy_id;
    }

    @Override
    public String getUserName(){
        return username;
    }

    public int getOrderCount() {
        return orderCount;
    }

    @Override
    public void setID(int id){
        deliveryBoy_id=id;
    }

    @Override
    public void setUsername(String uname){
        username=uname;
    }

    @Override
    public void setAddress(String address) {
        Address = address;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
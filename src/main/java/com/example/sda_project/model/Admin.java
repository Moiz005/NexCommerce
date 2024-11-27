package com.example.sda_project.model;

public class Admin implements User {
    private int customer_id;
    private String username;
    private String Address;

    @Override
    public String getAddress() {
        return Address;
    }

    @Override
    public int getID(){
        return customer_id;
    }

    @Override
    public String getUserName(){
        return username;
    }

    @Override
    public String getAvailability(){
        return "";
    }

    @Override
    public void setID(int id){
        customer_id=id;
    }

    @Override
    public void setUsername(String uname){
        username=uname;
    }

    @Override
    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public void setAvailability(String availability){}
}
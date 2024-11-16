package com.example.sda_project;

public class Customer {
    private int customer_id;
    private String username;

    public int getID(){
        return customer_id;
    }

    public String getUserName(){
        return username;
    }

    public void setID(int id){
        customer_id=id;
    }

    public void setUsername(String uname){
        username=uname;
    }
}
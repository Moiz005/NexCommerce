package com.example.sda_project.model;

public interface User {
    public String getAddress();
    public int getID();
    public String getUserName();
    public String getAvailability();
    public void setAddress(String address);
    public void setID(int id);
    public void setUsername(String uname);
    public void setAvailability(String availability);
}
package com.example.sda_project;

public class Product {
    private String name;
    private double price;
    private String description;
    private String imageUrl; // URL for the product image
    public int quantity;

    public Product(String name, double price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        quantity = 1;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int q){
        quantity = q;
    }
}
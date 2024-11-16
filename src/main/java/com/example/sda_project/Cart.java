package com.example.sda_project;

import com.example.sda_project.Product;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products = new ArrayList<Product>();

    public void addProducts(Product product){
        if(products.contains(product)){
            products.get(products.indexOf(product)).quantity+=1;
            return;
        }
        products.add(product);
    }

    public void setProducts(List<Product> products){
        this.products = products;
    }

    public List<Product> getProducts(){
        return products;
    }
}
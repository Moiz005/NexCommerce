package com.example.sda_project.model;

import java.util.ArrayList;
import java.util.List;

public class WishList {
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

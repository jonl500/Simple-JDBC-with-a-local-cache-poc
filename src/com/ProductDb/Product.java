package com.ProductDb;

import java.util.Random;

public class Product implements Comparable<Product> {
private String name;
private double price;
private int stock;
private int id;
private boolean isUnique = true;

public Product(String name, double price, int stock, int id) {
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.id = id;
}



protected void generateUniqueId() {
    // Generate a unique ID for the product
    // You can use a library like UUID or a database
    Random random = new Random();
    if ((this.id != 0)  && (this.isUnique == true)) {
        generateUniqueId();
    }
    this.id = random.nextInt(10000000, 99999999);
}

    public boolean isUnique() {
        return isUnique;
    }

    public boolean assertUniqueId() {
    // Check if the generated ID is unique
    // You can use a database or a library to check if the ID is unique
    // Return true if the ID is unique, false otherwise
    this.isUnique = !this.isUnique;
    return this.isUnique;
}
@Override
public int compareTo(Product other) {
    return this.id == other.getId() ? 0 : -1;
}

public void setStock(int stock) {
    this.stock = stock;
}
public String getName() {
    return name;
}
public double getPrice() {
    return price;
}
public int getStock() {
    return stock;
}
public int getId() {
    return id;
}

@Override
public String toString(){
   String product = this.name + " " + this.price + " " + this.stock + " " + this.id;

   return product;
}
}

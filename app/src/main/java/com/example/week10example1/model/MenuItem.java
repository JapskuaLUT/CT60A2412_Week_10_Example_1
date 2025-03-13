package com.example.week10example1.model;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private int categoryId;
    private String imageFileName;

    // Default constructor for JSON deserialization
    public MenuItem() {
    }

    public MenuItem(int id, String name, String description, double price, int categoryId, String imageFileName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.imageFileName = imageFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    // Format price as string with currency symbol
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }
}

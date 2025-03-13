package com.example.week10example1.model;

public class Restaurant {
    private String name;
    private String description;

    // Default constructor for JSON deserialization
    public Restaurant() {
    }

    public Restaurant(String name, String description) {
        this.name = name;
        this.description = description;
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
}

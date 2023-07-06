package com.example.foodapp.Model;

public class ProductSearch {
    int id;
    String name;
    String userSearch;

    public ProductSearch() {
    }

    public ProductSearch(int id, String name, String userSearch) {
        this.id = id;
        this.name = name;
        this.userSearch = userSearch;
    }

    public String getUserSearch() {
        return userSearch;
    }

    public void setUserSearch(String userSearch) {
        this.userSearch = userSearch;
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
}

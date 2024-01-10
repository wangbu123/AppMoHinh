package com.example.appmohinh.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {
    private int id;
    private String name;
    private String img;
    private int price;
    private int priceSale;
    private String describe;
    private Type typeFood;
    private int status;

    public Product() {
    }

    public Product(int id, String name, String img, int price, int priceSale, String describe, Type typeFood, int status) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.price = price;
        this.priceSale = priceSale;
        this.describe = describe;
        this.typeFood = typeFood;
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(int priceSale) {
        this.priceSale = priceSale;
    }


    public Type getTypeFood() {
        return typeFood;
    }

    public void setTypeFood(Type typeFood) {
        this.typeFood = typeFood;
    }

    public Map<String, Object> updateProduct() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("img", img);
        result.put("price", price);
        result.put("priceSale", priceSale);
        result.put("status", status);
        result.put("typeFood", typeFood);
        result.put("describe", describe);

        return result;
    }
}

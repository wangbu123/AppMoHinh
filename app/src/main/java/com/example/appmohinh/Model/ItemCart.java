package com.example.appmohinh.Model;

import java.util.HashMap;
import java.util.Map;

public class ItemCart {
    private int id;
    private String name;
    private int price;
    private String img;
    private int quantity;
    private int idProduct;

    public ItemCart() {
    }

    public ItemCart(int id, String name, int price, String img, int quantity, int idProduct) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.img = img;
        this.quantity = quantity;
        this.idProduct = idProduct;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Map<String, Object> updateQuantity(){
        HashMap<String,Object> resutl = new HashMap<>();
        resutl.put("id",id);
        resutl.put("name",name);
        resutl.put("price",price);
        resutl.put("img",img);
        resutl.put("quantity",quantity);
        resutl.put("idProduct",idProduct);

        return resutl;
    }
}

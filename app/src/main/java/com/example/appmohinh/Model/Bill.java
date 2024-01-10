package com.example.appmohinh.Model;

import java.util.HashMap;
import java.util.Map;

public class Bill {
    private int id;
    private String name;
    private String phoneNumber;
    private String address;
    private String menu;
    private String date;
    private String sumPrice;
    private int status;
    private String idUser;

    public Bill() {
    }

    public Bill(int id, String name, String phoneNumber, String address, String menu, String date, String sumPrice, int status, String idUser) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.menu = menu;
        this.date = date;
        this.sumPrice = sumPrice;
        this.status = status;
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Map<String, Object> updateStatus() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("phoneNumber", phoneNumber);
        result.put("address", address);
        result.put("menu", menu);
        result.put("date", date);
        result.put("sumPrice", sumPrice);
        result.put("status", status);
        result.put("idUser", idUser);

        return result;
    }
}

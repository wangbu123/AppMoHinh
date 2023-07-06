package com.example.foodapp.Model;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Voucher {
    private int id;
    private String discountVoucher;
    private int quantity;
    private String timeVoucher;

    public Voucher() {
    }

    public Voucher(int id, String discountVoucher, int quantity, String timeVoucher) {
        this.id = id;
        this.discountVoucher = discountVoucher;
        this.quantity = quantity;
        this.timeVoucher = timeVoucher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscountVoucher() {
        return discountVoucher;
    }

    public void setDiscountVoucher(String discountVoucher) {
        this.discountVoucher = discountVoucher;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTimeVoucher() {
        return timeVoucher;
    }

    public void setTimeVoucher(String timeVoucher) {
        this.timeVoucher = timeVoucher;
    }

    public Map<String, Object> updateQuantity(){
        HashMap<String,Object> resutl = new HashMap<>();
        resutl.put("quantity",quantity);

        return resutl;
    }



}

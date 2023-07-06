package com.example.foodapp.Model;

public class VoucherUser {
    private int id;
    private int idVoucher;
    private String discountVoucher;
    private int quantity;
    private String timeVoucher;
    private String nameUser;

    public VoucherUser() {
    }

    public VoucherUser(int id, int idVoucher, String discountVoucher, int quantity, String timeVoucher, String nameUser) {
        this.id = id;
        this.idVoucher = idVoucher;
        this.discountVoucher = discountVoucher;
        this.quantity = quantity;
        this.timeVoucher = timeVoucher;
        this.nameUser = nameUser;
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

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public int getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(int idVoucher) {
        this.idVoucher = idVoucher;
    }
}

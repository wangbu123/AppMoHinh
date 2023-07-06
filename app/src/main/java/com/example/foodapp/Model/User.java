package com.example.foodapp.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private int id;
    private String phoneNumber;
    private String name;
    private String img;
    private String phoneNumber2;
    private String birthday;
    private String passwd;
    private String idUser;


    public User() {
    }

    public User(int id, String phoneNumber, String name, String img, String phoneNumber2, String birthday, String passwd, String idUser) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.img = img;
        this.phoneNumber2 = phoneNumber2;
        this.birthday = birthday;
        this.passwd = passwd;
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Map<String, Object> updateProfile() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("phoneNumber", phoneNumber);
        result.put("phoneNumber2", phoneNumber2);
        result.put("passwd", passwd);
        result.put("birthday", birthday);
        result.put("img", img);
        return result;
    }
}

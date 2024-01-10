package com.example.appmohinh.Model;

import java.io.Serializable;

public class Type implements Serializable {
    private int id;
    private String img;
    private String name;

    public Type() {
    }

    public Type(int id, String img, String name) {
        this.id = id;
        this.img = img;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

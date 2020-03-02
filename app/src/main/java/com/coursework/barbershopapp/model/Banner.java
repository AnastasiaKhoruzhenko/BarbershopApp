package com.coursework.barbershopapp.model;

public class Banner {

    private String img, name, text;

    public Banner(){
    }

    public Banner(String img, String name, String text) {
        this.img = img;
        this.name = name;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

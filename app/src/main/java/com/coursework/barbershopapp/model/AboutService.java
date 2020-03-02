package com.coursework.barbershopapp.model;

public class AboutService {

    private String price, time, title, descr;

    public AboutService(){}

    public AboutService(String price, String time, String title, String descr) {
        this.price = price;
        this.time = time;
        this.title = title;
        this.descr = descr;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String  price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String  time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}

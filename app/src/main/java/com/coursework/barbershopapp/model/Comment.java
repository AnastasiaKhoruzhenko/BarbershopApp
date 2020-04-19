package com.coursework.barbershopapp.model;

public class Comment {

    private String comment, rating, customerEmail, name, surname, id;

    public Comment() {
    }

    public Comment(String comment, String rating, String customerEmail, String name, String surname, String id) {
        this.comment = comment;
        this.rating = rating;
        this.customerEmail = customerEmail;
        this.name = name;
        this.surname = surname;
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.coursework.barbershopapp.model;

public class User {

    private String email, name, surname, phone, birth, score;

    public User(){}

    public User(String email, String name, String surname, String phone, String birth, String score) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.birth = birth;
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

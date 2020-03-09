package com.coursework.barbershopapp.model;

public class Person {

    String email, name, surname, phone, score;
    Boolean defaultPass;

    public Person(){}

    public Person(String email, String name, String surname, String phone, String score, Boolean defaultPass) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.score = score;
        this.defaultPass = defaultPass;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Boolean getDefaultPass() {
        return defaultPass;
    }

    public void setDefaultPass(Boolean defaultPass) {
        this.defaultPass = defaultPass;
    }
}

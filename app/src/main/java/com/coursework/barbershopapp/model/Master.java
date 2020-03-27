package com.coursework.barbershopapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

public class Master implements Parcelable {

    String email, name, surname, phone, score;
    Boolean defaultPass;
    List<String> services;

    public Master(){}

    public Master(String email, String name, String surname, String phone, String score, Boolean defaultPass, List<String> services) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.score = score;
        this.defaultPass = defaultPass;
        this.services = services;
    }

    public Master(String email, String name, String surname, String phone, String score, Boolean defaultPass) {
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

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    protected Master(Parcel in) {
        email = in.readString();
        name = in.readString();
        surname = in.readString();
        phone = in.readString();
        score = in.readString();
        byte tmpDefaultPass = in.readByte();
        defaultPass = tmpDefaultPass == 0 ? null : tmpDefaultPass == 1;
        services = in.createStringArrayList();
    }

    public static final Creator<Master> CREATOR = new Creator<Master>() {
        @Override
        public Master createFromParcel(Parcel in) {
            return new Master(in);
        }

        @Override
        public Master[] newArray(int size) {
            return new Master[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(phone);
        dest.writeString(score);
        dest.writeByte((byte) (defaultPass == null ? 0 : defaultPass ? 1 : 2));
        dest.writeStringList(services);
    }
}

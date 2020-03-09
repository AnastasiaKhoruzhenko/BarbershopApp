package com.coursework.barbershopapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Banner implements Parcelable {

    private String img, name, text;

    public Banner(){
    }

    public Banner(String img, String name, String text) {
        this.img = img;
        this.name = name;
        this.text = text;
    }

    protected Banner(Parcel in) {
        img = in.readString();
        name = in.readString();
        text = in.readString();
    }

    public static final Creator<Banner> CREATOR = new Creator<Banner>() {
        @Override
        public Banner createFromParcel(Parcel in) {
            return new Banner(in);
        }

        @Override
        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(name);
        dest.writeString(text);
    }
}

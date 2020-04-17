package com.coursework.barbershopapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Banner implements Parcelable {

    private String name, text, textEN;

    public Banner(){
    }

    public Banner(String name, String text, String textEN) {
        this.name = name;
        this.text = text;
        this.textEN = textEN;
    }

    protected Banner(Parcel in) {
        name = in.readString();
        text = in.readString();
        textEN = in.readString();
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

    public String getTextEN() {
        return textEN;
    }

    public void setTextEN(String textEN) {
        this.textEN = textEN;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(text);
        dest.writeString(textEN);
    }
}

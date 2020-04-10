package com.coursework.barbershopapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AboutService implements Parcelable {

    private String price, time, title, descr, id, titleEN, descrEN;

    public AboutService(){}

    public AboutService(String price, String time, String title, String descr, String id, String titleEN, String descrEN) {
        this.price = price;
        this.time = time;
        this.title = title;
        this.descr = descr;
        this.id = id;
        this.titleEN = titleEN;
        this.descrEN = descrEN;
    }

    protected AboutService(Parcel in) {
        price = in.readString();
        time = in.readString();
        title = in.readString();
        descr = in.readString();
        id = in.readString();
        titleEN = in.readString();
        descrEN = in.readString();
    }

    public static final Creator<AboutService> CREATOR = new Creator<AboutService>() {
        @Override
        public AboutService createFromParcel(Parcel in) {
            return new AboutService(in);
        }

        @Override
        public AboutService[] newArray(int size) {
            return new AboutService[size];
        }
    };

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getDescrEN() {
        return descrEN;
    }

    public void setDescrEN(String descrEN) {
        this.descrEN = descrEN;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(price);
        dest.writeString(time);
        dest.writeString(title);
        dest.writeString(descr);
        dest.writeString(id);
        dest.writeString(titleEN);
        dest.writeString(descrEN);
    }
}

package com.coursework.barbershopapp.model;

public class Upload {

    private String mName;
    private String mImageUrl;

    public Upload(){}

    public Upload(String name, String url)
    {
        if(name.trim().equals("")){
            name = "No name";
        }
        this.mName = name;
        this.mImageUrl = url;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}

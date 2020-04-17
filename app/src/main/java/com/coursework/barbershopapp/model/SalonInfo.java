package com.coursework.barbershopapp.model;

public class SalonInfo {

    private String address, definition, phone;

    public SalonInfo() {
    }

    public SalonInfo(String address, String definition, String phone) {
        this.address = address;
        this.definition = definition;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

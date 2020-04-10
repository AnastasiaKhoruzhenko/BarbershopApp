package com.coursework.barbershopapp.model;

public class SalonInfo {

    private String address, definition;

    public SalonInfo() {
    }

    public SalonInfo(String address, String definition) {
        this.address = address;
        this.definition = definition;
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
}

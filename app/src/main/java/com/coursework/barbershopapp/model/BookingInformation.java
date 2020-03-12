package com.coursework.barbershopapp.model;

public class BookingInformation {

    String customerName, customerSurname, customerPhone, barberEmail, barberName, barberSurname, barberPhone, time, date, dateId, service, rating;
    Long slot, price;

    public BookingInformation() {
    }

    public BookingInformation(String customerName, String customerSurname, String customerPhone, String barberEmail, String barberName, String barberSurname, String barberPhone, String time, String date,String dateId, String service, Long slot, String rating, Long price) {
        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.customerPhone = customerPhone;
        this.barberEmail = barberEmail;
        this.barberName = barberName;
        this.barberSurname = barberSurname;
        this.barberPhone = barberPhone;
        this.time = time;
        this.date = date;
        this.dateId = dateId;
        this.service = service;
        this.slot = slot;
        this.rating = rating;
        this.price = price;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getBarberEmail() {
        return barberEmail;
    }

    public void setBarberEmail(String barberEmail) {
        this.barberEmail = barberEmail;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getBarberSurname() {
        return barberSurname;
    }

    public void setBarberSurname(String barberSurname) {
        this.barberSurname = barberSurname;
    }

    public String getBarberPhone() {
        return barberPhone;
    }

    public void setBarberPhone(String barberPhone) {
        this.barberPhone = barberPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public String  getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }
}

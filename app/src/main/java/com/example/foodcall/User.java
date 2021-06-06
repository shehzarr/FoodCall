package com.example.foodcall;

import com.j256.ormlite.field.DatabaseField;

public class User {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    String UID;
    @DatabaseField
    String name;
    @DatabaseField
    String phone;
    @DatabaseField
    String address;
    @DatabaseField
    String city;
    @DatabaseField
    Boolean customer;
    @DatabaseField
    String image_header;

    public User() {

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getImage_header() {
        return image_header;
    }

    public void setImage_header(String image_header) {
        this.image_header = image_header;
    }

    public User(String UID, String name, String phone, String address, String city, Boolean customer, String image_header) {
        this.UID = UID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.customer = customer;
        this.image_header = image_header;
    }

    public User(String UID, String name, String phone, String address, String city, Boolean customer) {
        this.UID = UID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.customer = customer;
    }

    public User(String name, String phone, String address, String city, Boolean customer) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public Boolean getCustomer() {
        return customer;
    }

    public void setCustomer(Boolean temp) {
        this.customer = temp;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", UID='" + UID + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", customer=" + customer +
                '}';
    }
}

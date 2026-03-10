package com.example.tracks;

import android.os.Parcel;

import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    private String address;
    private String photo;
    private String password;
    private ArrayList<String> favorites;

    // Firebase يستخدم هذا constructor
    public User() {
        favorites = new ArrayList<>();
    }

    public User(String firstName, String lastName, String username,
                String phone, String address, String photo) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.photo = photo;

        this.favorites = new ArrayList<>();
    }

    public User(Parcel in) {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }///8989
    public ArrayList<String> getFavorites() {
        if (favorites == null) favorites = new ArrayList<>();
        return favorites;
    }//
    public void setFavorites(ArrayList<String> favorites) {
        this.favorites = favorites;
    }
    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", Photo='" + photo + '\'' +
                '}';
    }
    //88
}
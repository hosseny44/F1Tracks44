package com.example.tracks;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {

    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    private String address;
    private String photo;
    private String password;
    private ArrayList<String> favorites;

    // Firebase يحتاج هذا الـ constructor
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

    // Parcelable
    protected User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        username = in.readString();
        phone = in.readString();
        address = in.readString();
        photo = in.readString();
        password = in.readString();
        favorites = in.createStringArrayList();
        if (favorites == null) favorites = new ArrayList<>();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public ArrayList<String> getFavorites() {
        if (favorites == null) favorites = new ArrayList<>();
        return favorites;
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.favorites = favorites != null ? favorites : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", photo='" + photo + '\'' +
                ", favorites=" + favorites +
                '}';
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(username);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(photo);
        dest.writeString(password);
        dest.writeStringList(favorites);
    }
}
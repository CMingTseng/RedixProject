package com.booxtown.model;


import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class User implements Serializable{

    @Expose
    private String first_name;
    @Expose
    private String last_name;
    @Expose
    private String username;
    @Expose
    private String email;
    @Expose
    private String birthday;
    @Expose
    private String phone;
    @Expose
    private String password;
    @Expose
    private String session_id;
    @Expose
    private int user_id;
    @Expose
    private String photo;
    @Expose
    private float rating;
    @Expose
    private int contributor;
    @Expose
    private int goldenBook;
    @Expose
    private int listBook;

    @Expose
    private int is_birthday;

    public User(String first_name, String last_name, String username, String email, String birthday, String phone, String password, String session_id, int user_id, String photo, float rating, int contributor, int goldenBook, int listBook) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.phone = phone;
        this.password = password;
        this.session_id = session_id;
        this.user_id = user_id;
        this.photo = photo;
        this.rating = rating;
        this.contributor = contributor;
        this.goldenBook = goldenBook;
        this.listBook = listBook;
    }

    public int getIs_birthday() {
        return is_birthday;
    }

    public void setIs_birthday(int is_birthday) {
        this.is_birthday = is_birthday;
    }

    public int getContributor() {
        return contributor;
    }

    public void setContributor(int contributor) {
        this.contributor = contributor;
    }

    public int getGoldenBook() {
        return goldenBook;
    }

    public void setGoldenBook(int goldenBook) {
        this.goldenBook = goldenBook;
    }

    public int getListBook() {
        return listBook;
    }

    public void setListBook(int listBook) {
        this.listBook = listBook;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public User() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }


}

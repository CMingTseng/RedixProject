package com.booxtown.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by vietp on 04/09/2016.
 */
public class Book implements Serializable{

    @Expose
    private String title;
    @Expose
    private String author;
    @Expose
    private String photo;
    @Expose
    private String hash_tag;
    @Expose
    private float location_longitude;
    @Expose
    private float location_latitude;
    @Expose
    private String genre;
    @Expose
    private String b_condition;
    @Expose
    private String b_action;
    @Expose
    private float price;
    @Expose
    private String id;
    @Expose
    private String username;
    @Expose
    private String user_id;
    @Expose
    private float rating;
    @Expose
    private String user_photo;

    @Expose
    private String first_name;

    @Expose
    private String last_name;


    private String create_date;

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }


    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public Book(String title, String author, String photo, String hash_tag, float location_longitude, float location_latitude, String genre, String b_condition, String b_action, float price, String id, String username, String user_id, float rating, String create_date) {
        this.title = title;
        this.author = author;
        this.photo = photo;
        this.hash_tag = hash_tag;
        this.location_longitude = location_longitude;
        this.location_latitude = location_latitude;
        this.genre = genre;
        this.b_condition = b_condition;
        this.b_action = b_action;
        this.price = price;
        this.id = id;
        this.username = username;
        this.user_id = user_id;
        this.rating = rating;
        this.create_date = create_date;
    }

    public Book(String id, String title, String author, String photo, String hash_tag, float location_longitude, float location_latitude, String genre, String b_condition, String b_action,
                float price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.photo = photo;
        this.hash_tag = hash_tag;
        this.location_longitude = location_longitude;
        this.location_latitude = location_latitude;
        this.genre = genre;
        this.b_condition = b_condition;
        this.b_action = b_action;
        this.price = price;
    }

    public Book() {
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHash_tag() {
        return hash_tag;
    }

    public void setHash_tag(String hash_tag) {
        this.hash_tag = hash_tag;
    }

    public float getLocation_longitude() {
        return location_longitude;
    }

    public void setLocation_longitude(float location_longitude) {
        this.location_longitude = location_longitude;
    }

    public float getLocation_latitude() {
        return location_latitude;
    }

    public void setLocation_latitude(float location_latidude) {
        this.location_latitude = location_latidude;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCondition() {
        return b_condition;
    }

    public void setCondition(String condition) {
        this.b_condition = condition;
    }

    public String getAction() {
        return b_action;
    }

    public void setAction(String action) {
        this.b_action = action;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", photo='" + photo + '\'' +
                ", hash_tag='" + hash_tag + '\'' +
                ", location_longitude=" + location_longitude +
                ", location_latitude=" + location_latitude +
                ", genre='" + genre + '\'' +
                ", b_condition='" + b_condition + '\'' +
                ", b_action='" + b_action + '\'' +
                ", price='" + price + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public static Comparator<Book> priceasen = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            int value1 = (int) Math.round(lhs.getPrice());
            int value2 =  (int)Math.round(rhs.getPrice());
            return value1 - value2;
        }
    };

    public static Comparator<Book> pricedcen = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            int value1 = (int) Math.round(lhs.getPrice());
            int value2 =  (int)Math.round(rhs.getPrice());
            return value2 - value1;
        }
    };

    public static Comparator<Book> recently = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            String date[] = lhs.getCreate_date().split("-");
            String date1[] = rhs.getCreate_date().split("-");
            int dt1 = Integer.valueOf(date[0]+date[1]+date[2].substring(0,2));
            int dt2 = Integer.valueOf(date1[0]+date1[1]+date1[2].substring(0,2));

            return dt2 - dt1;
        }
    };

    public static Comparator<Book> asid = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            int dt1 = Integer.parseInt(lhs.getId());
            int dt2 = Integer.parseInt(rhs.getId());
            return dt1 - dt2;
        }
    };

}

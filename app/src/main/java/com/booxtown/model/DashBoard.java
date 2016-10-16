package com.booxtown.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by thuyetpham94 on 01/10/2016.
 */
public class DashBoard implements Serializable{
    @Expose
    private int id;
    @Expose
    private int user_buyer_id;
    @Expose
    private int user_seller_id;
    @Expose
    private String create_date;
    @Expose
    private String book_buyer_id;
    @Expose
    private int book_seller_id;
    @Expose
    private String action;
    @Expose
    private int is_accept;
    @Expose
    private int is_reject;
    @Expose
    private String notification_id;
    @Expose
    private int is_cancel;
    @Expose
    private float user_promp;

    private float user_cour_seller;

    private float user_promp_seller;

    public float getUser_cour_seller() {
        return user_cour_seller;
    }

    public void setUser_cour_seller(float user_cour_seller) {
        this.user_cour_seller = user_cour_seller;
    }

    public float getUser_promp_seller() {
        return user_promp_seller;
    }

    public void setUser_promp_seller(float user_promp_seller) {
        this.user_promp_seller = user_promp_seller;
    }

    public float getUser_quality_seller() {
        return user_quality_seller;
    }

    public void setUser_quality_seller(float user_quality_seller) {
        this.user_quality_seller = user_quality_seller;
    }

    private float user_quality_seller;
    @Expose
    private float user_cour;
    @Expose
    private float user_quality;
    @Expose
    private int book_swap_id;
    @Expose
    private String book_seller;
    @Expose
    private float rating;
    @Expose
    private String author;



    public DashBoard(int id, int user_buyer_id, int user_seller_id, String create_date, String book_buyer_id, int book_seller_id, String action, int is_accept, int is_reject, String notification_id, int is_cancel, float user_promp, float user_cour, float user_quality, int book_swap_id, String book_seller, float rating, String author) {
        this.id = id;
        this.user_buyer_id = user_buyer_id;
        this.user_seller_id = user_seller_id;
        this.create_date = create_date;
        this.book_buyer_id = book_buyer_id;
        this.book_seller_id = book_seller_id;
        this.action = action;
        this.is_accept = is_accept;
        this.is_reject = is_reject;
        this.notification_id = notification_id;
        this.is_cancel = is_cancel;
        this.user_promp = user_promp;
        this.user_cour = user_cour;
        this.user_quality = user_quality;
        this.book_swap_id = book_swap_id;
        this.book_seller = book_seller;
        this.rating = rating;
        this.author = author;
    }

    public float getUser_promp() {
        return user_promp;
    }

    public void setUser_promp(float user_promp) {
        this.user_promp = user_promp;
    }

    public float getUser_cour() {
        return user_cour;
    }

    public void setUser_cour(float user_cour) {
        this.user_cour = user_cour;
    }

    public float getUser_quality() {
        return user_quality;
    }

    public void setUser_quality(float user_quality) {
        this.user_quality = user_quality;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_buyer_id() {
        return user_buyer_id;
    }

    public void setUser_buyer_id(int user_buyer_id) {
        this.user_buyer_id = user_buyer_id;
    }

    public int getUser_seller_id() {
        return user_seller_id;
    }

    public void setUser_seller_id(int user_seller_id) {
        this.user_seller_id = user_seller_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getBook_buyer_id() {
        return book_buyer_id;
    }

    public void setBook_buyer_id(String book_buyer_id) {
        this.book_buyer_id = book_buyer_id;
    }

    public int getBook_seller_id() {
        return book_seller_id;
    }

    public void setBook_seller_id(int book_seller_id) {
        this.book_seller_id = book_seller_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getIs_accept() {
        return is_accept;
    }

    public void setIs_accept(int is_accept) {
        this.is_accept = is_accept;
    }

    public int getIs_reject() {
        return is_reject;
    }

    public void setIs_reject(int is_reject) {
        this.is_reject = is_reject;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }



    public void setUser_quality(int user_quality) {
        this.user_quality = user_quality;
    }

    public int getBook_swap_id() {
        return book_swap_id;
    }

    public void setBook_swap_id(int book_swap_id) {
        this.book_swap_id = book_swap_id;
    }

    public String getBook_seller() {
        return book_seller;
    }

    public void setBook_seller(String book_seller) {
        this.book_seller = book_seller;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

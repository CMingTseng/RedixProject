package com.booxtown.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 30/09/2016.
 */
public class Transaction  implements Serializable {
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
    private int notification_id;
    @Expose
    private int is_cancel;
    @Expose
    private int user_promp;
    @Expose
    private int user_cour;
    @Expose
    private int user_quality;
    @Expose
    private String book_name;

    @Expose
    private String book_author;

    @Expose
    private String user_buy;

    @Expose
    private String firstNameUserBuy;

    @Expose
    private String firstNameUserSell;

    @Expose
    private String user_sell;

    @Expose
    private String book_price;
    @Expose
    private String book_swap_id;
    @Expose
    private String session_user_buy;
    @Expose
    private String seller_time_start;
    @Expose
    private String session_user_sell;
    @Expose
    private String seller_time_to;

    @Expose
    private List<Book> book = new ArrayList<Book>();

    public Transaction() {
    }

    public Transaction(int id, int user_buyer_id, int user_seller_id, String create_date, String book_buyer_id, int book_seller_id, String action, int is_accept, int is_reject, int notification_id, int is_cancel, int user_promp, int user_cour, int user_quality, String book_name, String book_author, String user_buy, String firstNameUserBuy, String firstNameUserSell, String user_sell, String book_price, String book_swap_id, String session_user_buy, String seller_time_start, String session_user_sell, String seller_time_to, List<Book> book) {
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
        this.book_name = book_name;
        this.book_author = book_author;
        this.user_buy = user_buy;
        this.firstNameUserBuy = firstNameUserBuy;
        this.firstNameUserSell = firstNameUserSell;
        this.user_sell = user_sell;
        this.book_price = book_price;
        this.book_swap_id = book_swap_id;
        this.session_user_buy = session_user_buy;
        this.seller_time_start = seller_time_start;
        this.session_user_sell = session_user_sell;
        this.seller_time_to = seller_time_to;
        this.book = book;
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

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }

    public int getUser_promp() {
        return user_promp;
    }

    public void setUser_promp(int user_promp) {
        this.user_promp = user_promp;
    }

    public int getUser_cour() {
        return user_cour;
    }

    public void setUser_cour(int user_cour) {
        this.user_cour = user_cour;
    }

    public int getUser_quality() {
        return user_quality;
    }

    public void setUser_quality(int user_quality) {
        this.user_quality = user_quality;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getUser_buy() {
        return user_buy;
    }

    public void setUser_buy(String user_buy) {
        this.user_buy = user_buy;
    }

    public String getFirstNameUserBuy() {
        return firstNameUserBuy;
    }

    public void setFirstNameUserBuy(String firstNameUserBuy) {
        this.firstNameUserBuy = firstNameUserBuy;
    }

    public String getFirstNameUserSell() {
        return firstNameUserSell;
    }

    public void setFirstNameUserSell(String firstNameUserSell) {
        this.firstNameUserSell = firstNameUserSell;
    }

    public String getUser_sell() {
        return user_sell;
    }

    public void setUser_sell(String user_sell) {
        this.user_sell = user_sell;
    }

    public String getBook_price() {
        return book_price;
    }

    public void setBook_price(String book_price) {
        this.book_price = book_price;
    }

    public String getBook_swap_id() {
        return book_swap_id;
    }

    public void setBook_swap_id(String book_swap_id) {
        this.book_swap_id = book_swap_id;
    }

    public String getSession_user_buy() {
        return session_user_buy;
    }

    public void setSession_user_buy(String session_user_buy) {
        this.session_user_buy = session_user_buy;
    }

    public String getSeller_time_start() {
        return seller_time_start;
    }

    public void setSeller_time_start(String seller_time_start) {
        this.seller_time_start = seller_time_start;
    }

    public String getSession_user_sell() {
        return session_user_sell;
    }

    public void setSession_user_sell(String session_user_sell) {
        this.session_user_sell = session_user_sell;
    }

    public String getSeller_time_to() {
        return seller_time_to;
    }

    public void setSeller_time_to(String seller_time_to) {
        this.seller_time_to = seller_time_to;
    }

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }
}

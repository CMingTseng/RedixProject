package redix.booxtown.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 30/09/2016.
 */
public class Transaction {
    @Expose
    private int id;
    @Expose
    private int user_buyer_id;
    @Expose
    private int user_seller_id;
    @Expose
    private String create_date;
    @Expose
    private int book_buyer_id;
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
    private List<Book> book = new ArrayList<Book>();

    public Transaction() {
    }

    public Transaction(int id, int user_buyer_id, int user_seller_id, String create_date, int book_buyer_id, int book_seller_id, String action, int is_accept, int is_reject, int notification_id, int is_cancel, int user_promp, int user_cour, int user_quality, List<Book> book) {
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

    public int getBook_buyer_id() {
        return book_buyer_id;
    }

    public void setBook_buyer_id(int book_buyer_id) {
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

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }
}

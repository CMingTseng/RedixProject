package redix.booxtown.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by thuyetpham94 on 27/09/2016.
 */
public class Wishboard implements Serializable{
    @Expose
    private String title;
    @Expose
    private String author;
    @Expose
    private String comment;
    @Expose
    private String create_date;
    @Expose
    private int user_id;
    @Expose
    private String username;
    @Expose
    private String id;
    @Expose
    private String photo;


    public Wishboard(String title, String author, String comment, String create_date, int user_id, String username) {
        this.title = title;
        this.author = author;
        this.comment = comment;
        this.create_date = create_date;
        this.user_id = user_id;
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

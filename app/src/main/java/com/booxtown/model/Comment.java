package com.booxtown.model;

import com.google.gson.annotations.Expose;

import java.util.Comparator;

/**
 * Created by thuyetpham94 on 17/09/2016.
 */
public class Comment {
    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private String create_date;
    @Expose
    private String thread_id;
    @Expose
    private String user_id;
    @Expose
    private String username;
    @Expose
    private float rating;
    @Expose
    private String photo;
    @Expose
    private int contributor;
    @Expose
    private int goldenBook;
    @Expose
    private int listBook;

    public Comment(String id, String content, String create_date, String thread_id, String user_id, String username, float rating, String photo, int contributor, int goldenBook, int listBook) {
        this.id = id;
        this.content = content;
        this.create_date = create_date;
        this.thread_id = thread_id;
        this.user_id = user_id;
        this.username = username;
        this.rating = rating;
        this.photo = photo;
        this.contributor = contributor;
        this.goldenBook = goldenBook;
        this.listBook = listBook;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static Comparator<Comment> aseid = new Comparator<Comment>() {
        @Override
        public int compare(Comment lhs, Comment rhs) {
            int dt1 = Integer.parseInt(lhs.getId());
            int dt2 = Integer.parseInt(rhs.getId());
            return dt1 - dt2;
        }
    };
}

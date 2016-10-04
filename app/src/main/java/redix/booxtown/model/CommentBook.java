package redix.booxtown.model;

import com.google.gson.annotations.Expose;

/**
 * Created by thuyetpham94 on 01/10/2016.
 */
public class CommentBook {
    @Expose
    private int id;
    @Expose
    private String content;
    @Expose
    private String create_date;
    @Expose
    private String username;
    @Expose
    private int user_id;
    @Expose
    private int book_id;
    @Expose
    private int post_id;
    @Expose
    private float rating;
    @Expose
    private String photo;

    public CommentBook(int id, String content, String create_date, String username, int user_id, int book_id, float rating, String photo) {
        this.id = id;
        this.content = content;
        this.create_date = create_date;
        this.username = username;
        this.user_id = user_id;
        this.book_id = book_id;
        this.rating = rating;
        this.photo = photo;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
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
}

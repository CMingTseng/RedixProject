package com.booxtown.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by thuyetpham94 on 16/09/2016.
 */
public class Thread implements Serializable {
    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private String description;
    @Expose
    private String create_date;
    @Expose
    private String topic_id;
    @Expose
    private String user_id;
    @Expose
    private String full_name;
    @Expose
    private int is_read;
    @Expose
    private String username;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getNum_comment() {
        return num_comment;
    }

    public void setNum_comment(int num_comment) {
        this.num_comment = num_comment;
    }

    @Expose
    private int num_comment;


    public Thread(String id, String title, String description, String create_date, String topic_id, String user_id, int is_read, String username, int num_comment) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.create_date = create_date;
        this.topic_id = topic_id;
        this.user_id = user_id;
        this.is_read = is_read;
        this.username = username;
        this.num_comment = num_comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Thread() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public static Comparator<Thread> aseid = new Comparator<Thread>() {
        @Override
        public int compare(Thread lhs, Thread rhs) {
            int dt1 = Integer.parseInt(lhs.getId());
            int dt2 = Integer.parseInt(rhs.getId());
            return dt1 - dt2;
        }
    };
}

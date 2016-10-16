package com.booxtown.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by duong on 9/16/2016.
 */
public class Topic implements Serializable {
            public String id;
            public String title;
            public String description;
            public String create_date;
            public int is_expire;
            public String user_id;
            public int num_thread;
            public int is_read;
    public int getNum_thread() {
        return num_thread;
    }

    public void setNum_thread(int num_thread) {
        this.num_thread = num_thread;
    }



    public Topic() {
    }

    public Topic(String id, String title, String description, String create_date, int is_expire, String user_id, int num_thread, int is_read) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.create_date = create_date;
        this.is_expire = is_expire;
        this.user_id = user_id;
        this.num_thread = num_thread;
        this.is_read=is_read;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
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

    public int getIs_expire() {
        return is_expire;
    }

    public void setIs_expire(int is_expire) {
        this.is_expire = is_expire;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static Comparator<Topic> aseid  = new Comparator<Topic>() {
        @Override
        public int compare(Topic lhs, Topic rhs) {
            int dt1 = Integer.parseInt(lhs.getId());
            int dt2 = Integer.parseInt(rhs.getId());
            return dt1 - dt2;
        }
    };
}

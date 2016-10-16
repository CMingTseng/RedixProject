package com.booxtown.model;

import com.google.gson.annotations.Expose;

import java.util.Comparator;

/**
 * Created by Administrator on 26/09/2016.
 */
public class Notification {
    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private String title_notification;
    @Expose
    private String key_screen;
    @Expose
    private String id_screen;
    @Expose
    private int is_read;

    private String create_date;

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
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

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getTitle_notifi() {
        return title_notification;
    }

    public void setTitle_notifi(String title_notifi) {
        this.title_notification = title_notifi;
    }

    public Notification(String id, String content, String title_notifi, String key_screen, int is_read) {
        this.id = id;
        this.content = content;
        this.title_notification = title_notifi;
        this.key_screen = key_screen;
        this.is_read = is_read;
    }

    public Notification(String title_notifi, String key_screen,String id_screen) {
        this.id_screen=id_screen;
        this.title_notification = title_notifi;
        this.key_screen = key_screen;
    }

    public String getKey_screen() {
        return key_screen;
    }

    public void setKey_screen(String key_screen) {
        this.key_screen = key_screen;
    }

    public Notification() {
    }

    public  static Comparator<Notification> aseid = new Comparator<Notification>() {
        @Override
        public int compare(Notification lhs, Notification rhs) {
            int dt1 = Integer.parseInt(lhs.getId());
            int dt2 = Integer.parseInt(rhs.getId());
            return dt1 - dt2;
        }
    };

    public String getId_screen() {
        return id_screen;
    }

    public void setId_screen(String id_screen) {
        this.id_screen = id_screen;
    }
}

package com.booxtown.model;

import com.google.gson.annotations.Expose;

/**
 * Created by thuyetpham94 on 28/09/2016.
 */
public class Setting {
    @Expose
    private int id;
    @Expose
    private int is_notification;
    @Expose
    private int is_best_time;
    @Expose
    private int is_current_location;
    @Expose
    private String time_start;
    @Expose
    private String time_to;
    @Expose
    private int user_id;

    private double longitude;

    private double latitude;

    public Setting(int id, int is_notification, int is_best_time, int is_current_location, String time_start, String time_to, int user_id) {
        this.id = id;
        this.is_notification = is_notification;
        this.is_best_time = is_best_time;
        this.is_current_location = is_current_location;
        this.time_start = time_start;
        this.time_to = time_to;
        this.user_id = user_id;
    }

    public Setting(int is_notification, int is_best_time, int is_current_location, String time_start, String time_to) {
        this.is_notification = is_notification;
        this.is_best_time = is_best_time;
        this.is_current_location = is_current_location;
        this.time_start = time_start;
        this.time_to = time_to;
    }

    public Setting(int id, int is_notification, int is_best_time, int is_current_location, String time_start, String time_to, int user_id, double longitude, double latitude) {
        this.id = id;
        this.is_notification = is_notification;
        this.is_best_time = is_best_time;
        this.is_current_location = is_current_location;
        this.time_start = time_start;
        this.time_to = time_to;
        this.user_id = user_id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_notification() {
        return is_notification;
    }

    public void setIs_notification(int is_notification) {
        this.is_notification = is_notification;
    }

    public int getIs_best_time() {
        return is_best_time;
    }

    public void setIs_best_time(int is_best_time) {
        this.is_best_time = is_best_time;
    }

    public int getIs_current_location() {
        return is_current_location;
    }

    public void setIs_current_location(int is_current_location) {
        this.is_current_location = is_current_location;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_to() {
        return time_to;
    }

    public void setTime_to(String time_to) {
        this.time_to = time_to;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

package com.booxtown.model;

/**
 * Created by Administrator on 14/11/2016.
 */
public class DayUsed {
    String code;
    String DayUsed;
    String is_active;
    int DayExpire;

    public int getDayExpirep() {
        return DayExpire;
    }

    public void setDayExpirep(int dayExpirep) {
        DayExpire = dayExpirep;
    }

    public DayUsed(String code, String dayUsed, String is_active, int dayExpirep) {
        this.code = code;
        DayUsed = dayUsed;
        this.is_active = is_active;
        DayExpire = dayExpirep;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDayUsed() {
        return DayUsed;
    }

    public void setDayUsed(String dayUsed) {
        DayUsed = dayUsed;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}

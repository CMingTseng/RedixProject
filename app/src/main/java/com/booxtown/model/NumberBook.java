package com.booxtown.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 16/10/2016.
 */
public class NumberBook {
    @Expose
    private String total;
    @Expose
    private String swap;
    @Expose
    private String sell;
    @Expose
    private String free;

    public NumberBook(String total, String swap, String sell, String free) {
        this.total = total;
        this.swap = swap;
        this.sell = sell;
        this.free = free;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSwap() {
        return swap;
    }

    public void setSwap(String swap) {
        this.swap = swap;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }
}

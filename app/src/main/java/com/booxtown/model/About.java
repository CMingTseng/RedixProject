package com.booxtown.model;

/**
 * Created by Administrator on 12/01/2017.
 */

public class About {
    private int id;
    private String content;
    private int status;

    public About() {
    }

    public About(int id, String content, int status) {
        this.id = id;
        this.content = content;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

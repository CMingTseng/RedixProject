package com.booxtown.model;

/**
 * Created by Administrator on 02/11/2016.
 */
public class Contact {
    private String session_id;
    private String content;

    public Contact() {
    }

    public Contact(String session_id, String content) {
        this.session_id = session_id;
        this.content = content;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.booxtown.model;

import java.util.List;

/**
 * Created by thuyetpham94 on 17/09/2016.
 */
public class CommentResult {
    private int code;
    private List<Comment> comment;

    public CommentResult(int code, List<Comment> comment) {
        this.code = code;
        this.comment = comment;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }
}

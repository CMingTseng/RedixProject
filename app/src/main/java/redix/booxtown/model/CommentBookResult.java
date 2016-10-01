package redix.booxtown.model;

import java.util.List;

/**
 * Created by thuyetpham94 on 01/10/2016.
 */
public class CommentBookResult {
    private int code;
    private List<CommentBook> comment;

    public CommentBookResult(int code, List<CommentBook> comment) {
        this.code = code;
        this.comment = comment;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<CommentBook> getComment() {
        return comment;
    }

    public void setComment(List<CommentBook> comment) {
        this.comment = comment;
    }
}

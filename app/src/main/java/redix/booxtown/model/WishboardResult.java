package redix.booxtown.model;

import java.util.List;

/**
 * Created by thuyetpham94 on 27/09/2016.
 */
public class WishboardResult {
    private int code;
    private List<Wishboard> post;

    public WishboardResult(int code, List<Wishboard> post) {
        this.code = code;
        this.post = post;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Wishboard> getList() {
        return post;
    }

    public void setList(List<Wishboard> list) {
        this.post = list;
    }
}

package redix.booxtown.model;

import java.util.List;

/**
 * Created by thuyetpham94 on 11/09/2016.
 */
public class Profile {
    private int code;
    private User user;

    public Profile(int code, User user) {
        this.code = code;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

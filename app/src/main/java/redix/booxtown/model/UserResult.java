package redix.booxtown.model;

import java.util.List;

/**
 * Created by vietp on 04/09/2016.
 */
public class UserResult {
    private int code;
    private List<User> user;

    public UserResult() {
    }

    public UserResult(int code, List<User> user) {
        this.code = code;
        this.user = user;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}

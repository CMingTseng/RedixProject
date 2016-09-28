package redix.booxtown.model;

import java.util.List;

/**
 * Created by Administrator on 27/09/2016.
 */
public class NotificationResult {
    int code;
    public List<Notification> notifi;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Notification> getNotifi() {
        return notifi;
    }

    public void setNotifi(List<Notification> topic) {
        this.notifi = topic;
    }

    public NotificationResult(int code, List<Notification> notifi) {
        this.code = code;
        this.notifi = notifi;
    }

    public NotificationResult() {
    }
}

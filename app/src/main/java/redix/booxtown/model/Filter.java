package redix.booxtown.model;

/**
 * Created by duong on 9/19/2016.
 */
public class Filter {
    public String title;
    public Boolean isCheck;

    public Filter() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }
}

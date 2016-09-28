package redix.booxtown.model;

/**
 * Created by duong on 9/9/2016.
 */
public class Genre {
    public Genre(){

    }
    public boolean ischeck;
    public String value;

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package redix.booxtown.model;

/**
 * Created by Administrator on 28/09/2016.
 */

public class BookSwap {
    public BookSwap(){

    }
    public boolean ischeck;
    public String value;
    public String book_id;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

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

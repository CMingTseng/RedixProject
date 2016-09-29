package redix.booxtown.model;

import com.google.gson.annotations.Expose;

/**
 * Created by thuyetpham94 on 29/09/2016.
 */
public class GenreValue {
    @Expose
    private int id;
    @Expose
    private String title;

    public GenreValue(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

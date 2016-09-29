package redix.booxtown.model;

import java.util.List;

/**
 * Created by thuyetpham94 on 29/09/2016.
 */
public class GenreValueResult {
    private int code;
    private List<GenreValue> genre;

    public GenreValueResult(int code, List<GenreValue> genre) {
        this.code = code;
        this.genre = genre;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<GenreValue> getGenre() {
        return genre;
    }

    public void setGenre(List<GenreValue> genre) {
        this.genre = genre;
    }
}

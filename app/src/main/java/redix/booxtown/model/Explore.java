package redix.booxtown.model;

import java.io.Serializable;

/**
 * Created by Administrator on 26/08/2016.
 */
public class Explore implements Serializable{
    private String url_img_book;
    private String title_book;
    private String author_book;
    private boolean swap;
    private boolean free;
    private boolean buy;
    private float price_book;
    public Explore(){

    }
    public Explore(String url_img_book, String title_book, String author_book, boolean swap, boolean free, boolean buy, float price_book) {
        this.url_img_book = url_img_book;
        this.title_book = title_book;
        this.author_book = author_book;
        this.swap = swap;
        this.free = free;
        this.buy = buy;
        this.price_book=price_book;
    }

    public float getPrice_book() {
        return price_book;
    }

    public void setPrice_book(float price_book) {
        this.price_book = price_book;
    }

    public void setUrl_img_book(String url_img_book) {
        this.url_img_book = url_img_book;
    }

    public void setTitle_book(String title_book) {
        this.title_book = title_book;
    }

    public void setAuthor_book(String author_book) {
        this.author_book = author_book;
    }

    public void setSwap(boolean swap) {
        this.swap = swap;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public String getUrl_img_book() {
        return url_img_book;
    }

    public String getTitle_book() {
        return title_book;
    }

    public String getAuthor_book() {
        return author_book;
    }

    public boolean isSwap() {
        return swap;
    }

    public boolean isFree() {
        return free;
    }

    public boolean isBuy() {
        return buy;
    }
}

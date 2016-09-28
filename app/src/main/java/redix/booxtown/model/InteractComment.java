package redix.booxtown.model;

/**
 * Created by Administrator on 28/08/2016.
 */
public class InteractComment {

    private float rating;
    private boolean rank_one;
    private boolean rank_two;
    private boolean rank_three;
    private  String user_name;
    private String content;
    private String date_time;

    public InteractComment() {
    }

    public InteractComment(float rating, boolean rank_one, boolean rank_two, boolean rank_three, String user_name, String content, String date_time) {

        this.rating = rating;
        this.rank_one = rank_one;
        this.rank_two = rank_two;
        this.rank_three = rank_three;
        this.user_name = user_name;
        this.content = content;
        this.date_time = date_time;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isRank_one() {
        return rank_one;
    }

    public void setRank_one(boolean rank_one) {
        this.rank_one = rank_one;
    }

    public boolean isRank_two() {
        return rank_two;
    }

    public void setRank_two(boolean rank_two) {
        this.rank_two = rank_two;
    }

    public boolean isRank_three() {
        return rank_three;
    }

    public void setRank_three(boolean rank_three) {
        this.rank_three = rank_three;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}

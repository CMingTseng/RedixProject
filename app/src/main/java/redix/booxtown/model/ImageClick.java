package redix.booxtown.model;

import android.net.Uri;

/**
 * Created by Administrator on 02/10/2016.
 */
public class ImageClick {

    private Uri uri;
    private String key;

    public ImageClick() {
    }

    public ImageClick(Uri uri, String key) {
        this.uri = uri;
        this.key = key;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

package dim.mobilecomputing.findme.models;

import android.graphics.Bitmap;

/**
 * Created by Sathindu on 2016-05-25.
 */
public class User {
    private Bitmap image;
    private String uid;
    private String name;
    private String email;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return name;
    }
}

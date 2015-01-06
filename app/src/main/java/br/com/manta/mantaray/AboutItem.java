package br.com.manta.mantaray;

import android.graphics.drawable.Drawable;

/**
 * Created by JGabrielFreitas on 05/01/15.
 */
public class AboutItem {

    private Drawable image;
    private String   title;
    private String   subText;

    public AboutItem(Drawable image, String title, String subText) {
        this.image = image;
        this.title = title;
        this.subText = subText;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }
}

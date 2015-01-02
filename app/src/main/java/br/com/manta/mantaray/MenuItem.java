package br.com.manta.mantaray;

import android.graphics.drawable.Drawable;

/**
 * Created by JGabrielFreitas on 02/01/2015 - 15:32.
 */

public class MenuItem {

    private Drawable image;
    private String   title;
    private String   subText;

    public MenuItem(Drawable image, String title, String subText) {
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

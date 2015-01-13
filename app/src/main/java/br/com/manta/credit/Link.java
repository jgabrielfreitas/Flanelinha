package br.com.manta.credit;

import android.graphics.drawable.Drawable;

/**
 * Created by JGabrielFreitas on 13/01/2015 - 10:17.
 */
public class Link {

    Drawable image;
    String linkTitle;
    String link;

    public Link(Drawable image, String link, String linkTitle) {
        this.image = image;
        this.linkTitle = linkTitle;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }


}

package org.me.socialmediaapp;

import android.location.Location;
import android.media.Image;
import android.net.Uri;

public class Post {

    private String mTitle, mDesc;
    private Location mLocation;
    private Uri mImage;

    public Post() {
    }

    public Post(String mTitle, String mDesc, Location mLocation, Uri mImage) {
        this.mTitle = mTitle;
        this.mDesc = mDesc;
        this.mLocation = mLocation;
        this.mImage = mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public Uri getImage() {
        return mImage;
    }

    public void setImage(Uri mImage) {
        this.mImage = mImage;
    }
}

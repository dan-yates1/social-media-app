package org.me.socialmediaapp;

import android.location.Location;
import android.media.Image;
import android.net.Uri;

import java.util.ArrayList;

public class Post {

    private String mTitle, mDesc;
    private Location mLocation;
    private Uri mImage;
    private User mAuthor;
    private ArrayList<String> mComments;
    private int mLikes;
    private int mCommentCount;

    public Post() {
    }

    public Post(String mTitle, String mDesc, Location mLocation, Uri mImage, User author) {
        this.mTitle = mTitle;
        this.mDesc = mDesc;
        this.mLocation = mLocation;
        this.mImage = mImage;
        this.mAuthor = author;
        this.mComments = new ArrayList<>();
        this.mLikes = 0;
        this.mCommentCount = 0;
    }

    public void addComment(String comment) {
        mComments.add(comment);
    }

    public ArrayList<String> getComments() {
        return mComments;
    }

    public void setComments(ArrayList<String> mComments) {
        this.mComments = mComments;
    }

    public User getAuthor() {
        return mAuthor;
    }

    public void setAuthor(User author) {
        this.mAuthor = author;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int mLikes) {
        this.mLikes = mLikes;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setComments(int commentCount) {
        this.mCommentCount = commentCount;
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

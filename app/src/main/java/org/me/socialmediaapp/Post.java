package org.me.socialmediaapp;

import java.util.ArrayList;

public class Post {

    private String title, desc, imgRef, authorId;
    private int likes;
    private ArrayList<Comment> comments;

    public Post() {
    }

    public Post(String title, String desc, String image, String authorId) {
        this.title = title;
        this.desc = desc;
        this.imgRef = image;
        this.authorId = authorId;
        this.likes = 0;
        this.comments = new ArrayList<Comment>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgRef() {
        return imgRef;
    }

    public void setImgRef(String imgRef) {
        this.imgRef = imgRef;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthor(String authorId) {
        this.authorId = authorId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}

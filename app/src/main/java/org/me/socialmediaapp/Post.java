package org.me.socialmediaapp;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {

    private String desc, imgRef, authorUid, postUid;
    private int likes;
    private ArrayList<Comment> comments;
    private transient Timestamp timestamp;

    public Post() {
    }

    public Post(String desc, String image, String authorUid) {
        this.desc = desc;
        this.imgRef = image;
        this.authorUid = authorUid;
        this.likes = 0;
        this.comments = new ArrayList<>();
        this.timestamp = Timestamp.now();
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

    public String getAuthorUid() {
        return authorUid;
    }

    public void setAuthor(String authorId) {
        this.authorUid = authorId;
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

    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
    }

    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

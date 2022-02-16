package org.me.socialmediaapp;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Post {

    private String desc, imgRef, authorId, postUid;
    private int likes;
    private ArrayList<Comment> comments;
    private Timestamp timestamp;

    public Post() {
    }

    public Post(String desc, String image, String authorId) {
        this.desc = desc;
        this.imgRef = image;
        this.authorId = authorId;
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

    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
    }
}

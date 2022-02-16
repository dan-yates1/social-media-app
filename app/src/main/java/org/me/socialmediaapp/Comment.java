package org.me.socialmediaapp;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Comment implements Serializable {
    private String author, comment, postUid, commentUid;
    private Timestamp timestamp;

    public Comment(String author, String comment, String postUid, String commentUid) {
        this.author = author;
        this.comment = comment;
        this.postUid = postUid;
        this.commentUid = commentUid;
        this.timestamp = Timestamp.now();
    }

    public Comment() {
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommentUid() {
        return commentUid;
    }

    public void setCommentUid(String commentUid) {
        this.commentUid = commentUid;
    }

    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String mPostUid) {
        this.postUid = mPostUid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String mAuthor) {
        this.author = mAuthor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String mComment) {
        this.comment = mComment;
    }
}

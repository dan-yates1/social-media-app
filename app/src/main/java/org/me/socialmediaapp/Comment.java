package org.me.socialmediaapp;

public class Comment {
    private User mAuthor;
    private String mComment;

    public Comment(User mAuthor, String mComment) {
        this.mAuthor = mAuthor;
        this.mComment = mComment;
    }

    public Comment() {
    }

    public User getAuthor() {
        return mAuthor;
    }

    public void setAuthor(User mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }
}

package org.me.socialmediaapp;

public class Comment {
    private User mAuthor;
    private Post mPost;
    private String mComment;

    public Comment(User mAuthor, Post mPost, String mComment) {
        this.mAuthor = mAuthor;
        this.mPost = mPost;
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

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post mPost) {
        this.mPost = mPost;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }
}

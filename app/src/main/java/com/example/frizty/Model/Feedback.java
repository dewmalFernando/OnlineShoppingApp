package com.example.frizty.Model;

public class Feedback {

    private String feedName, feedEmail, feedComment;

    public Feedback() {
    }

    public Feedback(String feedName, String feedEmail, String feedComment) {
        this.feedName = feedName;
        this.feedEmail = feedEmail;
        this.feedComment = feedComment;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedEmail() {
        return feedEmail;
    }

    public void setFeedEmail(String feedEmail) {
        this.feedEmail = feedEmail;
    }

    public String getFeedComment() {
        return feedComment;
    }

    public void setFeedComment(String feedComment) {
        this.feedComment = feedComment;
    }
}

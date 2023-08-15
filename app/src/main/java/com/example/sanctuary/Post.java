package com.example.sanctuary;

import java.util.Date;

public class Post {

    private String text;
    private String imageUrl;
    private Date timestamp;

    public Post() {
        // Default constructor required for Firestore
    }

    public Post(String text, String imageUrl, Date timestamp) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

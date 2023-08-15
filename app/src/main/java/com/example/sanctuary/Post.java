package com.example.sanctuary;
public class Post {
    private String content;
    private String imageUrl;
    private String userId;
    private long timestamp;

    public Post() {
        // Default constructor required for Firestore
    }

    public Post(String content, String imageUrl, String userId, long timestamp) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

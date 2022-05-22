package com.teamb.tourtokorea;

class FirebasePost {
    public String postContent;
    public String postTitle;
    public String userCountry;
    public String userId;
    public String key;

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public String getUserId() {
        return userId;
    }

    public String getKey() {
        return key;
    }
}

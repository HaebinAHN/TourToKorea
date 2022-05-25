package com.teamb.tourtokorea;

public class Post {
    private String postTitle;
    private String postContent;
    private String userId;
    private String ImgID;
    private String userCountry;
    private String Location;

    public Post() {}

    public Post(String postTitle, String postContent, String userId, String userCountry,String Imgurl,String Location) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.userId = userId;
        this.userCountry = userCountry;
        this.ImgID = Imgurl;
        this.Location = Location;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgID() {
        return ImgID;
    }

    public void setImgID(String imgID) {
        ImgID = imgID;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }
}

package com.vrg.sample;



public class ImageItem {
    private String userName;
    private int userPhoto;

    public ImageItem(String userName, int userPhoto) {
        this.userName = userName;
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(int userPhoto) {
        this.userPhoto = userPhoto;
    }
}

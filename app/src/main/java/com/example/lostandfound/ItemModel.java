package com.example.lostandfound;

import android.net.Uri;

public class ItemModel {
    private int id;
    private int imageResId;
    private Uri imageUri;
    private String description;
    private String location;
    private String date;
    private String phone;
    private String postedBy;
    private String userEmail;
    private String type;

    public ItemModel(Uri imageUri, String description, String location, String date, String phone) {
        this.imageResId = 0;
        this.imageUri = imageUri;
        this.description = description;
        this.location = location;
        this.date = date;
        this.phone = phone;
    }

    public ItemModel(int imageResId, String description, String location, String date, String phone) {
        this.imageResId = imageResId;
        this.imageUri = null;
        this.description = description;
        this.location = location;
        this.date = date;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.example.finalproject;

public class PropertyDetails {
    private String foodId;


    private String foodName;
    private String imageUrl;
    private String postedBy;
    private String price;
    private String number;
    private String location;
    private String ownerEmail;
    private String phoneNumber;

    public PropertyDetails(String foodId, String foodName, String imageUrl, String postedBy, String price, String number, String location, String ownerEmail, String phoneNumber) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.postedBy = postedBy;
        this.price = price;
        this.number = number;
        this.location = location;
        this.ownerEmail = ownerEmail;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public PropertyDetails() {
        //empty required constructor
    }


    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

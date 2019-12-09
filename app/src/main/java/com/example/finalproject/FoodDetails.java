package com.example.finalproject;

public class FoodDetails
{
    private String foodId;


    private String foodName;
    private String imageUrl;
    private String postedBy;
    private String price;
    private String number;

    public FoodDetails()
    {
        //empty required constructor
    }

    public FoodDetails(String foodId, String foodName, String imageUrl, String postedBy, String price, String number)
    {
        this.foodId = foodId;
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.postedBy = postedBy;
        this.price = price;
        this.number = number;
    }

    public String getFoodId()
    {
        return foodId;
    }

    public void setFoodId(String foodId)
    {
        this.foodId = foodId;
    }

    public String getFoodName()
    {
        return foodName;
    }

    public void setFoodName(String foodName)
    {
        this.foodName = foodName;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getPostedBy()
    {
        return postedBy;
    }

    public void setPostedBy(String postedBy)
    {
        this.postedBy = postedBy;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }
}

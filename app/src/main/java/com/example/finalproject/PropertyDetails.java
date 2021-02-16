package com.example.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.Collections;
import java.util.List;

public class PropertyDetails implements Parcelable {
    private String propertyId;


    private String propertyName;
    private List<String> imageUrl;
    private String postedBy;
    private String price;
    private String description;
    private String location;
    private String ownerEmail;
    private String phoneNumber;
    private GeoPoint geoPoint;
    public PropertyDetails() {
    }

    protected PropertyDetails(Parcel in) {
        propertyId = in.readString();
        propertyName = in.readString();
        imageUrl = in.createStringArrayList();
        postedBy = in.readString();
        price = in.readString();
        description = in.readString();
        location = in.readString();
        ownerEmail = in.readString();
        phoneNumber = in.readString();
        Double lat = in.readDouble();
        Double lng = in.readDouble();
        geoPoint = new GeoPoint(lat, lng);

    }

    public static final Creator<PropertyDetails> CREATOR = new Creator<PropertyDetails>() {
        @Override
        public PropertyDetails createFromParcel(Parcel in) {
            return new PropertyDetails(in);
        }

        @Override
        public PropertyDetails[] newArray(int size) {
            return new PropertyDetails[size];
        }
    };

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
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

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public PropertyDetails(String propertyId, String propertyName, List<String> imageUrl, String postedBy, String price, String description, String location, String ownerEmail, String phoneNumber, GeoPoint geoPoint) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.imageUrl = imageUrl;
        this.postedBy = postedBy;
        this.price = price;
        this.description = description;
        this.location = location;
        this.ownerEmail = ownerEmail;
        this.phoneNumber = phoneNumber;
        this.geoPoint = geoPoint;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(propertyId);
        parcel.writeString(propertyName);
        parcel.writeStringList(imageUrl);
        parcel.writeString(postedBy);
        parcel.writeString(price);
        parcel.writeString(description);
        parcel.writeString(location);
        parcel.writeString(ownerEmail);
        parcel.writeString(phoneNumber);
        parcel.writeDouble(geoPoint.getLatitude());
        parcel.writeDouble(geoPoint.getLongitude());
    }
}

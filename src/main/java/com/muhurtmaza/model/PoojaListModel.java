package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay on 17/12/15.
 */
public class PoojaListModel {


    @SerializedName("pooja_name")
    String mPooja_Name;

    @SerializedName("no_of_guruji")
    String mNo_Of_Guruji;

    @SerializedName("pooja_duration")
    String mPooja_Duration;

    @SerializedName("pooja_id")
    String mPooja_id;

    @SerializedName("image")
    String mPooja_Image_URL;

    @SerializedName("rating")
    String mRating;

    @SerializedName("booked")
    String mBooked;

    @SerializedName("views")
    String mViews;

    @SerializedName("price")
    String mPrice;

    public String getmPooja_Name() {
        return mPooja_Name;
    }

    public void setmPooja_Name(String mPooja_Name) {
        this.mPooja_Name = mPooja_Name;
    }

    public String getmNo_Of_Guruji() {
        return mNo_Of_Guruji;
    }

    public void setmNo_Of_Guruji(String mNo_Of_Guruji) {
        this.mNo_Of_Guruji = mNo_Of_Guruji;
    }

    public String getmPooja_Duration() {
        return mPooja_Duration;
    }

    public void setmPooja_Duration(String mPooja_Duration) {
        this.mPooja_Duration = mPooja_Duration;
    }

    public String getmPooja_id() {
        return mPooja_id;
    }

    public void setmPooja_id(String mPooja_id) {
        this.mPooja_id = mPooja_id;
    }

    public String getmPooja_Image_URL() {
        return mPooja_Image_URL;
    }

    public void setmPooja_Image_URL(String mPooja_Image_URL) {
        this.mPooja_Image_URL = mPooja_Image_URL;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmViews() {
        return mViews;
    }

    public void setmViews(String mViews) {
        this.mViews = mViews;
    }

    public String getmBooked() {
        return mBooked;
    }

    public void setmBooked(String mBooked) {
        this.mBooked = mBooked;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    @Override
    public String toString() {
        return "PoojaListModel{" +
                "mPooja_Name='" + mPooja_Name + '\'' +
                ", mNo_Of_Guruji='" + mNo_Of_Guruji + '\'' +
                ", mPooja_Duration='" + mPooja_Duration + '\'' +
                ", mPooja_id='" + mPooja_id + '\'' +
                ", mPooja_Image_URL='" + mPooja_Image_URL + '\'' +
                ", mRating='" + mRating + '\'' +
                ", mBooked='" + mBooked + '\'' +
                ", mViews='" + mViews + '\'' +
                ", mPrice='" + mPrice + '\'' +
                '}';
    }
}

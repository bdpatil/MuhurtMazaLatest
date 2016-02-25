package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.ui.NewPoojaDetailsActivity;

import java.io.Serializable;

/**
 * Created by imac on 18/12/15.
 */
public class NewPoojaDetailModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("pooja_name")
    String mPooja_Name;

    @SerializedName("no_of_guruji")
    String mNo_Of_Guruji;

    @SerializedName("pooja_duration")
    String mPooja_Duration;


    @SerializedName("image")
    String mPooja_Image_URL;


    @SerializedName("description")
    String mPoojaDescription;

    @SerializedName("terms")
    String mPoojaTerms;

    @SerializedName("rating")
    String mRating;

    @SerializedName("booked")
    String mBooked;

    @SerializedName("dakshina_without_samagree")
    String mDakshina_without_samagree;

    @SerializedName("dakshina_with_samagree")
    String mDakshina_with_samagree;

    @SerializedName("views")
    String mViews;

    @SerializedName("pooja_blessings")
    String mPoojaBlessings;

    @SerializedName("pooja_special_instruction")
    String mSpecialInstructions;


    public NewPoojaDetailModel(String mPooja_Name, String mNo_Of_Guruji, String mPooja_Duration, String mPooja_Image_URL, String mPoojaDescription, String mPoojaTerms, String mRating, String mBooked, String mDakshina_without_samagree, String mDakshina_with_samagree, String mViews, String mPoojaBlessings, String mSpecialInstructions) {
        this.mPooja_Name = mPooja_Name;
        this.mNo_Of_Guruji = mNo_Of_Guruji;
        this.mPooja_Duration = mPooja_Duration;
        this.mPooja_Image_URL = mPooja_Image_URL;
        this.mPoojaDescription = mPoojaDescription;
        this.mPoojaTerms = mPoojaTerms;
        this.mRating = mRating;
        this.mBooked = mBooked;
        this.mDakshina_without_samagree = mDakshina_without_samagree;
        this.mDakshina_with_samagree = mDakshina_with_samagree;
        this.mViews = mViews;
        this.mPoojaBlessings = mPoojaBlessings;
        this.mSpecialInstructions = mSpecialInstructions;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getmPooja_Image_URL() {
        return mPooja_Image_URL;
    }

    public void setmPooja_Image_URL(String mPooja_Image_URL) {
        this.mPooja_Image_URL = mPooja_Image_URL;
    }

    public String getmPoojaDescription() {
        return mPoojaDescription;
    }

    public void setmPoojaDescription(String mPoojaDescription) {
        this.mPoojaDescription = mPoojaDescription;
    }

    public String getmPoojaTerms() {
        return mPoojaTerms;
    }

    public void setmPoojaTerms(String mPoojaTerms) {
        this.mPoojaTerms = mPoojaTerms;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmBooked() {
        return mBooked;
    }

    public void setmBooked(String mBooked) {
        this.mBooked = mBooked;
    }

    public String getmDakshina_without_samagree() {
        return mDakshina_without_samagree;
    }

    public void setmDakshina_without_samagree(String mDakshina_without_samagree) {
        this.mDakshina_without_samagree = mDakshina_without_samagree;
    }

    public String getmDakshina_with_samagree() {
        return mDakshina_with_samagree;
    }

    public void setmDakshina_with_samagree(String mDakshina_with_samagree) {
        this.mDakshina_with_samagree = mDakshina_with_samagree;
    }

    public String getmViews() {
        return mViews;
    }

    public void setmViews(String mViews) {
        this.mViews = mViews;
    }

    public String getmPoojaBlessings() {
        return mPoojaBlessings;
    }

    public void setmPoojaBlessings(String mPoojaBlessings) {
        this.mPoojaBlessings = mPoojaBlessings;
    }

    public String getmSpecialInstructions() {
        return mSpecialInstructions;
    }

    public void setmSpecialInstructions(String mSpecialInstructions) {
        this.mSpecialInstructions = mSpecialInstructions;
    }

    @Override
    public String toString() {
        return "NewPoojaDetailModel{" +
                "mPooja_Name='" + mPooja_Name + '\'' +
                ", mNo_Of_Guruji='" + mNo_Of_Guruji + '\'' +
                ", mPooja_Duration='" + mPooja_Duration + '\'' +
                ", mPooja_Image_URL='" + mPooja_Image_URL + '\'' +
                ", mPoojaDescription='" + mPoojaDescription + '\'' +
                ", mPoojaTerms='" + mPoojaTerms + '\'' +
                ", mRating='" + mRating + '\'' +
                ", mBooked='" + mBooked + '\'' +
                ", mDakshina_without_samagree='" + mDakshina_without_samagree + '\'' +
                ", mDakshina_with_samagree='" + mDakshina_with_samagree + '\'' +
                ", mViews='" + mViews + '\'' +
                ", mPoojaBlessings='" + mPoojaBlessings + '\'' +
                ", mSpecialInstructions='" + mSpecialInstructions + '\'' +
                '}';
    }
}

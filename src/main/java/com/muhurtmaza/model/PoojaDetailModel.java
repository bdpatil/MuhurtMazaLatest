package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by imac on 18/12/15.
 */
public class PoojaDetailModel {

    @SerializedName("pooja_name")
    String mPooja_Name;

    @SerializedName("no_of_guruji")
    String mNo_Of_Guruji;

    @SerializedName("pooja_duration")
    String mPooja_Duration;

    @SerializedName("with_samagri_cost")
    String mPoojaWithSamagriCost;

    @SerializedName("image")
    String mPooja_Image_URL;

    @SerializedName("without_samagri_cost")
    String mPoojaWithoutSamagriCost;

    @SerializedName("description")
    String mPoojaDescription;

    @SerializedName("terms")
    String mPoojaTerms;

    public PoojaDetailModel(){
    }

    public PoojaDetailModel(String mPooja_Name, String mNo_Of_Guruji, String mPooja_Duration, String mPoojaWithSamagriCost, String mPooja_Image_URL, String mPoojaWithoutSamagriCost, String mPoojaDescription, String mPoojaTerms) {
        this.mPooja_Name = mPooja_Name;
        this.mNo_Of_Guruji = mNo_Of_Guruji;
        this.mPooja_Duration = mPooja_Duration;
        this.mPoojaWithSamagriCost = mPoojaWithSamagriCost;
        this.mPooja_Image_URL = mPooja_Image_URL;
        this.mPoojaWithoutSamagriCost = mPoojaWithoutSamagriCost;
        this.mPoojaDescription = mPoojaDescription;
        this.mPoojaTerms = mPoojaTerms;
    }

    @Override
    public String toString() {
        return "PoojaDetailModel{" +
                "mPooja_Name='" + mPooja_Name + '\'' +
                ", mNo_Of_Guruji='" + mNo_Of_Guruji + '\'' +
                ", mPooja_Duration='" + mPooja_Duration + '\'' +
                ", mPoojaWithSamagriCost='" + mPoojaWithSamagriCost + '\'' +
                ", mPooja_Image_URL='" + mPooja_Image_URL + '\'' +
                ", mPoojaWithoutSamagriCost='" + mPoojaWithoutSamagriCost + '\'' +
                ", mPoojaDescription='" + mPoojaDescription + '\'' +
                ", mPoojaTerms='" + mPoojaTerms + '\'' +
                '}';
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

    public String getmPoojaWithSamagriCost() {
        return mPoojaWithSamagriCost;
    }

    public void setmPoojaWithSamagriCost(String mPoojaWithSamagriCost) {
        this.mPoojaWithSamagriCost = mPoojaWithSamagriCost;
    }

    public String getmPooja_Image_URL() {
        return mPooja_Image_URL;
    }

    public void setmPooja_Image_URL(String mPooja_Image_URL) {
        this.mPooja_Image_URL = mPooja_Image_URL;
    }

    public String getmPoojaWithoutSamagriCost() {
        return mPoojaWithoutSamagriCost;
    }

    public void setmPoojaWithoutSamagriCost(String mPoojaWithoutSamagriCost) {
        this.mPoojaWithoutSamagriCost = mPoojaWithoutSamagriCost;
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
}

package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 26-12-2015.
 */
public class PoojaBookModel {

    @SerializedName("cust_id")
    String mCutomerId;

    @SerializedName("pooja_id")
    String mPoojaId;

    @SerializedName("current_date")
    String mCurrentDate;

    @SerializedName("current_time")
    String mCurrentTime;

    @SerializedName("date")
    String mDate;

    @SerializedName("venue")
    String mVenu;

    @SerializedName("with samagri")
    String mWithSamagri;

    @SerializedName("cost")
    String mCost;

    @SerializedName("method")
    String mMethod;

    @SerializedName("first_name")
    String mFirstName;

    @SerializedName("last_name")
    String mLastName;

    @SerializedName("language")
    String mLanguage;

    @SerializedName("email")
    String mEmail;

    @SerializedName("contact_no")
    String mContactNo;

    @SerializedName("state")
    String mState;

    @SerializedName("city")
    String mCity;

    public String getmCutomerId() {
        return mCutomerId;
    }

    public void setmCutomerId(String mCutomerId) {
        this.mCutomerId = mCutomerId;
    }

    public String getmPoojaId() {
        return mPoojaId;
    }

    public void setmPoojaId(String mPoojaId) {
        this.mPoojaId = mPoojaId;
    }

    public String getmCurrentDate() {
        return mCurrentDate;
    }

    public void setmCurrentDate(String mCurrentDate) {
        this.mCurrentDate = mCurrentDate;
    }

    public String getmCurrentTime() {
        return mCurrentTime;
    }

    public void setmCurrentTime(String mCurrentTime) {
        this.mCurrentTime = mCurrentTime;
    }

    public String getmVenu() {
        return mVenu;
    }

    public void setmVenu(String mVenu) {
        this.mVenu = mVenu;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmWithSamagri() {
        return mWithSamagri;
    }

    public void setmWithSamagri(String mWithSamagri) {
        this.mWithSamagri = mWithSamagri;
    }

    public String getmCost() {
        return mCost;
    }

    public void setmCost(String mCost) {
        this.mCost = mCost;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmMethod() {
        return mMethod;
    }

    public void setmMethod(String mMethod) {
        this.mMethod = mMethod;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmContactNo() {
        return mContactNo;
    }

    public void setmContactNo(String mContactNo) {
        this.mContactNo = mContactNo;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }


    @Override
    public String toString() {
        return "PoojaBookModel{" +
                "mCutomerId='" + mCutomerId + '\'' +
                ", mPoojaId='" + mPoojaId + '\'' +
                ", mCurrentDate='" + mCurrentDate + '\'' +
                ", mCurrentTime='" + mCurrentTime + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mVenu='" + mVenu + '\'' +
                ", mWithSamagri='" + mWithSamagri + '\'' +
                ", mCost='" + mCost + '\'' +
                ", mMethod='" + mMethod + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mLanguage='" + mLanguage + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mContactNo='" + mContactNo + '\'' +
                ", mState='" + mState + '\'' +
                ", mCity='" + mCity + '\'' +
                '}';
    }
}

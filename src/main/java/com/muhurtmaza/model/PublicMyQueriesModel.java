package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 23-12-2015.
 */
public class PublicMyQueriesModel {
    @SerializedName("bookingId")
    private String mBookingId;

    @SerializedName("booking_date")
    private String mBookingDate;

    @SerializedName("pooja_name")
    private String mPoojaName;

    @SerializedName("name")
    private String muserName;

    @SerializedName("pooja_image")
    private String mImgPath;

    @SerializedName("status")
    private String mStatus;


    public PublicMyQueriesModel() {
    }

    public PublicMyQueriesModel(String mBookingId, String mBookingDate, String mPoojaName, String muserName, String mImgPath) {
        this.mBookingId = mBookingId;
        this.mBookingDate = mBookingDate;
        this.mPoojaName = mPoojaName;
        this.muserName = muserName;
        this.mImgPath = mImgPath;
    }

    @Override
    public String toString() {
        return "BookingInfoModel{" +
                "mBookingId='" + mBookingId + '\'' +
                ", mBookingDate='" + mBookingDate + '\'' +
                ", mPoojaName='" + mPoojaName + '\'' +
                ", muserName='" + muserName + '\'' +
                ", mImgPath='" + mImgPath + '\'' +
                '}';
    }

    public String getmBookingId() {
        return mBookingId;
    }

    public void setmBookingId(String mBookingId) {
        this.mBookingId = mBookingId;
    }

    public String getmBookingDate() {
        return mBookingDate;
    }

    public void setmBookingDate(String mBookingDate) {
        this.mBookingDate = mBookingDate;
    }

    public String getmPoojaName() {
        return mPoojaName;
    }

    public void setmPoojaName(String mPoojaName) {
        this.mPoojaName = mPoojaName;
    }

    public String getMuserName() {
        return muserName;
    }

    public void setMuserName(String muserName) {
        this.muserName = muserName;
    }

    public String getmImgPath() {
        return mImgPath;
    }

    public void setmImgPath(String mImgPath) {
        this.mImgPath = mImgPath;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

}

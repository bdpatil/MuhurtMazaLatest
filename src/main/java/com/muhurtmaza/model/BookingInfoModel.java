package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sandeep on 11/22/15.
 */
public class BookingInfoModel {

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

    @SerializedName("dakshina")
    private String mDakshina;

    @SerializedName("guruji_count")
    private String mGuruji;

    @SerializedName("pooja_duration")
    private String mDuration;

    @Override
    public String toString() {
        return "BookingInfoModel{" +
                "mBookingId='" + mBookingId + '\'' +
                ", mBookingDate='" + mBookingDate + '\'' +
                ", mPoojaName='" + mPoojaName + '\'' +
                ", muserName='" + muserName + '\'' +
                ", mImgPath='" + mImgPath + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mDakshina='" + mDakshina + '\'' +
                ", mGuruji='" + mGuruji + '\'' +
                ", mDuration='" + mDuration + '\'' +
                '}';
    }

    public BookingInfoModel() {

    }

    public BookingInfoModel(String mBookingId, String mBookingDate, String mPoojaName, String muserName, String mImgPath, String mStatus, String mDakshina, String mGuruji, String mDuration) {
        this.mBookingId = mBookingId;
        this.mBookingDate = mBookingDate;
        this.mPoojaName = mPoojaName;
        this.muserName = muserName;
        this.mImgPath = mImgPath;
        this.mStatus = mStatus;
        this.mDakshina = mDakshina;
        this.mGuruji = mGuruji;
        this.mDuration = mDuration;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmGuruji() {
        return mGuruji;
    }

    public void setmGuruji(String mGuruji) {
        this.mGuruji = mGuruji;
    }

    public String getmDakshina() {
        return mDakshina;
    }

    public void setmDakshina(String mDakshina) {
        this.mDakshina = mDakshina;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmImgPath() {
        return mImgPath;
    }

    public void setmImgPath(String mImgPath) {
        this.mImgPath = mImgPath;
    }

    public String getMuserName() {
        return muserName;
    }

    public void setMuserName(String muserName) {
        this.muserName = muserName;
    }

    public String getmPoojaName() {
        return mPoojaName;
    }

    public void setmPoojaName(String mPoojaName) {
        this.mPoojaName = mPoojaName;
    }

    public String getmBookingDate() {
        return mBookingDate;
    }

    public void setmBookingDate(String mBookingDate) {
        this.mBookingDate = mBookingDate;
    }

    public String getmBookingId() {
        return mBookingId;
    }

    public void setmBookingId(String mBookingId) {
        this.mBookingId = mBookingId;
    }








}

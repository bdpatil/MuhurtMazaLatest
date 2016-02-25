package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.webservice.BaseResponse;

/**
 * Created by bharat on 11-02-2016.
 */
public class GetBookedPoojaModel {

    @SerializedName("status")
    private String mStatus;
    @SerializedName("pooja_name")
    private String mPoojaName;
    @SerializedName("booking_date")
    private String mBookingDate;
    @SerializedName("dakshina")
    private String mDakshina;
    @SerializedName("guruji_count")
    private String mGuruji;
    @SerializedName("pooja_image")
    private String mPoojaImage;
    @SerializedName("bookingId")
    private String mBookingId;
    @SerializedName("pooja_duration")
    private String mDuration;
  /*  @SerializedName("name")
    private String mName;*/

    public GetBookedPoojaModel() {

    }

    public GetBookedPoojaModel(String mStatus, String mPoojaName, String mBookingDate, String mDakshina, String mGuruji, String mPoojaImage, String mBookingId, String mDuration) {
        this.mStatus = mStatus;
        this.mPoojaName = mPoojaName;
        this.mBookingDate = mBookingDate;
        this.mDakshina = mDakshina;
        this.mGuruji = mGuruji;
        this.mPoojaImage = mPoojaImage;
        this.mBookingId = mBookingId;
        this.mDuration = mDuration;
   /*     this.mName = mName;*/
    }

    @Override
    public String toString() {
        return "GetBookedPoojaModel{" +
                "mStatus='" + mStatus + '\'' +
                ", mPoojaName='" + mPoojaName + '\'' +
                ", mBookingDate='" + mBookingDate + '\'' +
                ", mDakshina='" + mDakshina + '\'' +
                ", mGuruji='" + mGuruji + '\'' +
                ", mPoojaImage='" + mPoojaImage + '\'' +
                ", mBookingId='" + mBookingId + '\'' +
                ", mDuration='" + mDuration + '\'' +
                /*", mName='" + mName + '\''*/ +
                '}';
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
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

    public String getmDakshina() {
        return mDakshina;
    }

    public void setmDakshina(String mDakshina) {
        this.mDakshina = mDakshina;
    }

    public String getmGuruji() {
        return mGuruji;
    }

    public void setmGuruji(String mGuruji) {
        this.mGuruji = mGuruji;
    }

    public String getmPoojaImage() {
        return mPoojaImage;
    }

    public void setmPoojaImage(String mPoojaImage) {
        this.mPoojaImage = mPoojaImage;
    }

    public String getmBookingId() {
        return mBookingId;
    }

    public void setmBookingId(String mBookingId) {
        this.mBookingId = mBookingId;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

 /*   public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }*/


}

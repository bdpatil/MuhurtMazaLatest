package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.webservice.BaseResponse;

import org.json.JSONObject;

/**
 * Created by Admin on 12-02-2016.
 */
public class BookingDetailsResponse extends BaseResponse {

    @SerializedName("dakshina")
    private String mDakshina;
    @SerializedName("image")
    private String mImage;
    @SerializedName("reference_no")
    private String mRefNo;
    @SerializedName("pooja_venu")
    private String mAddress;
    @SerializedName("pooja_date")
    private String mDate;
    @SerializedName("booking_status")
    private String mStatus;
    @SerializedName("pooja_duration")
    private String mDuration;
    @SerializedName("city")
    private String mCity;
    @SerializedName("name")
    private String mUser_Name;
    @SerializedName("language")
    private String mLanguage;
    @SerializedName("no_of_guruji")
    private String mGuruji;
    @SerializedName("pooja_name")
    private String mPooja_Name;
    @SerializedName("samagri")
    private String mSamagree;
    @SerializedName("phone_no")
    private String mPhoneNo;
    @SerializedName("email")
    private String mEmail;

    public BookingDetailsResponse(){
}

    @Override
    public String toString() {
        return "BookingDetailsResponse{" +
                "mDakshina='" + mDakshina + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mRefNo='" + mRefNo + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mDuration='" + mDuration + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mUser_Name='" + mUser_Name + '\'' +
                ", mLanguage='" + mLanguage + '\'' +
                ", mGuruji='" + mGuruji + '\'' +
                ", mPooja_Name='" + mPooja_Name + '\'' +
                ", mSamagree='" + mSamagree + '\'' +
                ", mPhoneNo='" + mPhoneNo + '\'' +
                ", mEmail='" + mEmail + '\'' +
                '}';
    }

    public BookingDetailsResponse(String mEmail, String mPhoneNo, String mGuruji, String mPooja_Name, String mSamagree, String mLanguage, String mUser_Name, String mCity, String mDuration, String mStatus, String mDate, String mAddress, String mRefNo, String mImage, String mDakshina) {
        this.mEmail = mEmail;
        this.mPhoneNo = mPhoneNo;
        this.mGuruji = mGuruji;
        this.mPooja_Name = mPooja_Name;
        this.mSamagree = mSamagree;
        this.mLanguage = mLanguage;
        this.mUser_Name = mUser_Name;
        this.mCity = mCity;
        this.mDuration = mDuration;
        this.mStatus = mStatus;
        this.mDate = mDate;
        this.mAddress = mAddress;
        this.mRefNo = mRefNo;
        this.mImage = mImage;
        this.mDakshina = mDakshina;
    }



    public String getmDakshina() {
        return mDakshina;
    }

    public void setmDakshina(String mDakshina) {
        this.mDakshina = mDakshina;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmRefNo() {
        return mRefNo;
    }

    public void setmRefNo(String mRefNo) {
        this.mRefNo = mRefNo;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmUser_Name() {
        return mUser_Name;
    }

    public void setmUser_Name(String mUser_Name) {
        this.mUser_Name = mUser_Name;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmGuruji() {
        return mGuruji;
    }

    public void setmGuruji(String mGuruji) {
        this.mGuruji = mGuruji;
    }

    public String getmPooja_Name() {
        return mPooja_Name;
    }

    public void setmPooja_Name(String mPooja_Name) {
        this.mPooja_Name = mPooja_Name;
    }

    public String getmPhoneNo() {
        return mPhoneNo;
    }

    public void setmPhoneNo(String mPhoneNo) {
        this.mPhoneNo = mPhoneNo;
    }

    public String getmSamagree() {
        return mSamagree;
    }

    public void setmSamagree(String mSamagree) {
        this.mSamagree = mSamagree;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }


    public static BookingDetailsResponse fromJson(String pResult)  {
        try {

            JSONObject objJson= new JSONObject(pResult);

            BookingDetailsResponse obj=new BookingDetailsResponse();

            JSONObject user_info=objJson.getJSONObject("nameList");
            //JSONObject basic_info=user_info.getJSONObject("basic");
            // obj.setmUser_id(basic_info.getString("user_id"));

            Gson gson = new Gson();
            obj= gson.fromJson(user_info.toString(), BookingDetailsResponse.class);

            if(objJson.has("message")){
                obj.setmMessage(objJson.getString("message"));
            }else{
                obj.setmMessage("");
            }
            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

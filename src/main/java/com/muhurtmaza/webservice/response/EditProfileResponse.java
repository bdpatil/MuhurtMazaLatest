package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.webservice.BaseResponse;

import org.json.JSONObject;

/**
 * Created by Admin on 17-02-2016.
 */
public class EditProfileResponse extends BaseResponse{

    @SerializedName("cust_address")
    String mAddress;
    @SerializedName("phone")
    String mPhone;
    @SerializedName("first_name")
    String mFName;
    @SerializedName("last_name")
    String mLName;
    @SerializedName("city")
    String mCity;
    @SerializedName("user_profile_image")
    String mUserImage;
    @SerializedName("email_id")
    String mEmailId;
    @SerializedName("cust_language")
    String mLanguage;

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmEmailId() {
        return mEmailId;
    }

    public void setmEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public String getmUserImage() {
        return mUserImage;
    }

    public void setmUserImage(String mUserImage) {
        this.mUserImage = mUserImage;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmLName() {
        return mLName;
    }

    public void setmLName(String mLName) {
        this.mLName = mLName;
    }

    public String getmFName() {
        return mFName;
    }

    public void setmFName(String mFName) {
        this.mFName = mFName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public static EditProfileResponse fromJson(String pResult)  {
        try {

            JSONObject objJson= new JSONObject(pResult);
            EditProfileResponse obj=new EditProfileResponse();
            boolean success=objJson.getBoolean("success");
            if(!success){
                obj.setmMessage(objJson.getString("message"));
            }else{
                JSONObject user_info=objJson.getJSONObject("user_info");
                JSONObject basic_info=user_info.getJSONObject("basic");
                // obj.setmUser_id(basic_info.getString("user_id"));
                Gson gson = new Gson();
                obj= gson.fromJson(basic_info.toString(), EditProfileResponse.class);
                obj.setmMessage(objJson.getString("message"));
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.webservice.BaseResponse;

import org.json.JSONObject;

/**
 * Created by user on 16-12-2015.
 */
public class UpdateUserProfile extends BaseResponse {

    @SerializedName("name")
    String mName;

    @SerializedName("email")
    String mEmail_id;

    @SerializedName("phone")
    String mPhone;

    @SerializedName("user_profile_image")
    String mUser_profile_image;

    @SerializedName("user_id")
    String mUser_id;

    @SerializedName("state")
    String mState;

    @SerializedName("city")
    String mCity;

    @SerializedName("address")
    String mAddress;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail_id() {
        return mEmail_id;
    }

    public void setmEmail_id(String mEmail_id) {
        this.mEmail_id = mEmail_id;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmUser_profile_image() {
        return mUser_profile_image;
    }

    public void setmUser_profile_image(String mUser_profile_image) {
        this.mUser_profile_image = mUser_profile_image;
    }

    public String getmUser_id() {
        return mUser_id;
    }

    public void setmUser_id(String mUser_id) {
        this.mUser_id = mUser_id;
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

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }


    public static UpdateUserProfile fromJson(String pResult)  {
        try {

            JSONObject objJson= new JSONObject(pResult);

            UpdateUserProfile obj=new UpdateUserProfile();

            JSONObject user_info=objJson.getJSONObject("user_info");
            JSONObject basic_info=user_info.getJSONObject("basic");

            Gson gson = new Gson();
            obj= gson.fromJson(basic_info.toString(), UpdateUserProfile.class);

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

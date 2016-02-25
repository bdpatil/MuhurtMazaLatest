package com.muhurtmaza.webservice.response;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.webservice.BaseResponse;

import org.json.JSONObject;

/**
 * Created by SANDEEP on 11/28/2015.
 */
public class NewUserResponse extends BaseResponse {
    /*{
"user_info": {
"basic": {
"phone": "",
"first_name": "Bharat Patil",
"user_id": "574",
"sms_alert": "1",
"email_alert": "1",
"user_profile_image": "http://192.168.0.127:8000/media/images/IMG_patil8833gmail.com_2016-02-20_051352_298561.jpg",
"email_id": "patil8833@gmail.com",
"address": " "
}
},
"message": "Already sign up",
"success": "true"
}*/
    @SerializedName("phone")
    String mPhone;

    @SerializedName("first_name")
    String mFirst_name;

    @SerializedName("user_id")
    String mUser_id;

    @SerializedName("sms_alert")
    String mSms_alert;

    @SerializedName("email_alert")
    String mEmail_alert;

    @SerializedName("user_profile_image")
    String mUser_profile_image;

    @SerializedName("email_id")
    String mEmail_id;

    @SerializedName("address")
    String mAddress;

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhon(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmFirst_name() {
        return mFirst_name;
    }

    public void setmFirst_name(String mFirst_name) {
        this.mFirst_name = mFirst_name;
    }

    public String getmUser_id() {
        return mUser_id;
    }

    public void setmUser_id(String mUser_id) {
        this.mUser_id = mUser_id;
    }

    public String getmSms_alert() {
        return mSms_alert;
    }

    public void setmSms_alert(String mSms_alert) {
        this.mSms_alert = mSms_alert;
    }

    public String getmEmail_alert() {
        return mEmail_alert;
    }

    public void setmEmail_alert(String mEmail_alert) {
        this.mEmail_alert = mEmail_alert;
    }

    public String getmUser_profile_image() {
        return mUser_profile_image;
    }

    public void setmUser_profile_image(String mUser_profile_image) {
        this.mUser_profile_image = mUser_profile_image;
    }

    public String getmEmail_id() {
        return mEmail_id;
    }

    public void setmEmail_id(String mEmail_id) {
        this.mEmail_id = mEmail_id;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public static NewUserResponse fromJson(String pResult)  {
        try {

            Log.d("Return string",pResult.toString());

            JSONObject objJson= new JSONObject(pResult);
            NewUserResponse obj = new NewUserResponse();
            boolean success=objJson.getBoolean("success");
            if(!success){
                obj.setmMessage(objJson.getString("message"));
            }else{
                JSONObject user_info=objJson.getJSONObject("user_info");
                JSONObject basic_info=user_info.getJSONObject("basic");
                //obj.setmUser_id(basic_info.getString("user_id"));
                Gson gson = new Gson();
                obj= gson.fromJson(basic_info.toString(), NewUserResponse.class);
                obj.setmMessage(objJson.getString("message"));
            }
            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.webservice.BaseResponse;

import org.json.JSONObject;

/**
 * Created by Admin on 10-02-2016.
 */
public class CheckOTPResponse
        extends BaseResponse {

    @SerializedName("message")
    String mMessage;

    @SerializedName("success")
    String mSuccess;


    @Override
    public String getmMessage() {
        return mMessage;
    }

    @Override
    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(String mSuccess) {
        this.mSuccess = mSuccess;
    }

    @Override
    public String toString() {
        return "CheckOTPResponse{" +
                "mMessage='" + mMessage + '\'' +
                ", mSuccess='" + mSuccess + '\'' +
                '}';
    }

    public static CheckOTPResponse fromJson(String pResult) {
        try {

            JSONObject objJson = new JSONObject(pResult);
            CheckOTPResponse obj = new CheckOTPResponse();
            Gson gson = new Gson();
            obj = gson.fromJson(objJson.toString(), CheckOTPResponse.class);
            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

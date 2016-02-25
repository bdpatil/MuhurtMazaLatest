package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.webservice.BaseResponse;

import org.json.JSONObject;

/**
 * Created by admin on 26-12-2015.
 */
public class PoojaBookResponse extends BaseResponse{

    @SerializedName("booking_id")
    String  mBookingId;

    @SerializedName("message")
    String mMessage;

    @SerializedName("reference_no")
    String mReferenceNo;

    @SerializedName("success")
    String mSuccess;

    public String getmBookingId() {
        return mBookingId;
    }

    public void setmBookingId(String mBookingId) {
        this.mBookingId = mBookingId;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmReferenceNo() {
        return mReferenceNo;
    }

    public void setmReferenceNo(String mReferenceNo) {
        this.mReferenceNo = mReferenceNo;
    }

    public String getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(String mSuccess) {
        this.mSuccess = mSuccess;
    }

    @Override
    public String toString() {
        return "PoojaBookResponse{" +
                "mBookingId='" + mBookingId + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mReferenceNo='" + mReferenceNo + '\'' +
                ", mSuccess='" + mSuccess + '\'' +
                '}';
    }

    public static PoojaBookResponse fromJson(String pResult)  {
        try {

            JSONObject objJson= new JSONObject(pResult);

            PoojaBookResponse obj=new PoojaBookResponse();

            Gson gson = new Gson();
            obj= gson.fromJson(objJson.toString(), PoojaBookResponse.class);

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

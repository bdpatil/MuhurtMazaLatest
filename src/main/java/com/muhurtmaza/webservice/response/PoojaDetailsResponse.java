package com.muhurtmaza.webservice.response;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.model.PoojaDetailModel;
import com.muhurtmaza.webservice.BaseResponse;


/**
 * Created by imac on 18/12/15.
 */
public class PoojaDetailsResponse extends BaseResponse {

    @SerializedName("nameList")
    PoojaDetailModel mList;

    public PoojaDetailModel getmList() {
        Log.d("List", mList.toString());
        return mList;
    }

    public void setmList(PoojaDetailModel mList) {
        this.mList = mList;
    }

    public static PoojaDetailsResponse fromJson(String pResult)   {
        try {
            Gson gson = new Gson();
            return gson.fromJson(pResult, PoojaDetailsResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

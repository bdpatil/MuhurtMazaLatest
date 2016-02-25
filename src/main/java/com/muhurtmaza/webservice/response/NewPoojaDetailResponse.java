package com.muhurtmaza.webservice.response;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.model.NewPoojaDetailModel;

import com.muhurtmaza.webservice.BaseResponse;


/**
 * Created by imac on 18/12/15.
 */
public class NewPoojaDetailResponse extends BaseResponse {

    @SerializedName("nameList")
    NewPoojaDetailModel mList;

    public NewPoojaDetailModel getmList() {
        Log.d("List", mList.toString());
        return mList;
    }

    public void setmList(NewPoojaDetailModel mList) {
        this.mList = mList;
    }

    public static NewPoojaDetailResponse fromJson(String pResult)   {
        try {
            Gson gson = new Gson();
            return gson.fromJson(pResult, NewPoojaDetailResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

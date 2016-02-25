package com.muhurtmaza.webservice.response;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.model.NewPoojaListModel;
import com.muhurtmaza.model.PoojaListModel;
import com.muhurtmaza.webservice.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Akshay on 17/12/15.
 */
public class PoojaListResponse extends BaseResponse {

    @SerializedName("nameList")
    ArrayList<NewPoojaListModel> mList;

    public ArrayList<NewPoojaListModel> getmList() {
        Log.d("List",mList.toString());
        return mList;
    }

    public void setmList(ArrayList<NewPoojaListModel> mList) {
        this.mList = mList;
    }

    public static PoojaListResponse fromJson(String pResult)   {
        try {
            Gson gson = new Gson();
            return gson.fromJson(pResult, PoojaListResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

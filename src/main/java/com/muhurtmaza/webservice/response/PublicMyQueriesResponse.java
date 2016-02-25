package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.model.PublicMyQueriesModel;
import com.muhurtmaza.webservice.BaseResponse;

import java.util.ArrayList;

/**
 * Created by user on 23-12-2015.
 */
public class PublicMyQueriesResponse extends BaseResponse {

    @SerializedName("nameList")
    ArrayList<PublicMyQueriesModel> mList;

    public ArrayList<PublicMyQueriesModel> getmList() {
        return mList;
    }

    public void setmList(ArrayList<PublicMyQueriesModel> mList) {
        this.mList = mList;
    }

    public static GetBookedPoojaResponse fromJson(String pResult)   {
        try {
            Gson gson = new Gson();
            return gson.fromJson(pResult, GetBookedPoojaResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

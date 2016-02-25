package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.model.GetCityModel;
import com.muhurtmaza.webservice.BaseResponse;

import java.util.ArrayList;

/**
 * Created by admin on 22-12-2015.
 */
public class GetCityNamesResponse extends BaseResponse {

    @SerializedName("nameList")
    ArrayList<GetCityModel> mList;

    public ArrayList<GetCityModel> getmList() {
        return mList;
    }

    public void setmList(ArrayList<GetCityModel> mList) {
        this.mList = mList;
    }

    public static GetCityNamesResponse fromJson(String pResult) {

        try {
            Gson gson = new Gson();
            return gson.fromJson(pResult, GetCityNamesResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.muhurtmaza.model.BookingInfoModel;
import com.muhurtmaza.model.GetBookedPoojaModel;
import com.muhurtmaza.webservice.BaseResponse;

import java.util.ArrayList;

/**
 * Created by admin on 04-12-2015.
 */
public class GetBookedPoojaResponse extends BaseResponse{

    @SerializedName("nameList")
    ArrayList<GetBookedPoojaModel> mList;

    public ArrayList<GetBookedPoojaModel> getmList() {
        return mList;
    }

    public void setmList(ArrayList<GetBookedPoojaModel> mList) {
        this.mList = mList;
    }

    public static GetBookedPoojaResponse fromJson(String pResult)   {
        try {

            Gson gson = new Gson();
            return gson.fromJson(pResult.toString(), GetBookedPoojaResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

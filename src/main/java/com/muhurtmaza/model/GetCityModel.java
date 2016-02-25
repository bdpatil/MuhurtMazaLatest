package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 23-12-2015.
 */
public class GetCityModel {

    @SerializedName("city_name")
    String mCityName;

    @SerializedName("pooja_city_id")
    String mPoojaCityId;


    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getmPoojaCityId() {
        return mPoojaCityId;
    }

    public void setmPoojaCityId(String mPoojaCityId) {
        this.mPoojaCityId = mPoojaCityId;
    }

    @Override
    public String toString() {
        return "GetCityNamesResponse{" +
                "mCityName='" + mCityName + '\'' +
                ", mPoojaCityId='" + mPoojaCityId + '\'' +
                '}';
    }
}

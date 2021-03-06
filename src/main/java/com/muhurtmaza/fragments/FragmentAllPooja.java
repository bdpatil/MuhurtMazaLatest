/**
 *
 * @author
 * Akshaykumar Maldhure
 *
 *
 */


package com.muhurtmaza.fragments;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.muhurtmaza.R;
import com.muhurtmaza.adapter.PoojaBookingListRecyclerViewAdapter;
import com.muhurtmaza.model.NewPoojaListModel;

import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.PoojaListResponse;
import com.muhurtmaza.widget.MMToast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class FragmentAllPooja extends  ParentFragment implements BaseHttpHelper.ResponseHelper{
    private final static String ALL_POOJA = "All";
    private LinearLayoutManager layoutManager;
    private Context mContext;
    RecyclerView recyclerView;
    AppPreferences appPreferences;
    String cityname;
    PoojaBookingListRecyclerViewAdapter mAdapter;
    int color;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =inflater.inflate(R.layout.fragment_all_pooja, container, false);
        mContext = getActivity();
        setUI(rootView);
        appPreferences = AppPreferences.getInstance(mContext);
        cityname=appPreferences.getString(AppConstants.CITY_NAME_ADAPTER, "");
        if(cityname ==""){
            cityname="Pune";
        }

        try {
            if(BaseHttpHelper.isNwConnected(mContext))
                 getPoojaList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }


    public FragmentAllPooja(){

    }
    /**
     * this method calls API and returns list
     * @throws JSONException
     */
    private  void  getPoojaList() throws JSONException {
        showLoadingDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("city_name", cityname);
        jsonObject.put("status", ALL_POOJA);
        ApiHelper lPoojaListApi = new ApiHelper(ApiConstants.POST, ApiConstants.GET_ALL_POOJA_LIST_URL, jsonObject.toString(), this);
        lPoojaListApi.mApiID=ApiConstants.GET_ALL_POOJA_LIST_ID;
        lPoojaListApi.invokeAPI();

    }



    private void setUI(View view) {

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_all_pooja);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setBackgroundColor(Color.WHITE);

    }



    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();
     // previously it was not working so i created new adapter and set it to the viewpager.

        PoojaListResponse obj=(PoojaListResponse)pResponse;

        List<NewPoojaListModel> rowListItem = obj.getmList();
        if(mAdapter!=null)
            mAdapter.clear();
        Log.d("Response List", rowListItem.toString());
        mAdapter = new PoojaBookingListRecyclerViewAdapter(getContext(),rowListItem);
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
    }
}

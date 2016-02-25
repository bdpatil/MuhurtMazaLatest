package com.muhurtmaza.fragments;

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

/**
 * Created by admin on 22-01-2016.
 */
public class FragmentPopularPooja extends  ParentFragment implements BaseHttpHelper.ResponseHelper{

    private final static String POPULAR_POOJA = "Popular";
    private LinearLayoutManager layoutManager;
    private Context mContext;
    RecyclerView recyclerView;
    AppPreferences appPreferences;
    String cityname;
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
            getPoojaList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    public FragmentPopularPooja(){

    }

    private  void  getPoojaList() throws JSONException {
        showLoadingDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("city_name", cityname);
        jsonObject.put("status", POPULAR_POOJA);
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

        PoojaListResponse obj=(PoojaListResponse)pResponse;

        List<NewPoojaListModel> rowListItem = obj.getmList();

        Log.d("Response List", rowListItem.toString());
        PoojaBookingListRecyclerViewAdapter adapter = new PoojaBookingListRecyclerViewAdapter(getContext(), rowListItem);
        recyclerView.setAdapter(adapter);
        MMToast.getInstance().showLongToast("Popular Pooja", getActivity());
    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
    }
}

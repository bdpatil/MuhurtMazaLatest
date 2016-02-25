package com.muhurtmaza.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhurtmaza.R;
import com.muhurtmaza.adapter.RecyclerViewAdapter;
import com.muhurtmaza.model.BookingInfoModel;
import com.muhurtmaza.model.GetBookedPoojaModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.GetBookedPoojaResponse;
import com.muhurtmaza.widget.MMToast;

import java.util.List;

/**
 * Created by admin on 01-12-2015.
 */
public class AllBookedPoojaFragment
        extends ParentFragment
        implements BaseHttpHelper.ResponseHelper {

    private LinearLayoutManager layoutManager;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        View layout = inflater.inflate(R.layout.fragment_all_booked, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setBackgroundColor(Color.WHITE);

        AppPreferences appPreferences = AppPreferences.getInstance(getActivity());
        String UserId = appPreferences.getString(AppConstants.USER_ID, "");
        showLoadingDialog();
        ApiHelper lApi = new ApiHelper(ApiConstants.GET, ApiConstants.GET_ALL_BOOKINGS_URL + "40", "", this);
        lApi.mApiID = ApiConstants.GET_ALL_BOOKINGS_ID;
        lApi.invokeAPI();

        return layout;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();

        GetBookedPoojaResponse obj = (GetBookedPoojaResponse) pResponse;
        List<GetBookedPoojaModel> rowListItem = obj.getmList();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), rowListItem);
        recyclerView.setAdapter(adapter);

        MMToast.getInstance().showLongToast("Booking list" + pResponse, getActivity());
    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast("IS On Fail", getActivity());


    }
}
package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhurtmaza.R;
import com.muhurtmaza.adapter.CityListAdapter;
import com.muhurtmaza.interfaces.RecyclerItemClickListener;
import com.muhurtmaza.model.GetCityModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.GetCityNamesResponse;
import com.muhurtmaza.widget.MMToast;

import java.util.ArrayList;
import java.util.List;

public class NewSearchCityActivity extends ParentActivity
        implements BaseHttpHelper.ResponseHelper {

    private LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    List<GetCityModel> itemList;
    private ArrayList<String> citylist;

    AppPreferences muhurtMazaPreferences;
    Context mContext;
    android.support.v7.widget.Toolbar mToolbar;
    TextView txtTitle;
    ImageView imgMenu;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search_city);
        muhurtMazaPreferences = AppPreferences.getInstance(this);
        mContext = this;
        setUI();

        /*recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                cityName = itemList.get(position).getmCityName();
                AppPreferences appPreferences = AppPreferences.getInstance(mContext);
                appPreferences.putString(AppConstants.CITY_NAME_ADAPTER, cityName);

                MMToast.getInstance().showShortToast("is on search"+itemList.get(position).getmCityName(), mContext);
            }
        }));*/
    }

    private void setUI() {

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Update Profile");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("Select Your City");


        layoutManager = new LinearLayoutManager(mContext);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_all_city);
        recyclerView.setLayoutManager(layoutManager);

        /*CityListAdapter adapter = new CityListAdapter();
        recyclerView.setAdapter(adapter);*/

        getCityList();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();

        int apiId = pResponse.getmAPIType();

        if (apiId == ApiConstants.GET_CITY_NAMES_ID) {

            GetCityNamesResponse cityNamesResponse = (GetCityNamesResponse) pResponse;
            //Log.d("City response object", cityNamesResponse.toString());
            //Log.d("City response", cityNamesResponse.getmList().toString());

            try {
                List<GetCityModel> rowList = cityNamesResponse.getmList();
                Log.d("List data", rowList.toString());


/*
                CityListAdapter adapter = new CityListAdapter(rowList, NewSearchCityActivity.this);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
*/
                CityListAdapter adapter = new CityListAdapter(rowList, NewSearchCityActivity.this);
                recyclerView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            int i;
            int len = cityNamesResponse.getmList().size();

            muhurtMazaPreferences.putString(AppConstants.TOTAL_BOOK_POOJA_CITY, "" + len);

            for (i = 0; i < len; i++) {
                citylist.add(i, cityNamesResponse.getmList().get(i).getmCityName());

                if (!cityNamesResponse.getmList().get(i).getmCityName().equals("")) {
                    Log.d("City names", AppConstants.CITY_ID_ + i + ":" + cityNamesResponse.getmList().get(i).getmCityName());
                    muhurtMazaPreferences.putString(AppConstants.CITY_ID_ + i, cityNamesResponse.getmList().get(i).getmCityName());
                }
            }

        }

    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showShortToast(AppConstants.CHECK_Internet, mContext);
    }

    private void getCityList() {

        citylist = new ArrayList<String>();
        if(BaseHttpHelper.isNwConnected(mContext)) {
            showLoadingDialog();
            try {
                ApiHelper lGetCityApi = new ApiHelper(ApiConstants.POST, ApiConstants.GET_CITY_NAMES, "", NewSearchCityActivity.this);
                lGetCityApi.mApiID = ApiConstants.GET_CITY_NAMES_ID;
                lGetCityApi.invokeAPI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

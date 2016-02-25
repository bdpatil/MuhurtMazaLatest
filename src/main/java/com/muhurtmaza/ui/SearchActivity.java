package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.muhurtmaza.R;
import com.muhurtmaza.adapter.SearchPoojaListRecyclerViewAdapter;
import com.muhurtmaza.model.NewPoojaListModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.PoojaListResponse;
import com.muhurtmaza.widget.MMToast;

import java.util.List;

public class SearchActivity
        extends ParentActivity
        implements BaseHttpHelper.ResponseHelper{

    String searchQuery = "";
    Context mContext;

    private LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    private android.support.v7.widget.Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        searchQuery = intent.getStringExtra(AppConstants.SEARCH_QUERY);

        mContext = this;

        setUI();

        //MMToast.getInstance().showLongToast(searchQuery,mContext);

        getSearchedPoojaList();
    }

    private void getSearchedPoojaList(){
        showLoadingDialog();
        try {
            ApiHelper lSearchApi = new ApiHelper(ApiConstants.GET, ApiConstants.SEARCH_POOJA_URL+searchQuery, "", this);
            lSearchApi.mApiID = ApiConstants.SEARCH_POOJA_ID;
            lSearchApi.invokeAPI();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUI(){
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_search);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();

        PoojaListResponse obj=(PoojaListResponse)pResponse;

        List<NewPoojaListModel> rowListItem = obj.getmList();

        //Log.d("Response List",rowListItem.toString());
        SearchPoojaListRecyclerViewAdapter adapter = new SearchPoojaListRecyclerViewAdapter(mContext, rowListItem);
        recyclerView.setAdapter(adapter);
        MMToast.getInstance().showLongToast("Pooja List", mContext);
    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast(AppConstants.CHECK_INTERNET_CONNECTION,mContext);
    }
}

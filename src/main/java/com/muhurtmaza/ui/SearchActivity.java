package com.muhurtmaza.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.muhurtmaza.R;
import com.muhurtmaza.adapter.PoojaBookingListRecyclerViewAdapter;
import com.muhurtmaza.adapter.SearchPoojaListRecyclerViewAdapter;
import com.muhurtmaza.model.NewPoojaListModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.PoojaListResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class SearchActivity
        extends ParentActivity
        implements BaseHttpHelper.ResponseHelper{

    String searchQuery = "",cityname;
    Context mContext;

    private LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    private android.support.v7.widget.Toolbar mToolBar;
    AppPreferences appPreferences;
    Toolbar mToolbar;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appPreferences = AppPreferences.getInstance(mContext);
        cityname=appPreferences.getString(AppConstants.CITY_NAME_ADAPTER, "");
        if(cityname ==""){
            cityname="Pune";
        }
        Intent intent = getIntent();
        searchQuery = intent.getStringExtra(AppConstants.SEARCH_QUERY);



        setUI();

        //MMToast.getInstance().showLongToast(searchQuery,mContext);

        getSearchedPoojaList(searchQuery);
    }

    private void getSearchedPoojaList(String text){
        //showLoadingDialog();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("search_text",text);
            jsonObject.put("city_name", cityname);
            jsonObject.put("status", "All");
            ApiHelper lSearchApi = new ApiHelper(ApiConstants.POST, ApiConstants.SEARCH_POOJA_URL, jsonObject.toString(), this);
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
        PoojaBookingListRecyclerViewAdapter adapter = new PoojaBookingListRecyclerViewAdapter(mContext, rowListItem);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search_view, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setQuery(searchQuery, false);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            @Override
            public boolean   onQueryTextChange( String newText ) {
                if(!newText.equals("")) {
                    getSearchedPoojaList(newText);
                }
                return true;
            }

            @Override
            public boolean  onQueryTextSubmit(String query) {

                return true;
            }
        });
        return true;
    }

    @Override
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
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast(AppConstants.CHECK_INTERNET_CONNECTION,mContext);
    }
}

package com.muhurtmaza.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhurtmaza.model.GetCityModel;

import java.util.List;
import com.muhurtmaza.R;
import com.muhurtmaza.ui.BookingDetailsActivity;
import com.muhurtmaza.ui.MainDrawerActivity;
import com.muhurtmaza.ui.NewForgotPasswordActivity;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.widget.MMToast;

/**
 * Created by Admin on 18-02-2016.
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListHolder> {

    List<GetCityModel> itemList;
    public Context context;
    String cityName;

    public CityListAdapter(){}

    public CityListAdapter(List<GetCityModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public CityListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_search_city, null);
        CityListHolder viewHolder = new CityListHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CityListHolder holder, final int position) {
        holder.txtCityName.setText(itemList.get(position).getmCityName());

        holder.txtCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetCityModel model=itemList.get(position);
                MMToast.getInstance().showLongToast("City has been changed to " + itemList.get(position).getmCityName(), context);
                cityName = itemList.get(position).getmCityName();
                AppPreferences appPreferences = AppPreferences.getInstance(context);
                appPreferences.putString(AppConstants.CITY_NAME_ADAPTER, cityName);
                Intent intent = new Intent(context, MainDrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
package com.muhurtmaza.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muhurtmaza.R;

/**
 * Created by Admin on 18-02-2016.
 */
public class CityListHolder extends RecyclerView.ViewHolder {

    public TextView txtCityName;

    public CityListHolder(View itemView) {
        super(itemView);
        txtCityName = (TextView) itemView.findViewById(R.id.txt_search_city_name);

    }
}
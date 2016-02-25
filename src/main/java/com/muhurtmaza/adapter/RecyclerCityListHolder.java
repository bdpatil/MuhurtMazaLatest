package com.muhurtmaza.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.muhurtmaza.R;

/**
 * Created by admin on 27-01-2016.
 */


public class RecyclerCityListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView txtCityName;
    //LinearLayout linearLayout;

    public RecyclerCityListHolder(View itemView){
        super(itemView);
        //linearLayout = (LinearLayout)itemView.findViewById(R.id.layout_search_city);
        txtCityName = (TextView)itemView.findViewById(R.id.txt_search_city_name);
    }

    @Override
    public void onClick(View v) {

    }
}

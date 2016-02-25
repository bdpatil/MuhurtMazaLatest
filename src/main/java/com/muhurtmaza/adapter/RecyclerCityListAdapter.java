package com.muhurtmaza.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhurtmaza.R;
import com.muhurtmaza.model.GetCityModel;

import java.util.List;

/**
 * Created by admin on 27-01-2016.
 */
public class RecyclerCityListAdapter extends RecyclerView.Adapter<RecyclerCityListHolder>{

    private List<GetCityModel> itemList;
    public Context context;

    public RecyclerCityListAdapter(List<GetCityModel> itemList,Context context){
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerCityListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_search_city,null);
        RecyclerCityListHolder viewHolder = new RecyclerCityListHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerCityListHolder holder, int position) {
        Log.d("City name ",itemList.get(position).getmCityName());
        holder.txtCityName.setText(itemList.get(position).getmCityName());
        //holder.txtCityName.setText("Demo text");
    }


    @Override
    public int getItemCount() {
        return 0;
    }
}

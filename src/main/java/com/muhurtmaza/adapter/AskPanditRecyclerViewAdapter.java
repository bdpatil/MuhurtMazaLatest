package com.muhurtmaza.adapter;

/**
 * Created by imac on 30/11/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhurtmaza.R;
import com.muhurtmaza.model.BookingInfoModel;

import java.util.List;

/**
 * Created by Gowtham Chandrasekar on 31-07-2015.
 */
public class AskPanditRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<BookingInfoModel> itemList;
    private Context context;

    public AskPanditRecyclerViewAdapter(){

    }

    public AskPanditRecyclerViewAdapter(Context context, List<BookingInfoModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclingTest","onCreateViewHolder method is called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_myqueries, null);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Log.d("RecyclingTest","onBindViewHolder method is called");
        //holder.callDetails.setText(itemList.get(position).getmBookingId());
      //  holder.callDetails.setText(itemList.get(position).getmPoojaName());
        //Glide.with(context).load(itemList.get(position).getmImgPath()).into(holder.profile_);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
package com.muhurtmaza.adapter;

/**
 * Created by imac on 30/11/15.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.muhurtmaza.R;
import com.muhurtmaza.model.BookingInfoModel;
import com.muhurtmaza.model.GetBookedPoojaModel;
import com.muhurtmaza.ui.BookingDetailsActivity;
import com.muhurtmaza.utility.AppConstants;

import java.util.List;

/**
 * Created by Gowtham Chandrasekar on 31-07-2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<GetBookedPoojaModel> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<GetBookedPoojaModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclingTest","onCreateViewHolder method is called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_new_booking_history, null);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        Glide.with(context).load(itemList.get(position).getmPoojaImage()).into(holder.imgPujaImage);
        holder.txtStatus.setText(itemList.get(position).getmStatus());
        holder.txtPujaName.setText(itemList.get(position).getmPoojaName());
        holder.txtDuration.setText(itemList.get(position).getmDuration());
        holder.txtGuruji.setText(itemList.get(position).getmGuruji());
        holder.txtDakshina.setText(itemList.get(position).getmDakshina());
        holder.txtPujaDate.setText(itemList.get(position).getmBookingDate());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BookingDetailsActivity.class);
                intent.putExtra(AppConstants.POOJA_BOOKING_ID,itemList.get(position).getmBookingId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
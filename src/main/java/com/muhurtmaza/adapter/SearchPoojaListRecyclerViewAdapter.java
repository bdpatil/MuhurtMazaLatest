package com.muhurtmaza.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.muhurtmaza.R;
import com.muhurtmaza.model.NewPoojaListModel;
import com.muhurtmaza.model.PoojaListModel;
import com.muhurtmaza.ui.PoojaDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by admin on 22-12-2015.
 */
public class SearchPoojaListRecyclerViewAdapter extends RecyclerView.Adapter<SearchPoojaListRecyclerViewHolder> {
    private List<NewPoojaListModel> itemList;

    public static final String RUPEE="\u20B9 ";
    private Context context;

    public SearchPoojaListRecyclerViewAdapter(Context context, List<NewPoojaListModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SearchPoojaListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("RecyclingTest", "onCreateViewHolder method is called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_pooja_bookings, null);
        SearchPoojaListRecyclerViewHolder viewHolder = new SearchPoojaListRecyclerViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(SearchPoojaListRecyclerViewHolder holder, int position) {
        //Log.d("RecyclingTest", "onBindViewHolder method is called");
        final int pos = position;
        holder.poojaName.setText(itemList.get(position).getPoojaName());
        holder.poojaPrice.setText(RUPEE + "" + 200);
        holder.poojaDuration.setText("Duration - "+getFormatedTime(itemList.get(position).getDuration()));
        holder.noOfGuruji.setText("Guruji - "+itemList.get(position).getNoOfGuruji());
        holder.noOfViews.setText("100");
        holder.noOfLikes.setText("20");
        Glide.with(context)
                .load(itemList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_muma_logo_dark)
                .crossFade()
                .into(holder.poojaImage);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PoojaDetailsActivity.class);
                i.putExtra("poojaId", itemList.get(pos).getPoojaId());
                Log.d("Print", itemList.get(pos).getPoojaId());
                v.getContext().startActivity(i);
            }
        });

        holder.bookPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PoojaDetailsActivity.class);
                i.putExtra("poojaId", itemList.get(pos).getPoojaId());
                i.putExtra("poojaCost",itemList.get(pos).getDakshinaWithoutSamagree());
                Log.d("Print",itemList.get(pos).getPoojaId());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    private String getFormatedTime(String time){
        String duration="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(time);

            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            if(hour==0){
                duration =minute +" min";
            }
            if(hour!=0){
                duration = hour +" hrs";
            }
            if(hour!=0 && minute!=0){
                duration = hour+":"+minute+" hrs";
            }

        }
        catch (Exception e){

        }

        return duration;
    }
}

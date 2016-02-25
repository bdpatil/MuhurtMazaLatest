package com.muhurtmaza.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.muhurtmaza.R;
import com.muhurtmaza.model.NewPoojaListModel;
import com.muhurtmaza.ui.NewPoojaDetailsActivity;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by imac on 01/12/15.
 */
public class PoojaBookingListRecyclerViewAdapter  extends RecyclerView.Adapter<PoojaBookingListRecyclerViewHolder> {

    final static String POOJA_ID ="pooja_id";
    final static String POOJA_CITY ="pooja_city";
    private List<NewPoojaListModel> itemList;
    public static final String RUPEE="\u20B9 ";
    AppPreferences appPreferences;
    String cityname;
    private Context context;

    public PoojaBookingListRecyclerViewAdapter(Context context, List<NewPoojaListModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public PoojaBookingListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclingTest", "onCreateViewHolder method is called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_new_book_pooja, null);
        PoojaBookingListRecyclerViewHolder viewHolder = new PoojaBookingListRecyclerViewHolder(view);
        appPreferences = AppPreferences.getInstance(context);
        cityname=appPreferences.getString(AppConstants.CITY_NAME_ADAPTER, "");
        if(cityname ==""){
            cityname="Pune";
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PoojaBookingListRecyclerViewHolder holder, final int position) {
        Log.d("RecyclingTest", "onBindViewHolder method is called");
        holder.poojaName.setText(itemList.get(position).getPoojaName());
        holder.poojaPrice.setText(RUPEE + "" + itemList.get(position).getDakshinaWithoutSamagree());
        holder.poojaDuration.setText(getFormatedTime(itemList.get(position).getDuration()));
        holder.noOfGuruji.setText(itemList.get(position).getNoOfGuruji());
        holder.noOfViews.setText(itemList.get(position).getViews());
        holder.noOfLikes.setText(itemList.get(position).getBooked());

        Glide.with(context)
                .load(itemList.get(position).getImage())
                .fitCenter()
                .placeholder(R.drawable.pooja_img)
                .crossFade()
                .into(holder.poojaImage);
        displayRating(itemList.get(position).getRating(),holder);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NewPoojaDetailsActivity.class);


                Bundle bundle = new Bundle();
                bundle.putString(POOJA_ID, itemList.get(position).getPoojaId());
                bundle.putString(POOJA_CITY, cityname);
                i.putExtras(bundle);

                v.getContext().startActivity(i);
            }
        });



    }



    private void displayRating(String number,final PoojaBookingListRecyclerViewHolder holder){
        Float rating = new Float(number);
        for (int i =1; i<= rating.intValue(); i++){

            switch (i){
                case 1:
                    holder.star1.setBackgroundResource(R.drawable.ic_star_h);
                    break;
                case 2:
                    holder.star2.setBackgroundResource(R.drawable.ic_star_h);
                    break;
                case 3:
                    holder.star3.setBackgroundResource(R.drawable.ic_star_h);
                    break;
                case 4:
                    holder.star4.setBackgroundResource(R.drawable.ic_star_h);
                    break;
                case 5:
                    holder.star5.setBackgroundResource(R.drawable.ic_star_h);
                    break;
            }
        }
        int temp = rating.intValue()+1;
        for (int i =temp; i<= 5; i++){

            switch (i){
                case 1:
                    holder.star1.setBackgroundResource(R.drawable.ic_star);
                    break;
                case 2:
                    holder.star2.setBackgroundResource(R.drawable.ic_star);
                    break;
                case 3:
                    holder.star3.setBackgroundResource(R.drawable.ic_star);
                    break;
                case 4:
                    holder.star4.setBackgroundResource(R.drawable.ic_star);
                    break;
                case 5:
                    holder.star5.setBackgroundResource(R.drawable.ic_star);
                    break;
            }
        }

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

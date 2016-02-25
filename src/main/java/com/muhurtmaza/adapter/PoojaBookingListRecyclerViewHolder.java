package com.muhurtmaza.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhurtmaza.R;


/**
 * Created by imac on 01/12/15.
 */
public class PoojaBookingListRecyclerViewHolder extends RecyclerView.ViewHolder {


    public TextView poojaName,poojaDuration,noOfGuruji,noOfLikes,noOfViews,poojaPrice;
    String poojaId = "";
    public ImageView poojaImage,star1,star2,star3,star4,star5;


    CardView cv;
    public PoojaBookingListRecyclerViewHolder(final View itemView) {
        super(itemView);
        //implementing onClickListener

        cv = (CardView)itemView.findViewById(R.id.cv);
        cv.setPreventCornerOverlap(false);
        poojaName = (TextView)itemView.findViewById(R.id.txt_new_puja_name);
        poojaPrice = (TextView)itemView.findViewById(R.id.txt_new_dakashina);
        poojaDuration = (TextView)itemView.findViewById(R.id.txt_new_puja_duration);
        noOfGuruji= (TextView)itemView.findViewById(R.id.txt_new_guruji_no);
        poojaImage = (ImageView)itemView.findViewById(R.id.img_pooja_view);
        noOfLikes = (TextView)itemView.findViewById(R.id.txt_new_likes);
        noOfViews = (TextView)itemView.findViewById(R.id.txt_new_views);

        //Rating stars
        star1=(ImageView)itemView.findViewById(R.id.star1);
        star2=(ImageView)itemView.findViewById(R.id.star2);
        star3=(ImageView)itemView.findViewById(R.id.star3);
        star4=(ImageView)itemView.findViewById(R.id.star4);
        star5=(ImageView)itemView.findViewById(R.id.star5);

    }



}

package com.muhurtmaza.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muhurtmaza.R;
import com.muhurtmaza.ui.BookingDetailsActivity;

/**
 * Created by Gowtham Chandrasekar on 31-07-2015.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView txtStatus,txtPujaName,txtDuration,txtDakshina,txtPujaDate,txtGuruji;
    public ImageView imgPujaImage;
    CardView cv;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
       // itemView.setOnClickListener(this);
        cv = (CardView)itemView.findViewById(R.id.cv);
        cv.setPreventCornerOverlap(false);

        imgPujaImage = (ImageView)itemView.findViewById(R.id.img_puja_MyBooking);
        txtStatus= (TextView) itemView.findViewById(R.id.txtStatus_MyBooking);
        txtPujaName= (TextView) itemView.findViewById(R.id.txtPujaName_MyBooking);
        txtDuration= (TextView) itemView.findViewById(R.id.txtDuration_MyBooking);
        txtGuruji= (TextView) itemView.findViewById(R.id.txtGuruji_MyBooking);
        txtDakshina= (TextView) itemView.findViewById(R.id.txtDakshina_MyBooking);
        txtPujaDate= (TextView) itemView.findViewById(R.id.txtPujaDate_MyBooking);
    }


}
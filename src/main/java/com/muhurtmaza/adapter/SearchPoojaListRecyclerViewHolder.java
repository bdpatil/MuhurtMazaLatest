package com.muhurtmaza.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhurtmaza.R;

/**
 * Created by admin on 22-12-2015.
 */
public class SearchPoojaListRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView poojaName,poojaDuration,noOfGuruji,noOfLikes,noOfViews,poojaPrice;
    String poojaId = "";
    public ImageView poojaImage;
    public Button bookPooja;

    CardView cv;
    public SearchPoojaListRecyclerViewHolder(final View itemView) {
        super(itemView);
        //implementing onClickListener
        itemView.setOnClickListener(this);
        cv = (CardView)itemView.findViewById(R.id.cv);
        cv.setPreventCornerOverlap(false);

        poojaName = (TextView)itemView.findViewById(R.id.pooja_name);
        poojaPrice = (TextView)itemView.findViewById(R.id.pooja_price);
        poojaDuration =(TextView)itemView.findViewById(R.id.pooja_duration);
        noOfGuruji=(TextView)itemView.findViewById(R.id.no_of_guruji);
        poojaImage = (ImageView)itemView.findViewById(R.id.pooja_image);
        noOfLikes = (TextView)itemView.findViewById(R.id.likes_count);
        noOfViews = (TextView)itemView.findViewById(R.id.views_count);
        bookPooja = (Button)itemView.findViewById(R.id.book_pooja_btn);


        bookPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(itemView.getContext(), PoojaDetailsActivity.class);
                i.putExtra("poojaId", 101);
                System.out.print("Addd"+getAdapterPosition());
                itemView.getContext().startActivity(i);*/
            };
        });
    }

    @Override
    public void onClick(View view) {
        //Every time you click on the row toast is displayed



    }

}

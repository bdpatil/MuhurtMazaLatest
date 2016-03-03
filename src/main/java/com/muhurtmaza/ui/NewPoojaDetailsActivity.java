package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.muhurtmaza.R;
import com.muhurtmaza.model.BookingDetails;
import com.muhurtmaza.model.NewPoojaDetailModel;
import com.muhurtmaza.model.User;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.NewPoojaDetailResponse;
import com.muhurtmaza.webservice.response.PoojaBookResponse;

import com.muhurtmaza.webservice.response.PoojaDetailsResponse;
import com.muhurtmaza.widget.MMToast;

import android.support.v7.widget.AppCompatButton;

import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;;import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewPoojaDetailsActivity extends ParentActivity implements  View.OnClickListener, BaseHttpHelper.ResponseHelper{

    final static String POOJA_ID ="pooja_id";
    final static String POOJA_CITY ="pooja_city";
    public TextView poojaDuration,noOfGuruji,noOfLikes,noOfViews,poojaPrice,poojaDescription,poojaBlessing,poojaInstructions,poojaNotes;
    String poojaId ;
    String poojaCity;
    AppCompatButton bookPoojaBtn;
    public ImageView poojaImage,star1,star2,star3,star4,star5;
    CollapsingToolbarLayout collapsingToolbar;
    public static final String RUPEE = "\u20B9 ";
    private Context mContext;
    Bundle bundle;
    NewPoojaDetailModel rowListItem;
    User user;
    BookingDetails details;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pooja_details);
        //initialization
        details =BookingDetails.getInstance();

        poojaPrice = (TextView)findViewById(R.id.txt_new_dakashina);
        poojaDuration = (TextView)findViewById(R.id.txt_new_puja_duration);
        noOfGuruji= (TextView)findViewById(R.id.txt_new_guruji_no);
        poojaImage = (ImageView)findViewById(R.id.img_pooja_view);
        noOfLikes = (TextView)findViewById(R.id.txt_new_likes);
        noOfViews = (TextView)findViewById(R.id.txt_new_views);
        poojaDescription = (TextView)findViewById(R.id.txt_pooja_desc);
        poojaBlessing = (TextView)findViewById(R.id.txt_pooja_blessings);
        poojaInstructions = (TextView)findViewById(R.id.txt_pooja_instructions);
        poojaNotes = (TextView)findViewById(R.id.txt_pooja_notes);
        bookPoojaBtn =(AppCompatButton)findViewById(R.id.btn_new_book);
        //Rating stars
        star1=(ImageView)findViewById(R.id.star1);
        star2=(ImageView)findViewById(R.id.star2);
        star3=(ImageView)findViewById(R.id.star3);
        star4=(ImageView)findViewById(R.id.star4);
        star5=(ImageView)findViewById(R.id.star5);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);

        //get data from previous activity
        Bundle bundle = getIntent().getExtras();
        poojaId = bundle.getString(POOJA_ID);
        details.setBookingId(poojaId);
        poojaCity =bundle.getString(POOJA_CITY);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext=this;
        try {
            getPoojaDetailsforPoojaID(poojaId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //set pooja title
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        //Book
        bookPoojaBtn.setOnClickListener(this);
        user = User.getInstance();
        //System.out.println("User Name : "+user.getUsername());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pooja_details, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        if (item != null) {
            mShareActionProvider
                    = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        }



        return true;
    }

    /**
     * Book pooja action
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v==bookPoojaBtn){
            if(user.getUserid()!=null) {
                Intent i = new Intent(mContext, NewActivityPoojaBookingDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString(POOJA_ID, poojaId);
                bundle.putString(POOJA_CITY, poojaCity);
                i.putExtra("poojaDetails", rowListItem);
                i.putExtras(bundle);
                v.getContext().startActivity(i);
            }
            else {
                Intent i = new Intent(mContext, NewLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                v.getContext().startActivity(i);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Api Calls Start here

    void getPoojaDetailsforPoojaID(String id) throws JSONException {
        showLoadingDialog();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(POOJA_ID, id);
        jsonObject.put(POOJA_CITY, poojaCity);
        ApiHelper lPoojaListApi = new ApiHelper(ApiConstants.POST, ApiConstants.GET_POOJA_DETAILS_URL, jsonObject.toString(), this);
        lPoojaListApi.mApiID = ApiConstants.GET_POOJA_DETAILS_ID;
        lPoojaListApi.invokeAPI();

    }

    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();
        int apiId = pResponse.getmAPIType();

        if (apiId == ApiConstants.GET_POOJA_DETAILS_ID) {

            NewPoojaDetailResponse obj = (NewPoojaDetailResponse) pResponse;
            rowListItem = obj.getmList();

            collapsingToolbar.setTitle(rowListItem.getmPooja_Name());
            noOfViews.setText(rowListItem.getmViews());
            noOfLikes.setText(rowListItem.getmBooked());
            poojaDescription.setText(rowListItem.getmPoojaDescription());
            noOfGuruji.setText(rowListItem.getmNo_Of_Guruji());
            poojaDuration.setText( getFormatedTime(rowListItem.getmPooja_Duration()));
            String gurujiAndDuration = "Duration - " + getFormatedTime(rowListItem.getmPooja_Duration()) + ", Guruji -" + rowListItem.getmNo_Of_Guruji();
            String price = RUPEE + " " + rowListItem.getmDakshina_without_samagree();
            poojaPrice.setText(price);
            poojaInstructions.setText(rowListItem.getmSpecialInstructions());
            poojaNotes.setText(rowListItem.getmPoojaTerms());
            poojaBlessing.setText(rowListItem.getmPoojaBlessings());

            details.setPoojaName(rowListItem.getmPooja_Name());

            Glide.with(mContext)
                    .load(rowListItem.getmPooja_Image_URL())
                    .centerCrop()
                    .placeholder(R.drawable.pooja_image)
                    .crossFade()
                    .into(poojaImage);
            displayRating(rowListItem.getmRating());
            setShareIntent();

        }

    }

    private String getFormatedTime(String time) {
        String duration = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(time);

            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            if (hour == 0) {
                duration = minute + " min";
            }
            if (hour != 0) {
                duration = hour + " hrs";
            }
            if (hour != 0 && minute != 0) {
                duration = hour + ":" + minute + " hrs";
            }

        } catch (Exception e) {

        }

        return duration;
    }

    private void displayRating(String number){
        Float rating = new Float(number);
        for (int i =1; i<= rating.intValue(); i++){

            switch (i){
                case 1:
                    star1.setBackgroundResource(R.drawable.ic_star_h);
                    break;
                case 2:
                    star2.setBackgroundResource(R.drawable.ic_star_h);
                    break;
                case 3:
                    star3.setBackgroundResource(R.drawable.ic_star_h);
                    break;
                case 4:
                    star4.setBackgroundResource(R.drawable.ic_star_h);
                    break;
                case 5:
                    star5.setBackgroundResource(R.drawable.ic_star_h);
                    break;
            }
        }
        int temp = rating.intValue()+1;
        for (int i =temp; i<= 5; i++){

            switch (i){
                case 1:
                    star1.setBackgroundResource(R.drawable.ic_star);
                    break;
                case 2:
                    star2.setBackgroundResource(R.drawable.ic_star);
                    break;
                case 3:
                    star3.setBackgroundResource(R.drawable.ic_star);
                    break;
                case 4:
                    star4.setBackgroundResource(R.drawable.ic_star);
                    break;
                case 5:
                    star5.setBackgroundResource(R.drawable.ic_star);
                    break;
            }
        }

    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();

        int apiId = pBaseResponse.getmAPIType();
        if(apiId == ApiConstants.BOOK_POOJA_ID){

        }

        //  MMToast.getInstance().showLongToast(AppConstants.API_STATUS_FALSE_MESSAGE, mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();

    }

    /**
     * share action
     */
    // Call to update the share intent
    private void setShareIntent() {
        if (mShareActionProvider != null) {
            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/html");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Pooja Recommendation from Muhurtmaza");
            shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<img src="+rowListItem.getmPooja_Image_URL()+" alt="+rowListItem.getmPooja_Name()+"><br>"+
            "\n<b>"+rowListItem.getmPooja_Name()+"</b><br>"+
            "\n<p>"+rowListItem.getmPoojaDescription()+"</p><br>\n"+
            "\n<b>No of Guruji:</b> "+rowListItem.getmNo_Of_Guruji()+"\t <b>Duration: </b>" + getFormatedTime(rowListItem.getmPooja_Duration())));

            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}




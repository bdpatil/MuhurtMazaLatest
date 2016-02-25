package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.muhurtmaza.R;
import com.muhurtmaza.fragments.FragmentPoojaPayment;
import com.muhurtmaza.fragments.FragmentPoojaPrices;
import com.muhurtmaza.fragments.FragmentPoojaVenue;
import com.muhurtmaza.model.PoojaBookModel;
import com.muhurtmaza.model.PoojaDetailModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.PoojaBookResponse;
import com.muhurtmaza.webservice.response.PoojaDetailsResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PoojaDetailsActivity extends ParentActivity implements BaseHttpHelper.ResponseHelper {

    Button nextbtn, stepOne, stepTwo, stepThree;
    View stepOneIndicator, stepTwoIndicator;
    String poojaId;
    ImageView poojaImage;
    FragmentPoojaPayment paymentFragment;
    FragmentPoojaVenue venueFragment;
    FragmentPoojaPrices pricesFragment;
    public static final String RUPEE = "\u20B9 ";
    public TextView poojaName, poojaDurationAndGuruji, withOutSamagriPrice, poojaDescription, withSamagriPrice, price, detailText;

    private Context mContext;
    public PoojaBookModel poojaBookModel;
    String paymentMode = "", contactNo="", poojaPrice="",bookingId="";

    AppPreferences appPreferences;
    ViewPager pager;
    android.support.v7.widget.Toolbar mToolbar;
    private ImageView imgMenu;
    private TextView txtTitle;

    Gson gson;
    String json="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pooja_detail);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = (TextView)mToolbar.findViewById(R.id.txt_title);
        imgMenu = (ImageView)mToolbar.findViewById(R.id.img_back);
        txtTitle.setText("Book Pooja");
        imgMenu.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(PoojaDetailsActivity.this,MainDrawerActivity.class);
                nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextIntent);
            }
        });

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            poojaId = b.getString("poojaId");  //(String) b.get("poojaId");
            poojaPrice = b.getString("poojaCost");  //(String)b.get("poojaCost");
        }
        mContext = this;

        //Set values for future use
        poojaBookModel = new PoojaBookModel();
        poojaBookModel.setmPoojaId(poojaId);
        poojaBookModel.setmCost(poojaPrice);

        appPreferences = AppPreferences.getInstance(PoojaDetailsActivity.this);
        gson = new Gson();
        json = gson.toJson(poojaBookModel);
        appPreferences.putString(AppConstants.PREF_POOJA_BOOK,json);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pooja Details");*/
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);
        //setup stepper buttons
        nextbtn = (Button) findViewById(R.id.next_btn);
        stepOne = (Button) findViewById(R.id.stepone);
        stepTwo = (Button) findViewById(R.id.steptwo);
        stepThree = (Button) findViewById(R.id.stepthree);

        stepOneIndicator = (View) findViewById(R.id.step_one_indicator);
        stepTwoIndicator = (View) findViewById(R.id.step_two_indicator);

        poojaName = (TextView) findViewById(R.id.pooja_name);
        poojaDescription = (TextView) findViewById(R.id.pooja_description);
        poojaDurationAndGuruji = (TextView) findViewById(R.id.duration_and_guruji);
        poojaImage = (ImageView) findViewById(R.id.pooja_image);
        price = (TextView) findViewById(R.id.pooja_price);

        detailText = (TextView) findViewById(R.id.detailsText);

        setupUI();
        updateStepIndicator();
        try {
            getPoojaDetailsforPoojaID(poojaId);
        } catch (Exception e) {

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void setupUI() {
        nextbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (pager.getCurrentItem()) {
                    case 0:
                        if (pricesFragment.validate()) {
                            pager.setCurrentItem(getItem(+1), true);
                            updateStepIndicator();
                        }
                        break;
                    case 1:
                        if (venueFragment.validate()) {
                            pager.setCurrentItem(getItem(+1), true);
                            updateStepIndicator();
                        }
                        break;
                    case 2:
                        if (pricesFragment.validate()) {
                            if (venueFragment.validate()) {

                                AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                                String custId = appPreferences.getString(AppConstants.USER_ID, "");

                                poojaBookModel.setmCutomerId(custId);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Calendar calendar = Calendar.getInstance();
                                String currentDate = simpleDateFormat.format(calendar.getTime());
                                poojaBookModel.setmCurrentDate(currentDate);

                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
                                String currentTime = simpleTimeFormat.format(calendar.getTime());
                                poojaBookModel.setmCurrentTime(currentTime);

                                json = gson.toJson(poojaBookModel);
                                appPreferences.putString(AppConstants.PREF_POOJA_BOOK,json);

                                //Gson gson = new Gson();
                                json = appPreferences.getString(AppConstants.PREF_POOJA_BOOK, "");
                                Log.d("Json","Value = "+json.toString());
                                poojaBookModel = gson.fromJson(json, PoojaBookModel.class);
                                poojaPrice = poojaBookModel.getmCost();

                                //Get Price fragment values
                                String jsonP = appPreferences.getString(AppConstants.PREF_POOJA_BOOK_PRICE, "");
                                Log.d("JsonP","Value P= "+jsonP.toString());
                                PoojaBookModel fragmentPrice = gson.fromJson(jsonP,PoojaBookModel.class);
                                poojaBookModel.setmDate(fragmentPrice.getmDate());
                                String pCost = ""+fragmentPrice.getmCost().toString();
                                if(!pCost.equals(""))
                                    poojaPrice = pCost;
                                poojaBookModel.setmWithSamagri(fragmentPrice.getmWithSamagri());

                                //Get Venu fragment values
                                String jsonV = appPreferences.getString(AppConstants.PREF_POOJA_BOOK_VENU, "");
                                Log.d("JsonV","Value V= "+jsonV.toString());
                                PoojaBookModel fragmentVenu = gson.fromJson(jsonV,PoojaBookModel.class);
                                poojaBookModel.setmFirstName(fragmentVenu.getmFirstName());
                                poojaBookModel.setmLastName(fragmentVenu.getmLastName());
                                poojaBookModel.setmLanguage(fragmentVenu.getmLanguage());
                                poojaBookModel.setmEmail(fragmentVenu.getmEmail());
                                poojaBookModel.setmContactNo(fragmentVenu.getmContactNo());
                                poojaBookModel.setmVenu(fragmentVenu.getmVenu());
                                poojaBookModel.setmState(fragmentVenu.getmState());
                                poojaBookModel.setmCity(fragmentVenu.getmCity());

                                //Get Payment fragment values
                                String jsonPay = appPreferences.getString(AppConstants.PREF_POOJA_BOOK_PAYMENT, "");
                                Log.d("JsonPay","Value Pay= "+json.toString());
                                PoojaBookModel fragmentPay = gson.fromJson(jsonPay,PoojaBookModel.class);
                                poojaBookModel.setmMethod(fragmentPay.getmMethod());

                                paymentMode = poojaBookModel.getmMethod();
                                contactNo = poojaBookModel.getmContactNo();
                                //poojaPrice = poojaBookModel.getmCost();
                                bookingId = poojaBookModel.getmPoojaId();

                                Log.d("Book pooja model","Model values = "+poojaBookModel.toString());

                                //if (paymentMode != null || !paymentMode.equals("") || !paymentMode.equals("")) {
                                   // if (paymentMode.equals(AppConstants.POOJA_BOOKING_MODE_COD)) {
                                        //Log.d("Pooja book model", "" + poojaBookModel.toString());
                                        //MMToast.getInstance().showLongToast("Pay to guruji = " + poojaBookModel.toString(), mContext);
                                        try {
                                            showLoadingDialog();
                                            //Api call here
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("pooja_id", poojaBookModel.getmPoojaId());
                                            jsonObject.put("cust_id", poojaBookModel.getmCutomerId());
                                            jsonObject.put("current_date", poojaBookModel.getmCurrentDate());
                                            jsonObject.put("current_time", poojaBookModel.getmCurrentTime());
                                            jsonObject.put("date", poojaBookModel.getmDate());
                                            jsonObject.put("venue",poojaBookModel.getmVenu());
                                            jsonObject.put("with samagri",poojaBookModel.getmWithSamagri());
                                            jsonObject.put("cost", poojaPrice);
                                            jsonObject.put("method", paymentMode);
                                            jsonObject.put("first_name", poojaBookModel.getmFirstName());
                                            jsonObject.put("last_name", poojaBookModel.getmLastName());
                                            jsonObject.put("language", poojaBookModel.getmLanguage());
                                            jsonObject.put("email", poojaBookModel.getmEmail());
                                            jsonObject.put("contact_no", contactNo);
                                            jsonObject.put("state", poojaBookModel.getmState());
                                            jsonObject.put("city", poojaBookModel.getmCity());

                                            ApiHelper lPoojaListApi = new ApiHelper(ApiConstants.POST, ApiConstants.BOOK_POOJA_URL, jsonObject.toString(), PoojaDetailsActivity.this);
                                            lPoojaListApi.mApiID = ApiConstants.BOOK_POOJA_ID;
                                            lPoojaListApi.invokeAPI();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                  //  } else {
                                   //     MMToast.getInstance().showLongToast("Payment gateway = " + poojaBookModel.toString(), mContext);
                                   // }
                                //}
                            } else {
                                pager.setCurrentItem(1, true);
                                updateStepIndicator();
                            }
                        } else {
                            pager.setCurrentItem(0, true);
                            updateStepIndicator();
                        }
                        break;
                    default:
                        break;
                }


            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                updateStepIndicator();
            }

            @Override
            public void onPageSelected(int position) {
                updateStepIndicator();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                updateStepIndicator();
            }
        });


    }

    //returns viewpager item
    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }


    //update button(step) indicators

    void updateStepIndicator() {

        switch (pager.getCurrentItem()) {
            case 0:
                //set text color of steps circle
                detailText.setText("Choose Pooja");
                stepOne.setTextColor(getResources().getColor(R.color.colorWhite));
                stepTwo.setTextColor(getResources().getColor(R.color.colorGrey));
                stepThree.setTextColor(getResources().getColor(R.color.colorGrey));

                //set background color of steps circle
                stepOne.setBackgroundResource(R.drawable.round_button_active);
                stepTwo.setBackgroundResource(R.drawable.round_button_disabled);
                stepThree.setBackgroundResource(R.drawable.round_button_disabled);

                //set indicator lines
                stepOneIndicator.setBackgroundColor(getResources().getColor(R.color.colorStepDisabled));
                stepTwoIndicator.setBackgroundColor(getResources().getColor(R.color.colorStepDisabled));
                nextbtn.setText("Next");
                break;
            case 1:
                //set text color of steps circle
                detailText.setText("Pooja Venue");
                stepOne.setTextColor(getResources().getColor(R.color.colorWhite));
                stepTwo.setTextColor(getResources().getColor(R.color.colorWhite));
                stepThree.setTextColor(getResources().getColor(R.color.colorGrey));

                //set background color of steps circle
                stepOne.setBackgroundResource(R.drawable.round_button_active);
                stepTwo.setBackgroundResource(R.drawable.round_button_active);
                stepThree.setBackgroundResource(R.drawable.round_button_disabled);

                //set indicator lines
                stepOneIndicator.setBackgroundColor(getResources().getColor(R.color.colorStepActive));
                stepTwoIndicator.setBackgroundColor(getResources().getColor(R.color.colorStepDisabled));
                nextbtn.setText("Next");
                break;
            case 2:
                //set text color of steps circle
                detailText.setText("Payment Method");
                stepOne.setTextColor(getResources().getColor(R.color.colorWhite));
                stepTwo.setTextColor(getResources().getColor(R.color.colorWhite));
                stepThree.setTextColor(getResources().getColor(R.color.colorWhite));

                //set background color of steps circle
                stepOne.setBackgroundResource(R.drawable.round_button_active);
                stepTwo.setBackgroundResource(R.drawable.round_button_active);
                stepThree.setBackgroundResource(R.drawable.round_button_active);

                //set indicator lines
                stepOneIndicator.setBackgroundColor(getResources().getColor(R.color.colorStepActive));
                stepTwoIndicator.setBackgroundColor(getResources().getColor(R.color.colorStepActive));
                nextbtn.setText("Confirm Pooja");
                break;
            default:
                break;
        }
    }


    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    pricesFragment = new FragmentPoojaPrices();
                    return pricesFragment;
                case 1:
                    venueFragment = new FragmentPoojaVenue();
                    return venueFragment;
                case 2:
                    paymentFragment = new FragmentPoojaPayment();
                    return paymentFragment;
                default:
                    return paymentFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }


    }


    //Api Calls Start here

    void getPoojaDetailsforPoojaID(String poojaid) throws JSONException {
        showLoadingDialog();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pooja_id", poojaid);

        ApiHelper lPoojaListApi = new ApiHelper(ApiConstants.POST, ApiConstants.GET_POOJA_DETAILS_URL, jsonObject.toString(), this);
        lPoojaListApi.mApiID = ApiConstants.GET_POOJA_DETAILS_ID;
        lPoojaListApi.invokeAPI();

    }

    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();
        int apiId = pResponse.getmAPIType();

        if (apiId == ApiConstants.GET_POOJA_DETAILS_ID) {

            PoojaDetailsResponse obj = (PoojaDetailsResponse) pResponse;
            pricesFragment.RecievedResponse(obj);

            PoojaDetailModel rowListItem = obj.getmList();

            poojaName.setText(rowListItem.getmPooja_Name());

            //update title text
            //getSupportActionBar().setTitle(rowListItem.getmPooja_Name());
            txtTitle.setText(rowListItem.getmPooja_Name());

            poojaDescription.setText(rowListItem.getmPoojaDescription());
            String gurujiAndDuration = "Duration - " + getFormatedTime(rowListItem.getmPooja_Duration()) + ", Guruji -" + rowListItem.getmNo_Of_Guruji();
            String poojaPrice = RUPEE + " " + rowListItem.getmPoojaWithoutSamagriCost();
            poojaDurationAndGuruji.setText(gurujiAndDuration);
            price.setText(poojaPrice);
            poojaBookModel.setmCost(""+rowListItem.getmPoojaWithoutSamagriCost());

            json = gson.toJson(poojaBookModel);
            appPreferences.putString(AppConstants.PREF_POOJA_BOOK,json);

            Glide.with(mContext)
                    .load(rowListItem.getmPooja_Image_URL())
                    .centerCrop()
                    .placeholder(R.drawable.ic_muma_logo_dark)
                    .crossFade()
                    .into(poojaImage);

        } else if (apiId == ApiConstants.BOOK_POOJA_ID) {

            if (paymentMode.equals(AppConstants.POOJA_BOOKING_MODE_COD)) {

                PoojaBookResponse poojaBookResponse = (PoojaBookResponse) pResponse;
                Intent i = new Intent(mContext, ConfirmationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(AppConstants.BOOK_POOJA_REFERENCE_NO, poojaBookResponse.getmReferenceNo());
                startActivity(i);
                finish();

            } else if (paymentMode.equals(AppConstants.POOJA_BOOKING_MODE_GATEWAY)) {
                //Payment gateway

                Intent intent = new Intent(mContext,MuhurtMazaPaymentActivity.class);
                intent.putExtra(AppConstants.POOJA_BOOKING_ID,bookingId);
                intent.putExtra(AppConstants.COST_OF_POOJA, poojaPrice);
                intent.putExtra(AppConstants.CONTACT_NO_FOR_POOJA, contactNo);
                startActivity(intent);
                finish();
            }

            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_PRICE);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_VENU);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_PAYMENT);
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

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();

        int apiId = pBaseResponse.getmAPIType();
        if(apiId == ApiConstants.BOOK_POOJA_ID){
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_PRICE);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_VENU);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_PAYMENT);
        }

        MMToast.getInstance().showLongToast(AppConstants.API_STATUS_FALSE_MESSAGE,mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        imgMenu.setVisibility(View.INVISIBLE);
        txtTitle.setVisibility(View.INVISIBLE);
    }
}


/**
 * @author NileshM 10/02/2016
 */


package com.muhurtmaza.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhurtmaza.R;
import com.muhurtmaza.model.NewPoojaDetailModel;
import com.muhurtmaza.model.PoojaDetailModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.PoojaBookResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewPoojaBookingReviewDetailsActivity extends ParentActivity implements View.OnClickListener , BaseHttpHelper.ResponseHelper{

    private Context context;
    private ImageView imgMenu;
    private TextView txtTitle;
    final static String POOJA_ID ="pooja_id";
    final static String POOJA_CITY ="pooja_city";
    Button paytoguruji, paynow;
    TextView txt_fname, txt_lname, txt_language, txt_email, txt_mobile, txt_address, txt_city,txt_samagree,txt_muhurtdate,txt_dakshina,txt_poojaname,txt_poojaduration,txt_noofguruji;
    String strfname, strlname, strlanguage, stremail, strmobile, straddress, strcity;
    String sfname,slname,slanguage,semail,smobile,saddress,scity,ssamagree,sdakshina,smuhurtdate,poojaId,poojaCity,ssamagreeStatus;
    android.support.v7.widget.Toolbar toolbar;
    String paymentMode = "";
    NewPoojaDetailModel poojaDetailModel;
    AppPreferences appPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);
        context = this;
        appPreferences = AppPreferences.getInstance(NewPoojaBookingReviewDetailsActivity.this);
        setupUI();
    }

    private void setupUI() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = (TextView) toolbar.findViewById(R.id.txt_title);
        imgMenu = (ImageView) toolbar.findViewById(R.id.img_back);
        txtTitle.setText("Review Details");
        //   mToolbar.setNavigationIcon(R.drawable.ic_share);
        imgMenu.setBackgroundResource(R.drawable.ic_white_arrow);
        imgMenu.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_fname = (TextView) findViewById(R.id.txt_fnameReviewDetails);
        txt_lname = (TextView) findViewById(R.id.txt_lnameReviewDetails);
        txt_language = (TextView) findViewById(R.id.txt_languageReviewDetails);
        txt_email = (TextView) findViewById(R.id.txt_emailReviewDetails);
        txt_mobile = (TextView) findViewById(R.id.txt_mobileNoReviewDetails);
        txt_address = (TextView) findViewById(R.id.txt_addressReviewDetails);
        txt_city = (TextView) findViewById(R.id.txt_cityReviewDetails);
        txt_samagree =(TextView)findViewById(R.id.txt_samagreeReviewDetails);
        txt_muhurtdate =(TextView)findViewById(R.id.txt_dateReviewDetails);
        txt_dakshina =(TextView)findViewById(R.id.txt_dakshinaReviewDetails);
        txt_poojaname =(TextView)findViewById(R.id.txt_pooja_name);
        txt_noofguruji =(TextView)findViewById(R.id.txt_no_of_guruji);
        txt_poojaduration =(TextView)findViewById(R.id.txt_pooja_duration);

        paytoguruji = (Button) findViewById(R.id.btn_payToGurujiReviewDetails);
        paynow = (Button) findViewById(R.id.btn_payNowReviewDetails);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sfname = preferences.getString("FName", "");
        txt_fname.setText(sfname);
        slname = preferences.getString("LName", "");
        txt_lname.setText(slname);
        slanguage = preferences.getString("Language", "");
        txt_language.setText(slanguage);
        semail = preferences.getString("Email", "");
        txt_email.setText(semail);
        smobile = preferences.getString("MobileNo", "");
        txt_mobile.setText(smobile);
        saddress = preferences.getString("Address", "");
        txt_address.setText(saddress);
        scity = preferences.getString("City", "");
        txt_city.setText(scity);
        ssamagree = preferences.getString("Samagree", "");
        ssamagreeStatus =preferences.getString("SamagreeStatus","");
        txt_samagree.setText(ssamagree);
        smuhurtdate = preferences.getString("MuhurtDate", "");
        txt_muhurtdate.setText(smuhurtdate);
        sdakshina = preferences.getString("Dakshina", "");
        txt_dakshina.setText("Dakshina: Rs."+sdakshina+"/-");

        poojaDetailModel= (NewPoojaDetailModel) getIntent().getSerializableExtra("poojaDetails");
        txt_poojaname.setText(poojaDetailModel.getmPooja_Name());
        txt_poojaduration.setText(getFormatedTime(poojaDetailModel.getmPooja_Duration()));
        txt_noofguruji.setText(poojaDetailModel.getmNo_Of_Guruji());


        Bundle bundle = getIntent().getExtras();
        poojaId = bundle.getString(POOJA_ID);
        poojaCity =bundle.getString(POOJA_CITY);

        paytoguruji.setOnClickListener(this);
        paynow.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == paytoguruji) {
                paymentMode=AppConstants.POOJA_BOOKING_MODE_COD;
        }

        if (v == paynow) {
            paymentMode=AppConstants.POOJA_BOOKING_MODE_GATEWAY;

        }
        try {
        BookPoojaWithCOD();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgMenu.setVisibility(View.INVISIBLE);
        txtTitle.setVisibility(View.INVISIBLE);
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


    /**
     * Pay to Guruji Action
     */
    void BookPoojaWithCOD() throws JSONException {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        appPreferences.putString(AppConstants.USER_EMAIL,semail);
        appPreferences.putString(AppConstants.USER_NAME,sfname);
        DateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        Date muhurt = null;
        try {
             muhurt = format.parse(smuhurtdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date();

        showLoadingDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cust_id","24");
        jsonObject.put("pooja_id", poojaId);
        jsonObject.put("current_date", new SimpleDateFormat("yyyy-MM-dd").format(date));
        jsonObject.put("current_time", new SimpleDateFormat("HH:mm:ss").format(date));
        jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(muhurt));
        jsonObject.put("venue", saddress+" "+scity);
        jsonObject.put("with samagri", ssamagreeStatus);
        jsonObject.put("cost", sdakshina);
        jsonObject.put("first_name", sfname);
        jsonObject.put("last_name", slname);
        jsonObject.put("language", slanguage);
        jsonObject.put("email", semail);
        jsonObject.put("contact_no", smobile);
        jsonObject.put("city", scity);
        jsonObject.put("method", paymentMode);

        System.out.println("data" + jsonObject);
        ApiHelper lPoojaListApi = new ApiHelper(ApiConstants.POST, ApiConstants.BOOK_POOJA_URL, jsonObject.toString(), this);
        lPoojaListApi.mApiID = ApiConstants.BOOK_POOJA_ID;
        lPoojaListApi.invokeAPI();

    }

    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();
        int apiId = pResponse.getmAPIType();

        if (apiId == ApiConstants.BOOK_POOJA_ID) {
            PoojaBookResponse poojaBookResponse = (PoojaBookResponse) pResponse;
            if (paymentMode.equals(AppConstants.POOJA_BOOKING_MODE_COD)) {


                Intent i = new Intent(context, ConfirmationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(AppConstants.BOOK_POOJA_REFERENCE_NO, poojaBookResponse.getmReferenceNo());
                i.putExtra(AppConstants.USER_EMAIL, semail);
                startActivity(i);
                finish();

            } else if (paymentMode.equals(AppConstants.POOJA_BOOKING_MODE_GATEWAY)) {
                //Payment gateway
                Intent intent = new Intent(context,MuhurtMazaPaymentActivity.class);
                intent.putExtra(AppConstants.POOJA_BOOKING_ID,poojaBookResponse.getmBookingId());
                intent.putExtra(AppConstants.COST_OF_POOJA, sdakshina);
                intent.putExtra(AppConstants.CONTACT_NO_FOR_POOJA, smobile);
                startActivity(intent);
                finish();
            }

            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_PRICE);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_VENU);
            appPreferences.deletePreferences(AppConstants.PREF_POOJA_BOOK_PAYMENT);
        }

    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        runOnUiThread(new Runnable() {

            public void run() {

                if (!isFinishing()) {
                    new AlertDialog.Builder(NewPoojaBookingReviewDetailsActivity.this)
                            .setTitle("Muhurtmaza")
                            .setMessage("Sorry, Something going wrong")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                }
                            }).create().show();
                }
            }
        });


    }
}

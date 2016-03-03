/**
 * @author NileshM 10/02/2016
 */


package com.muhurtmaza.ui;


import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.muhurtmaza.R;

import com.muhurtmaza.model.BookingDetails;
import com.muhurtmaza.model.NewPoojaDetailModel;
import com.muhurtmaza.model.User;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.CommonUtility;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewActivityPoojaBookingDetails extends ParentActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private final static String WITH_SAMAGREE = "With Samagree";
    private final static String WITHOUT_SAMAGREE = "Without Samagree";
    final static String POOJA_ID ="pooja_id";
    final static String POOJA_CITY ="pooja_city";
    final static String POOJA_WITH_SAMAGREE_PRICE ="with_samagree_price";
    final static String POOJA_WITHOUT_SAMAGREE_PRICE ="without_samagree_price";
    Button review;

    private Context context;
    private ImageView imgMenu;
    private TextView txtTitle;
    private EditText edtfname_BookingDetails, edtlname_BookingDetails,
            edtemail_BookingDetails, edtmobileno_BookingDetails, edtaddress_BookingDetails, edtcity_BookingDetails;
    private Spinner edtlanguage_BookingDetails;

    BookingDetails details;
    private FontFitTextView btnDateDay,txtDay,dateButton,txtMonth;
    private String TAG = getClass().getSimpleName();
    private AppPreferences appPreferences;
    private CardView muhurtDateView;
    private RadioGroup radioMuhurtGroup;
    User user;
    private String muhurtDateSelected,samagreeSelected,withSamagreePrice,withoutSamagreePrice,poojaId,poojaCity,dakshina,samagreeStatus;
    NewPoojaDetailModel poojaDetailModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pooja_booking_details);
            context = this;
            user =User.getInstance();
            details =BookingDetails.getInstance();
            setupUI();
    }

    private void setupUI() {
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Booking Details");



        appPreferences = AppPreferences.getInstance(this);
        //Ids related to the_Activity Booking Details xml..NileshM
        edtfname_BookingDetails= (EditText) findViewById(R.id.et_fnameBookingDetailsActivity);
        edtlname_BookingDetails= (EditText) findViewById(R.id.et_lnameBookingDetailsActivity);
        edtlanguage_BookingDetails= (Spinner) findViewById(R.id.et_languageBookingDetailsActivity);
        edtemail_BookingDetails= (EditText) findViewById(R.id.et_emailBookingDetailsActivity);
        edtmobileno_BookingDetails= (EditText) findViewById(R.id.et_mobileNoBookingDetailsActivity);
        edtaddress_BookingDetails= (EditText) findViewById(R.id.et_addressBookingDetailsActivity);
        edtcity_BookingDetails= (EditText) findViewById(R.id.et_cityBookingDetailsActivity);
        edtfname_BookingDetails.setText("Akshay");
        edtlname_BookingDetails.setText("Maldhure");
        edtemail_BookingDetails.setText("akshay@bynry.com");
        edtmobileno_BookingDetails.setText("9595903117");
        edtaddress_BookingDetails.setText("Pune");
        edtcity_BookingDetails.setText("Pune");

        review= (Button) findViewById(R.id.btnReview_BookingDetailsActivity);
        dateButton = (FontFitTextView)findViewById(R.id.btn_date);
        txtDay =(FontFitTextView)findViewById(R.id.txt_day);
        txtMonth =(FontFitTextView)findViewById(R.id.txt_month);
        btnDateDay =(FontFitTextView)findViewById(R.id.txt_date_day);
        muhurtDateView =(CardView)findViewById(R.id.muhurt_date_view);

        samagreeSelected = WITHOUT_SAMAGREE;
        radioMuhurtGroup=(RadioGroup)findViewById(R.id.radGroup);

        Bundle bundle = getIntent().getExtras();
        poojaId = bundle.getString(POOJA_ID);
        poojaCity =bundle.getString(POOJA_CITY);
        withoutSamagreePrice = bundle.getString(POOJA_WITHOUT_SAMAGREE_PRICE);
        withSamagreePrice =bundle.getString(POOJA_WITH_SAMAGREE_PRICE);
        poojaDetailModel= (NewPoojaDetailModel) getIntent().getSerializableExtra("poojaDetails");
        System.out.println("Pooja Details "+poojaDetailModel.getmPooja_Name());
        dakshina =poojaDetailModel.getmDakshina_without_samagree();
        samagreeStatus="NO";
        user.setUserBookedPoojaName(poojaDetailModel.getmPooja_Name());
        muhurtDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                Date date = new Date();
                now.setTime(date);
                now.add(Calendar.DAY_OF_YEAR, 1);
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        NewActivityPoojaBookingDetails.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setAccentColor(Color.parseColor("#d0631f"));
                dpd.setTitle("Select Muhurt");

                dpd.show(getFragmentManager(), "Datepickerdialog");

            }

        });



        radioMuhurtGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_no:
                        // do operations specific to this selection
                        samagreeSelected = WITHOUT_SAMAGREE;
                        dakshina = poojaDetailModel.getmDakshina_without_samagree();
                        samagreeStatus="NO";
                        break;

                    case R.id.radio_yes:
                        // do operations specific to this selection
                        samagreeSelected = WITH_SAMAGREE;
                        dakshina = poojaDetailModel.getmDakshina_with_samagree();
                        samagreeStatus="YES";
                        break;


                }


            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        dateButton.setText("" + getFormatedDate(calendar.get(Calendar.DAY_OF_MONTH)));
        txtMonth.setText("" + new SimpleDateFormat("MMMM").format(tomorrow));
        txtDay.setText("" + new SimpleDateFormat("EEEE").format(tomorrow));
        btnDateDay.setText("Tomorrow");
        muhurtDateSelected = new SimpleDateFormat("dd MMMM yyyy").format(tomorrow).toString();
        review.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        if (v==review){
            validate();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // imgMenu.setVisibility(View.INVISIBLE);
        // txtTitle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date utilDate =null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            utilDate =formatter.parse(formatter.format(new Date()));

            Calendar ca1 = Calendar.getInstance();

            ca1.set(year, monthOfYear, dayOfMonth);

            java.util.Date d = new java.util.Date(ca1.getTimeInMillis());

            dateButton.setText(""+getFormatedDate(dayOfMonth));
            txtMonth.setText(""+new SimpleDateFormat("MMMM").format(d));
            txtDay.setText("" + new SimpleDateFormat("EEEE").format(d));
            Log.d("", utilDate.toString() + "date" + d.toString());
            muhurtDateSelected = new SimpleDateFormat("dd MMMM yyyy").format(d).toString();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();

            if(new SimpleDateFormat("dd/MM/yyyy").format(d).toString().equals(new SimpleDateFormat("dd/MM/yyyy").format(tomorrow).toString())){
                btnDateDay.setText("Tommorrow");
            }
            else {
                btnDateDay.setText(""+new SimpleDateFormat("EEEE").format(d));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private String getFormatedDate(int day){
        String date="";
        switch(day){
            case 1:
                date=day+"st";
                break;
            case 21:
                date=day+"st";
                break;
            case 31:
                date=day+"st";
                break;
            case 2:
                date=day+"nd";
                break;
            case 22:
                date=day+"nd";
                break;
            case 3:
                date=day+"rd";
                break;
            case 23:
                date=day+"rd";
                break;
            default:
                date=day+"th";
                break;

        }
        return date;
    }



    public boolean validate(){
        if(validateFirstName()&&validateLastName()&&validateEmail()&&validatePhone()&&validateAddress()&&validateCity()){
            String strfName = edtfname_BookingDetails.getText().toString().trim();
            String strlName = edtlname_BookingDetails.getText().toString().trim();
            String strEmail = edtemail_BookingDetails.getText().toString().trim();
            String strMobileNo = edtmobileno_BookingDetails.getText().toString().trim();
            String strAddress = edtaddress_BookingDetails.getText().toString().trim();
            String strCity = edtcity_BookingDetails.getText().toString().trim();
            String strLanguage = edtlanguage_BookingDetails.getItemAtPosition(edtlanguage_BookingDetails.getSelectedItemPosition()).toString();
            String strMuhurtdate =muhurtDateSelected;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FName", strfName);
            editor.putString("LName", strlName);
            editor.putString("Language", strLanguage);
            editor.putString("Email", strEmail);
            editor.putString("MobileNo", strMobileNo);
            editor.putString("Address", strAddress);
            editor.putString("City", strCity);
            editor.putString("MuhurtDate", muhurtDateSelected);
            editor.putString("Samagree",samagreeSelected);
            editor.putString("Dakshina",dakshina);
            editor.putString("PoojaId",poojaId);
            editor.putString("PoojaCity",poojaCity);
            editor.putString("SamagreeStatus",samagreeStatus);
            editor.commit();

            details.setUser_email_id(strEmail);
            details.setMuhurt_date(strMuhurtdate);
            details.setDakshina(dakshina);
            details.setSamagree_chossen(samagreeStatus);

            Toast.makeText(getApplicationContext(),"Successful...!",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, NewPoojaBookingReviewDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(POOJA_ID, poojaId);
            bundle.putString(POOJA_CITY, poojaCity);
            intent.putExtras(bundle);
            intent.putExtra("poojaDetails",poojaDetailModel);
            startActivity(intent);

            return true;
        }
        else{
            return false;
        }
    }

    public boolean validateFirstName( )
    {
        if(edtfname_BookingDetails.getText().toString().length()==0){
            edtfname_BookingDetails.setError("First name is required");
            edtfname_BookingDetails.requestFocus();
            return false;
        }
        else if(edtfname_BookingDetails.getText().toString().trim().matches("[a-zA-Z]*")){
            edtfname_BookingDetails.setError(null);
            return true;
        }
        else{
            edtfname_BookingDetails.setError("Enter valid name");
            edtfname_BookingDetails.requestFocus();
            return false;
        }
    }
    // end method validateFirstName

    // validate last name
    public boolean validateLastName( )
    {
        if(edtlname_BookingDetails.getText().toString().length()==0){
            edtlname_BookingDetails.setError("Last name is required");
            edtlname_BookingDetails.requestFocus();
            return false;
        }
        else if(edtlname_BookingDetails.getText().toString().trim().matches("[a-zA-Z]*")){
            edtlname_BookingDetails.setError(null);
            return true;
        }
        else{
            edtlname_BookingDetails.setError("Enter valid name");
            edtlname_BookingDetails.requestFocus();
            return false;
        }
    } // end method validateLastName

    public boolean validateEmail(){
        if (CommonUtility.getInstance(context).isValidEmail(edtemail_BookingDetails.getText().toString())){ //!
            edtemail_BookingDetails.setError(null);
            return true;
        }
        else {
            edtemail_BookingDetails.setError("Enter valid email id");
            edtemail_BookingDetails.requestFocus();
            return false;
        }
    }

    public boolean validatePhone(){
        if (edtmobileno_BookingDetails.getText().toString().trim().matches("\\d{10}")){
            edtmobileno_BookingDetails.setError(null);
            return true;
        }
        else {
            edtmobileno_BookingDetails.setError("Enter valid mobile no");
            edtmobileno_BookingDetails.requestFocus();
            return false;
        }
    }

    public boolean validateAddress(){
        if (edtaddress_BookingDetails.getText().toString().trim().matches("[A-Za-z0-9'\\.\\-\\s\\,\\/]{3,150}")){
            edtaddress_BookingDetails.setError(null);
            return true;
        }
        else {
            edtaddress_BookingDetails.setError("Enter valid address");
            edtaddress_BookingDetails.requestFocus();
            return false;
        }
    }

    public boolean validateCity() {
        if (edtcity_BookingDetails.getText().toString().length() == 0) {
            edtcity_BookingDetails.setError("City name is required");
            edtcity_BookingDetails.requestFocus();
            return false;
        } else if (edtcity_BookingDetails.getText().toString().trim().matches("[a-zA-Z\\s]*")) {
            edtcity_BookingDetails.setError(null);
            return true;
        } else {
            edtcity_BookingDetails.setError("Enter valid city");
            edtcity_BookingDetails.requestFocus();
            return false;
        }
    }

}
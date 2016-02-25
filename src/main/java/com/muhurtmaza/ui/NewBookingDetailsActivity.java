package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.CommonUtility;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 20-02-2016.
 */
public class NewBookingDetailsActivity extends ParentActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    Button review;
    private Context context;
    private ImageView imgMenu;
    private TextView txtTitle;
    private EditText edtfname_BookingDetails, edtlname_BookingDetails,
            edtemail_BookingDetails, edtmobileno_BookingDetails, edtaddress_BookingDetails, edtcity_BookingDetails;
    private Spinner edtlanguage_BookingDetails;
    android.support.v7.widget.Toolbar mToolbar;

    private FontFitTextView btnDateDay,txtDay,dateButton,txtMonth;
    private String TAG = getClass().getSimpleName();
    private AppPreferences appPreferences;
    private CardView muhurtDateView;
    private RadioGroup radioMuhurtGroup;
    private RadioButton radioMuhurtButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking_details);
        context = this;


        setupUI();
    }

    private void setupUI() {
        mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.anim_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Booking Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        txtTitle = (TextView)mToolbar.findViewById(R.id.txt_title);
//        imgMenu = (ImageView)mToolbar.findViewById(R.id.img_back);
//        txtTitle.setText("Booking Details");
        //   mToolbar.setNavigationIcon(R.drawable.ic_share);
//        imgMenu.setBackgroundResource(R.drawable.ic_white_arrow);
//        imgMenu.setVisibility(View.VISIBLE);
//        txtTitle.setVisibility(View.VISIBLE);

//        imgMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        appPreferences = AppPreferences.getInstance(this);
        //Ids related to the_Activity Booking Details xml..NileshM
        edtfname_BookingDetails= (EditText) findViewById(R.id.et_fnameBookingDetailsActivity);
        edtlname_BookingDetails= (EditText) findViewById(R.id.et_lnameBookingDetailsActivity);
        edtlanguage_BookingDetails= (Spinner) findViewById(R.id.et_languageBookingDetailsActivity);
        edtemail_BookingDetails= (EditText) findViewById(R.id.et_emailBookingDetailsActivity);
        edtmobileno_BookingDetails= (EditText) findViewById(R.id.et_mobileNoBookingDetailsActivity);
        edtaddress_BookingDetails= (EditText) findViewById(R.id.et_addressBookingDetailsActivity);
        edtcity_BookingDetails= (EditText) findViewById(R.id.et_cityBookingDetailsActivity);

        review= (Button) findViewById(R.id.btnReview_BookingDetailsActivity);
        dateButton = (FontFitTextView)findViewById(R.id.btn_date);
        txtDay =(FontFitTextView)findViewById(R.id.txt_day);
        txtMonth =(FontFitTextView)findViewById(R.id.txt_month);
        btnDateDay =(FontFitTextView)findViewById(R.id.txt_date_day);
        muhurtDateView =(CardView)findViewById(R.id.muhurt_date_view);

        edtfname_BookingDetails.setText("bharat");
        edtlname_BookingDetails.setText("patil");
        edtemail_BookingDetails.setText("bdpatil@gmail.com");
        edtmobileno_BookingDetails.setText("9552039590");
        edtaddress_BookingDetails.setText("New Sangvi");
        edtcity_BookingDetails.setText("Pune");

        radioMuhurtGroup=(RadioGroup)findViewById(R.id.radGroup);




        muhurtDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                Date date = new Date();
                now.setTime(date);
                now.add(Calendar.DAY_OF_YEAR, 1);
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        NewBookingDetailsActivity.this,
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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        dateButton.setText("" + getFormatedDate(calendar.DAY_OF_MONTH));
        txtMonth.setText("" + new SimpleDateFormat("MMMM").format(tomorrow));
        txtDay.setText("" + new SimpleDateFormat("EEEE").format(tomorrow));
        review.setOnClickListener(this);
    }

    private void review() {

        String strfName = edtfname_BookingDetails.getText().toString().trim();
        String strlName = edtlname_BookingDetails.getText().toString().trim();
        String strEmail = edtemail_BookingDetails.getText().toString().trim();
        String strMobileNo = edtmobileno_BookingDetails.getText().toString().trim();
        String strAddress = edtaddress_BookingDetails.getText().toString().trim();
        String strCity = edtcity_BookingDetails.getText().toString().trim();

        edtfname_BookingDetails.setText(appPreferences.getString(AppConstants.USER_FNAME.trim(), ""));
        edtlname_BookingDetails.setText(appPreferences.getString(AppConstants.USER_LNAME.trim(), ""));
        edtemail_BookingDetails.setText(appPreferences.getString(AppConstants.USER_EMAIL.trim(), ""));
        edtmobileno_BookingDetails.setText(appPreferences.getString(AppConstants.USER_NUMBER.trim(), ""));
        edtaddress_BookingDetails.setText(appPreferences.getString(AppConstants.USER_ADDRESS.trim(), ""));
        edtcity_BookingDetails.setText(appPreferences.getString(AppConstants.USER_CITY.trim(), ""));

        if (!validateFirstName()) {
            return;
        }
        if (!validateLastName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validateAddress()) {
            return;
        }
        if (!validateCity()) {
            return;
        } else {

            appPreferences.putString("FName", strfName);
            appPreferences.putString("LName", strlName);
            appPreferences.putString("Email", strEmail);
            appPreferences.putString("MobileNo", strMobileNo);
            appPreferences.putString("Address", strAddress);
            appPreferences.putString("City", strCity);
            Toast.makeText(getApplicationContext(), "Successful...!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, NewBookingDetailsActivity.class);
            startActivity(intent);


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
            if (CommonUtility.getInstance(this).isValidEmail(edtemail_BookingDetails.getText().toString())){ //!
                edtemail_BookingDetails.setError(null);
                return true;
            }
            else {
                edtemail_BookingDetails.setError("Enter valid email id");
                edtemail_BookingDetails.requestFocus();
                return false;
            }
        }

        public boolean validatePhone() {
            if (edtmobileno_BookingDetails.getText().toString().trim().matches("\\d{10}")) {
                edtmobileno_BookingDetails.setError(null);
                return true;
            } else {
                edtmobileno_BookingDetails.setError("Enter valid mobile no");
                edtmobileno_BookingDetails.requestFocus();
                return false;
            }
        }

        public boolean validateAddress() {
            if (edtaddress_BookingDetails.getText().toString().trim().matches("[A-Za-z0-9'\\.\\-\\s\\,\\/]{3,150}")) {
                edtaddress_BookingDetails.setError(null);
                return true;
            } else {
                edtaddress_BookingDetails.setError("Enter valid address");
                edtaddress_BookingDetails.requestFocus();
                return false;
            }
        }

        public boolean validateCity(){
            if(edtcity_BookingDetails.getText().toString().length()==0){
                edtcity_BookingDetails.setError("City name is required");
                edtcity_BookingDetails.requestFocus();
                return false;
            }
            else if(edtcity_BookingDetails.getText().toString().trim().matches("[a-zA-Z\\s]*")){
                edtcity_BookingDetails.setError(null);
                return true;
            }
            else {
                edtcity_BookingDetails.setError("Enter valid city");
                edtcity_BookingDetails.requestFocus();
                return false;
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
    @Override
    public void onClick(View v) {
        if (v==review){
            review();
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    Log.d("","YES");
                break;
            case R.id.radio_no:
                if (checked)
                    Log.d("","No");
                break;
        }
    }
}
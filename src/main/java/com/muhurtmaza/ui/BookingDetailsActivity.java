package com.muhurtmaza.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.BookingDetailsResponse;
import com.muhurtmaza.webservice.response.ReportIssueResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONObject;

public class BookingDetailsActivity extends ParentActivity implements View.OnClickListener, BaseHttpHelper.ResponseHelper {
    private TextView txtCode_Details, txtStatus_Details, txtPoojaName_Details, txtDuration_Details, txtGuruji_Details, txtUserName_Details, txtLanguage_Details, txtStatusSamagree_Details,
            txtEmail_Details, txtPhoneNo_Details, txtDate_Details, txtAddress_Details, txtCity_Details, txtDakshina_Details;
    private Button btnInvitesFriends;
    String bookingId;
    Button btnok_Report, btnCancel_Report;
    private Context mContext;
    private ImageView imgMenu, imgProfile_Details;
    private TextView txtTitle;
    android.support.v7.widget.Toolbar mToolbar;
    private Context context;
    int selectedOption = 0; //invite friends =1,report issue=2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_booking_details);
        setupUI();
        Intent intent = getIntent();
        if (intent != null) {
            bookingId = intent.getStringExtra(AppConstants.POOJA_BOOKING_ID);
            callBookDetatil(bookingId);
        }
    }

    private void setupUI() {

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = (TextView) mToolbar.findViewById(R.id.txt_title);
        imgMenu = (ImageView) mToolbar.findViewById(R.id.img_back);
        txtTitle.setText("Book Pooja Details");
        imgMenu.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgProfile_Details = (ImageView) findViewById(R.id.img_profileBookingDetails);
        txtCode_Details = (TextView) findViewById(R.id.txt_codeBookingDetails);
        txtStatus_Details = (TextView) findViewById(R.id.txt_statusBookingDetails);
        txtPoojaName_Details = (TextView) findViewById(R.id.txt_poojaNameBookingDetails);
        txtDuration_Details = (TextView) findViewById(R.id.txt_durationBookingDetails);
        txtGuruji_Details = (TextView) findViewById(R.id.txt_gurujiBookingDetails);

        txtUserName_Details = (TextView) findViewById(R.id.txt_nameBookingDetails);
        txtLanguage_Details = (TextView) findViewById(R.id.txt_languageBookingDetails);
        txtStatusSamagree_Details = (TextView) findViewById(R.id.txt_samagreeBookingDetails);
        txtEmail_Details = (TextView) findViewById(R.id.txt_emailBookingDetails);
        txtPhoneNo_Details = (TextView) findViewById(R.id.txt_mobileNoBookingDetails);
        txtDate_Details = (TextView) findViewById(R.id.txt_dateBookingDetails);
        txtAddress_Details = (TextView) findViewById(R.id.txt_addressBookingDetails);
        txtCity_Details = (TextView) findViewById(R.id.txt_cityBookingDetails);
        txtDakshina_Details = (TextView) findViewById(R.id.txt_dakshinaBookingDetails);
        /*Button invites fiends*/
        btnInvitesFriends = (Button) findViewById(R.id.btn_inviteFriendsForPooja);

        btnInvitesFriends.setOnClickListener(this);

    }

    private void callBookDetatil(String bookingId) {
        try {
            showLoadingDialog();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("booking_id", "40"); //bookingId
            ApiHelper lApi = new ApiHelper(ApiConstants.POST, ApiConstants.POOJA_BOOKINGS_DETAILS, jsonObject.toString(), this);
            lApi.mApiID = ApiConstants.POOJA_BOOKINGS_DETAILS_ID;
            lApi.invokeAPI();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnInvitesFriends) {

            String statusValue = btnInvitesFriends.getText().toString().trim();
            setTextForStatus(statusValue);

            if (selectedOption == 1) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "'MuhurtMaza' launches free app to help you Book Pooja for all occassions in only 3 clicks!! You focus on performing Pooja Rituals and leave the rest(Muhurt,Guruji & Samagree) to us.");
                intent.setType("application/plain");
                startActivity(intent);
            } else if (selectedOption == 2) {
                LayoutInflater li = LayoutInflater.from(mContext);
                View promptsView = li.inflate(R.layout.reportissuedialog, null);
                btnok_Report = (Button) promptsView.findViewById(R.id.btnOk_reportIssue);
                btnCancel_Report = (Button) promptsView.findViewById(R.id.btnCancel_reportIssue);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();

                btnok_Report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MMToast.getInstance().showLongToast("Ok clicked", mContext);
                        try {

                            showLoadingDialog();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("booking_id", "40");
                            jsonObject.put("issues", "Lost Item");

                            ApiHelper lApi = new ApiHelper(ApiConstants.POST, ApiConstants.REPORT_ISSUES, jsonObject.toString(), BookingDetailsActivity.this);
                            lApi.mApiID = ApiConstants.REPORT_ISSUES_ID;
                            lApi.invokeAPI();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        alertDialog.cancel();
                    }
                });
                btnCancel_Report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MMToast.getInstance().showLongToast("Cancel clicked", mContext);
                        alertDialog.cancel();
                    }
                });
            }
            selectedOption = 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgMenu.setVisibility(View.INVISIBLE);
        txtTitle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();
        int apiId = pResponse.getmAPIType();
        if (apiId == ApiConstants.POOJA_BOOKINGS_DETAILS_ID) {
            BookingDetailsResponse bookingResponse = (BookingDetailsResponse) pResponse;
            Log.e("Response", "" + bookingResponse.toString());

            //Glide.with(context).load(bookingResponse.getmImage());
            txtCode_Details.setText(bookingResponse.getmRefNo());
            txtStatus_Details.setText(bookingResponse.getmStatus());
            txtPoojaName_Details.setText(bookingResponse.getmPooja_Name());
            txtDuration_Details.setText(bookingResponse.getmDuration());
            txtGuruji_Details.setText(bookingResponse.getmGuruji());
            txtUserName_Details.setText(bookingResponse.getmUser_Name());
            txtLanguage_Details.setText(bookingResponse.getmLanguage());
            txtStatusSamagree_Details.setText(bookingResponse.getmSamagree());
            txtEmail_Details.setText(bookingResponse.getmEmail());
            txtPhoneNo_Details.setText(bookingResponse.getmPhoneNo());
            txtDate_Details.setText(bookingResponse.getmDate());
            txtAddress_Details.setText(bookingResponse.getmAddress());
            txtCity_Details.setText(bookingResponse.getmCity());
            txtDakshina_Details.setText(bookingResponse.getmDakshina());

            txtDate_Details.setText(bookingResponse.getmDate());
            txtLanguage_Details.setText(bookingResponse.getmLanguage());

            String strS = txtStatus_Details.getText().toString().trim();
            setTextForStatus(strS);

        } else if (apiId == ApiConstants.REPORT_ISSUES_ID) {
           /* try {

                ReportIssueResponse reportIssueResponse = (ReportIssueResponse)pResponse;

                Log.d("Message",pResponse.toString());
                String message = pResponse.getmMessage();
                if (message.equals("Saved")) {
                    MMToast.getInstance().showLongToast("Issue Send", mContext);

                } else {
                    MMToast.getInstance().showLongToast("wrong", mContext);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            MMToast.getInstance().showLongToast(pResponse.getmMessage(),mContext);
        }

    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
    }

    private void setTextForStatus(String strStatus) {

        if (strStatus.equals("Booked")) {
            btnInvitesFriends.setText("Invite friends for this Pooja");
            selectedOption = 1;

        } else {
            btnInvitesFriends.setText("Report Issue");
            selectedOption = 2;
        }
    }
}

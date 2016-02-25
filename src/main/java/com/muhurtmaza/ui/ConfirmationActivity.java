package com.muhurtmaza.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ConfirmationActivity extends ParentActivity {


    final static String POOJA_ID ="pooja_id";
    final static String POOJA_CITY ="pooja_city";
    TextView txtRefId, txtPhNo;

    private ImageView imgCall, imgMenu;
    private TextView txtTitle,refferanceNo,emailid;
    Button btn_invite;
    android.support.v7.widget.Toolbar mToolbar;
    FontFitTextView fontFitTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ButterKnife.inject(this);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = (TextView) mToolbar.findViewById(R.id.txt_title);
        imgMenu = (ImageView) mToolbar.findViewById(R.id.img_back);
        imgCall = (ImageView) mToolbar.findViewById(R.id.img_edit);
        fontFitTextView =(FontFitTextView)findViewById(R.id.txt_thankYouConfirmation);
        refferanceNo = (TextView)findViewById(R.id.txtBookingRefernceId_confirmation);
        emailid =(TextView)findViewById(R.id.txt_emailIdConfirmation);

        Bundle bundle = getIntent().getExtras();
        refferanceNo.setText(bundle.getString(AppConstants.BOOK_POOJA_REFERENCE_NO));
        emailid.setText(bundle.getString(AppConstants.USER_EMAIL));

        setupUI();

        txtTitle.setText("CONFIRMATION");
        //   mToolbar.setNavigationIcon(R.drawable.ic_share);
        imgCall.setBackgroundResource(R.drawable.ic_action_call);
        imgMenu.setBackgroundResource(R.drawable.ic_action_back);

        imgCall.setVisibility(View.VISIBLE);
        imgMenu.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);

        imgMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(ConfirmationActivity.this, MainDrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_invite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Muhurt Maza");
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });



        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager =
        (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    String uri = "tel:" + txtPhNo.getText().toString();
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                    if (ActivityCompat.checkSelfPermission(ConfirmationActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Your call has failed...", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
    private class MyPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // phone ringing...
                    Toast.makeText(ConfirmationActivity.this, incomingNumber + " calls you",
                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
                    Toast.makeText(ConfirmationActivity.this, "on call...",
                            Toast.LENGTH_LONG).show();
                    //because user answers the incoming call
                    onCall = true;
                    break;


                default:
                    break;
            }

        }
    }

    private void setupUI() {
        txtRefId = (TextView) findViewById(R.id.txtBookingRefernceId_confirmation);
        btn_invite = (Button) findViewById(R.id.btn_inviteFriendsForPoojaConfirmation);
        txtPhNo = (TextView) findViewById(R.id.txt_cellNoConfirmation);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgMenu.setVisibility(View.INVISIBLE);
        txtTitle.setVisibility(View.INVISIBLE);
        imgMenu.setVisibility(View.INVISIBLE);
    }
}

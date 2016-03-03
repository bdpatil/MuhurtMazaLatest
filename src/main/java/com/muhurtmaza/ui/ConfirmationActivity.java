package com.muhurtmaza.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muhurtmaza.R;
import com.muhurtmaza.model.BookingDetails;
import com.muhurtmaza.model.User;
import com.muhurtmaza.utility.AppConstants;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ConfirmationActivity extends ParentActivity {


    final static String POOJA_ID ="pooja_id";
    final static String POOJA_CITY ="pooja_city";
    TextView txtRefId, txtPhNo;


    private TextView refferanceNo,emailid;
    Button btn_invite;
    android.support.v7.widget.Toolbar mToolbar;
    FontFitTextView fontFitTextView;
    User user;
    Context mContext;
    BookingDetails details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        details = BookingDetails.getInstance();
        setContentView(R.layout.activity_confirmation);
        ButterKnife.inject(this);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mContext =this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle("Confirmation");
        user=User.getInstance();
        fontFitTextView =(FontFitTextView)findViewById(R.id.txt_thankYouConfirmation);
        refferanceNo = (TextView)findViewById(R.id.txtBookingRefernceId_confirmation);
        emailid =(TextView)findViewById(R.id.txt_emailIdConfirmation);

        Bundle bundle = getIntent().getExtras();
        refferanceNo.setText(details.getRefferanceNo());
        emailid.setText(details.getUser_email_id());

        setupUI();



        btn_invite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Dear Friends\n" +
                        "\n" +
                        "We have organized the "+user.getUserBookedPoojaName()+" on "+user.getUserBookedPoojaDate()+" for harmony and well-being.Please grace the occasion with your presence and blessings.\n" +
                        "\n" +
                        "Regards\n" +
                        ""+user.getUserFirstname()+"");
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });



        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager =
        (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(mContext, MainDrawerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_confirmation, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call:
                makeCall();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    void makeCall(){
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
}

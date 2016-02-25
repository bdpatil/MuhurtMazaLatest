package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.CheckOTPResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONObject;

import java.util.StringTokenizer;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewOTPActivity
        extends ParentActivity
        implements BaseHttpHelper.ResponseHelper,
        View.OnClickListener {

    @InjectView(R.id.txt_resendOtp)
    TextView txtResendOTP;
    @InjectView(R.id.input_Otp)
    EditText edtOtp;
    @InjectView(R.id.btnSubmitOtp)
    Button btnSubmitOTP;
    String strMobileNo;
    android.support.v7.widget.Toolbar mToolbar;
    private ImageView imgMenu;
    private TextView txtTitle;


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_otp);
        ButterKnife.inject(this);
        mContext = this;
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = (TextView) mToolbar.findViewById(R.id.txt_title);
        imgMenu = (ImageView) mToolbar.findViewById(R.id.img_back);
        txtTitle.setText("OTP");
        imgMenu.setBackgroundResource(R.drawable.ic_close);
        imgMenu.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(NewOTPActivity.this, TutorialActivity.class);
                nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextIntent);
                finish();
            }
        });
        getOTP();
        btnSubmitOTP.setOnClickListener(this);
        txtResendOTP.setOnClickListener(this);
    }

    private void getOTP() {

        AppPreferences appPreferences = AppPreferences.getInstance(mContext);
        strMobileNo = appPreferences.getString(AppConstants.USER_NUMBER, "");
        showLoadingDialog();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", strMobileNo);
            Log.d("Mobile No", strMobileNo);
            ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.GENERATE_OTP, jsonObject.toString(), this);
            lSignUpApi.mApiID = ApiConstants.GENERATE_OTP_ID;
            lSignUpApi.invokeAPI();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {

        if (v == btnSubmitOTP) {

            //String strMoNo = edtMobileNo.getText().toString().trim();
            String strOTP = edtOtp.getText().toString().trim();


            showLoadingDialog();

            AppPreferences appPreferences = AppPreferences.getInstance(mContext);
            String userID = appPreferences.getString(AppConstants.USER_ID, "");

            try {
                JSONObject jsonObject = new JSONObject();
                // jsonObject.put("user_id", userID);
                jsonObject.put("contactno", strMobileNo);
                jsonObject.put("otpcode", strOTP);

                ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.CHECK_OTP, jsonObject.toString(), this);
                lSignUpApi.mApiID = ApiConstants.CHECK_OTP_ID;
                lSignUpApi.invokeAPI();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if(v == txtResendOTP){

                showLoadingDialog();

                AppPreferences appPreferences = AppPreferences.getInstance(mContext);
                String userID = appPreferences.getString(AppConstants.USER_ID, "");

                try {
                    JSONObject jsonObject = new JSONObject();
                  //  jsonObject.put("user_id", userID);
                    jsonObject.put("phone", strMobileNo);

                    ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.REGENERATE_OTP, jsonObject.toString(), this);
                    lSignUpApi.mApiID = ApiConstants.REGENERATE_OTP_ID;
                    lSignUpApi.invokeAPI();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }



    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();
        Log.d("Response",pResponse.toString());
        int apiId = pResponse.getmAPIType();

        boolean isSuccess = pResponse.ismStatus();
        String strMessage = pResponse.getmMessage();

MMToast.getInstance().showLongToast("response"+pResponse,mContext);


        if (apiId == ApiConstants.GENERATE_OTP_ID) {



            if(isSuccess){
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String address = extras.getString("address");
                    String message = extras.getString("message");
                    Log.d("Message Generate",message);
                    StringTokenizer st = new StringTokenizer(message, ",\n");
                    String msg = st.nextToken();
                    edtOtp.setText(msg);
                }
            }
            else{
                edtOtp.setText("");
                edtOtp.requestFocus();
            }
        }
        if (apiId == ApiConstants.REGENERATE_OTP_ID) {
            if(isSuccess){
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String address = extras.getString("address");
                    String message = extras.getString("message");
                    Log.d("Message Generate",message);
                    StringTokenizer st = new StringTokenizer(message, ",\n");
                    String msg = st.nextToken();
                    edtOtp.setText(msg);
                }
            }
            else{
                edtOtp.setText("");
                edtOtp.requestFocus();
                MMToast.getInstance().showLongToast(strMessage,mContext);
            }
        }
        if (apiId == ApiConstants.CHECK_OTP_ID) {

            CheckOTPResponse obj = (CheckOTPResponse)pResponse;

            if(obj.getmSuccess().equals("true")){
                Log.d("Otp","Check otp");
                Intent intent = new Intent(this, MainDrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                this.finish();
            }
            else{
                Log.d("Otp","Else part otp");
                edtOtp.setText("");
                edtOtp.requestFocus();
                MMToast.getInstance().showLongToast(strMessage,mContext);
            }
        }

    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast(AppConstants.API_STATUS_FALSE_MESSAGE, mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }
}

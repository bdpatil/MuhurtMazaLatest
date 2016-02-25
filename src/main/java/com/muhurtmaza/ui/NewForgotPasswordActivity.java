package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.ConnectionDetector;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.CheckOTPResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONObject;

public class NewForgotPasswordActivity extends ParentActivity implements BaseHttpHelper.ResponseHelper, View.OnClickListener {

    android.support.v7.widget.Toolbar mToolbar;
    TextView txtTitle;
    ImageView imgMenu;
    Button btnForgotPass;
    EditText edt_Email;
    private Context mContext;
    private ConnectionDetector conDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_forgot_password);
        mContext = this;
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = (TextView) mToolbar.findViewById(R.id.txt_title);
        imgMenu = (ImageView) mToolbar.findViewById(R.id.img_back);
        txtTitle.setText("Forgot Password");
        imgMenu.setBackgroundResource(R.drawable.ic_close);
        txtTitle.setVisibility(View.VISIBLE);
        imgMenu.setVisibility(View.VISIBLE);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(NewForgotPasswordActivity.this, TutorialActivity.class);
                nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextIntent);
                finish();
            }
        });
        setUI();

    }

    public void setUI() {


        btnForgotPass = (Button) findViewById(R.id.btnSubmit_NewForgotPass);
        edt_Email = (EditText) findViewById(R.id.edt_emailForgotPass);

        btnForgotPass.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        txtTitle.setVisibility(View.INVISIBLE);
        imgMenu.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == btnForgotPass) {
        /*    if (conDetect.checkConnectivity(mContext)) {*/

                String strEmail = edt_Email.getText().toString().trim();
                boolean cancel = false;
                View focusView = null;

                edt_Email.setError(null);

                if (strEmail.isEmpty()) {
                    edt_Email.setError(getString(R.string.error_field_required));
                    focusView = edt_Email;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    showLoadingDialog();
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("email_id", strEmail);
                        ApiHelper lForgotPassApi = new ApiHelper(ApiConstants.POST, ApiConstants.FORGET_PASSWORD_URL, jsonObject.toString(), NewForgotPasswordActivity.this);
                        lForgotPassApi.mApiID = ApiConstants.FORGOT_PASSWORD_ID;
                        lForgotPassApi.invokeAPI();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } /*else {
            MMToast.getInstance().showLongToast(AppConstants.CHECK_INTERNET_CONNECTION, mContext);
        }*/


    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();
        String strMessage = pResponse.getmMessage();
        int apiId = pResponse.getmAPIType();
        if (apiId == ApiConstants.FORGOT_PASSWORD_ID) {
            CheckOTPResponse obj = (CheckOTPResponse)pResponse;
            if(obj.getmMessage().equals("Forgot Password Send Successfully")){
                MMToast.getInstance().showLongToast(strMessage, mContext);
            }
            else{
                MMToast.getInstance().showLongToast("Please try after some time",mContext);
            }
        }
    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast("On Fail", mContext);


    }
}


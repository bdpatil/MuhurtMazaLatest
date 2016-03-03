package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.muhurtmaza.R;
import com.muhurtmaza.model.User;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.LocationManager;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.BookPoojaFeedbackResponse;

public class SplashScreen extends ParentActivity implements BaseHttpHelper.ResponseHelper {

    private Context mContext;
    private boolean isLogin = false;
    private String latitude = "", longitude = "";
    AppPreferences muhurtMazaPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;

        muhurtMazaPreferences = AppPreferences.getInstance(this);
        String userID = muhurtMazaPreferences.getString(AppConstants.USER_ID, "").trim();

        if (!TextUtils.isEmpty(userID) && !userID.equals("null") && !userID.equals("") && userID != null) {
            initUser();
            isLogin = true;
            getFeedback();
            callNextScreen();
        } else {
            getCurrentLocation();
            callNextScreen();
        }
    }

    private void callNextScreen() {
        if (isLogin) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(SplashScreen.this, MainDrawerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);

        } else {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    muhurtMazaPreferences.putString(AppConstants.USER_SELECTED_CITY,"Pune");
                    Intent intent = new Intent(SplashScreen.this, TutorialActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }


    private void getFeedback() {
        showLoadingDialog();
        AppPreferences appPreferences = AppPreferences.getInstance(SplashScreen.this);
        ApiHelper lGetFeedback = new ApiHelper(ApiConstants.GET, ApiConstants.GET_FEEDBACK + appPreferences.getString(AppConstants.USER_ID, ""), "", SplashScreen.this);
        lGetFeedback.mApiID = ApiConstants.GET_FEEDBACK_ID;
        lGetFeedback.invokeAPI();
    }

    @Override
    public void onSuccess(BaseResponse pResponse) {

        dismissLoadingDialog();

        int apiId = pResponse.getmAPIType();

        if (apiId == ApiConstants.GET_FEEDBACK_ID) {

            BookPoojaFeedbackResponse poojaFeedback = (BookPoojaFeedbackResponse) pResponse;

            Bundle bundle = new Bundle();
            bundle.putString("booking_id", poojaFeedback.getBookingId());
            bundle.putString("pooja_name", poojaFeedback.getPoojaName());
            bundle.putString("pooja_image", poojaFeedback.getPoojaImage());

            // Feedback screen
       /*     Intent intent = new Intent(SplashScreen.this, FeedbackActivity.class);
            intent.putExtra("Feedback_Bundle", bundle);
            startActivity(intent);*/

        }
    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getCurrentLocation() {
        ;
        LocationManager locationManager = new LocationManager(mContext,
                new LocationManager.LocationCallbackListener() {

                    @Override
                    public void onLocationReceived(Location location) {

                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location
                                    .getLongitude());
                            latitude = "" + location.getLatitude();
                            longitude = "" + location.getLongitude();
                            AppPreferences appPreferences = AppPreferences.getInstance(mContext);
                            appPreferences.putString(AppConstants.LOC_LATITUDE, latitude);
                            appPreferences.putString(AppConstants.LOC_LONGITUDE, longitude);

                        } else {
                        }
                    }
                });

        locationManager.fetchLocation();
    }

    private void initUser(){
        User user= User.getInstance();
        user.setUserid(muhurtMazaPreferences.getString(AppConstants.USER_ID, "").trim());
        user.setUserFirstname(muhurtMazaPreferences.getString(AppConstants.USER_FNAME, "").trim());
        user.setUserLastname(muhurtMazaPreferences.getString(AppConstants.USER_LNAME, "").trim());
        user.setUserEmailId(muhurtMazaPreferences.getString(AppConstants.USER_EMAIL, "").trim());
        user.setUserContactNo(muhurtMazaPreferences.getString(AppConstants.USER_NUMBER, "").trim());
        user.setUserLanguage(muhurtMazaPreferences.getString(AppConstants.USER_LANGUAGE, "").trim());
        user.setUserAddress(muhurtMazaPreferences.getString(AppConstants.USER_ADDRESS, "").trim());
        user.setUserProfilePicURL(muhurtMazaPreferences.getString(AppConstants.USER_PROFILE, "").trim());
        user.setUserEmailAlert(muhurtMazaPreferences.getString(AppConstants.EMAIL_ALERT, "").trim());
        user.setUserSMSAlert(muhurtMazaPreferences.getString(AppConstants.SMS_ALERT, "").trim());
        user.setUserCity(muhurtMazaPreferences.getString(AppConstants.USER_CITY, "").trim());

    }
}
package com.muhurtmaza.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.model.people.Person;
import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.CommonUtility;
import com.muhurtmaza.utility.ConnectionDetector;
import com.muhurtmaza.utility.LocationManager;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.NewUserResponse;
import com.muhurtmaza.webservice.response.UserProfileResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignUpActivity extends ParentActivity implements BaseHttpHelper.ResponseHelper, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {
    private AlertDialog alertDialog;
    private Context mContext;
    private String latitude, longitude;
    @InjectView(R.id.input_name)
    EditText edtName;
    @InjectView(R.id.input_phone)
    EditText edtMobile;
    @InjectView(R.id.input_email)
    EditText edtEmail;
    @InjectView(R.id.input_password)
    EditText edtPass;
    @InjectView(R.id.btn_new_sign_up)
    Button btnSignUp;

    Boolean isSocialSignOn=false;

    /*Linear vew of the fb and google plus*/
    @InjectView(R.id.lLayout_SignupViaFacebook)
    LinearLayout fb_signupLayout;

    @InjectView(R.id.lLayout_SignupViaGooglePlus)
    LinearLayout gp_signupLayout;

    @InjectView(R.id.btn_facebook_sdk)
    LoginButton btnFacebookSdk;

    private ImageView imgMenu;
    private TextView txtTitle;
    android.support.v7.widget.Toolbar mToolbar;
    private boolean isGLogingGoingOn = false;

    //Google login
    private static final int RC_SIGN_IN = 0;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    public static GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;

    //Facebook login
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ConnectionDetector conDetect;
    int countGoogleConnectAttempt = 0;

    private String entrySource = "";
    AppPreferences appPreferences;
    boolean flagShowDrawer = false;
    boolean flagBtnGoogleClicked = false;
    boolean isGoogleLoginValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(SignUpActivity.this);
        setContentView(R.layout.activity_new_sign_up);
        ButterKnife.inject(this);
        isSocialSignOn=false;
        mContext = SignUpActivity.this;
        getCurrentLocation();

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = (TextView) mToolbar.findViewById(R.id.txt_title);
        imgMenu = (ImageView) mToolbar.findViewById(R.id.img_back);
        txtTitle.setText("New User");
        //   mToolbar.setNavigationIcon(R.drawable.ic_share);
        imgMenu.setBackgroundResource(R.drawable.ic_close);
        imgMenu.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        callbackManager = CallbackManager.Factory.create();

        appPreferences = AppPreferences.getInstance(mContext);
        conDetect = new ConnectionDetector();


        isGoogleLoginValid = AppPreferences.getInstance(this).getBoolean("isGoogleLogin", false);

        String logoutFrom = getIntent().getStringExtra(AppConstants.LOGOUT_FROM);
        if (logoutFrom != null && !logoutFrom.equals(null) && !logoutFrom.equals("")) {
            if (logoutFrom.equals(AppConstants.GOOGLE_SIGN_UP)) {
                isGoogleLoginValid = false;
                logoutFromGoogle();
            }
        }
        setupUI();

    }

    private void setupUI() {
        //Facebook initialization
        callbackManager = CallbackManager.Factory.create();
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken,
                                                       AccessToken newToken) {
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldprofile,
                                                   Profile newprofile) {
                // WelcomeMessageDisplay(newprofile);
            }
        };
        fb_signupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFacebookSdk.callOnClick();
            }
        });
        btnFacebookSdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conDetect.checkConnectivity(mContext)) {
                    //MMToast.getInstance().showShortToast("facebook Button clicked", mContext);
                    isSocialSignOn=true;
                    loginWithFB();
                } else {
                    Toast.makeText(mContext, AppConstants.CHECK_INTERNET_CONNECTION, Toast.LENGTH_LONG).show();
                }
            }
        });

        gp_signupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flagBtnGoogleClicked = true;

                if (flagBtnGoogleClicked = true) {

                    if (conDetect.checkConnectivity(mContext)) {

                        if (checkPlayServices()) {
                            if (!mGoogleApiClient.isConnecting()) {
                                mSignInClicked = true;
                                resolveSignInError();
                            }

                        } else {
                            Toast.makeText(mContext, AppConstants.UPDATE_PLAY_SERVICES, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(mContext, AppConstants.CHECK_INTERNET_CONNECTION, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(SignUpActivity.this, TutorialActivity.class);
                nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextIntent);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignUpClick();
            }
        });
    }

    private void btnSignUpClick() {
        isSocialSignOn=false;
        String strName = edtName.getText().toString().trim();
        String strEmail = edtEmail.getText().toString().trim();
        String strPass = edtPass.getText().toString().trim();
        String strMobile = edtMobile.getText().toString().trim();

        // edtMobile.setRawInputType(Configuration.KEYBOARD_12KEY);
        boolean cancel = false;
        View focusView = null;

        edtName.setError(null);
        edtEmail.setError(null);
        edtPass.setError(null);
        edtMobile.setError(null);

        if (strName.isEmpty()) {
            edtName.setError(getString(R.string.error_field_required));
            focusView = edtName;
            cancel = true;
        }


        if (strEmail.isEmpty()) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        }


        if (strPass.isEmpty()) {
            edtPass.setError(getString(R.string.error_field_required));
            focusView = edtPass;
            cancel = true;
        }

        int len = strPass.length();
        if (len < 4) {
            edtPass.setError("Password should be of 4-20 characters");
            focusView = edtPass;
            cancel = true;
        }
        int lenmb = strMobile.length();
        if (lenmb < 10) {
            edtMobile.setError("10 digit only");
            focusView = edtMobile;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {

            try {
                showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email_id", strEmail);
                jsonObject.put("first_name", strName);
                jsonObject.put("phone", strMobile);
                jsonObject.put("password", strPass);
                jsonObject.put("sign_up_source", AppConstants.NEW_USER_SIGN_UP_SOURCE);

                AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                jsonObject.put("latitude", appPreferences.getString(AppConstants.LOC_LATITUDE, ""));
                jsonObject.put("longitude", appPreferences.getString(AppConstants.LOC_LONGITUDE, ""));


                ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.SIGN_UP_URL, jsonObject.toString(), this);
                lSignUpApi.mApiID = ApiConstants.NEW_SIGN_UP_ID;
                lSignUpApi.invokeAPI();


            } catch (Exception em) {
                em.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(BaseResponse pResponse) {

        dismissLoadingDialog();
        int apiId = pResponse.getmAPIType();

        if (apiId == ApiConstants.NEW_SIGN_UP_ID )
        {
            isGLogingGoingOn = false;
            NewUserResponse userProfile = (NewUserResponse) pResponse;
            String message = userProfile.getmMessage();

            if (isSocialSignOn==true) {
                // MMToast.getInstance().showLongToast("Sign Up message=" + pResponse.toString(), this);
                AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                appPreferences.putString(AppConstants.USER_ID, userProfile.getmUser_id());
                appPreferences.putString(AppConstants.USER_NAME, userProfile.getmFirst_name());
                appPreferences.putString(AppConstants.USER_PROFILE, userProfile.getmUser_profile_image());
                appPreferences.putString(AppConstants.USER_EMAIL, userProfile.getmEmail_id());
                appPreferences.putString(AppConstants.USER_NUMBER, userProfile.getmPhone());
                appPreferences.putString(AppConstants.USER_ADDRESS, userProfile.getmAddress());
                appPreferences.putString(AppConstants.SMS_ALERT, userProfile.getmSms_alert());
                appPreferences.putString(AppConstants.EMAIL_ALERT, userProfile.getmEmail_alert());
                appPreferences.putString(AppConstants.SIGN_UP_SOURCE, AppConstants.NEW_USER_SIGN_UP_SOURCE);

                Intent intent = new Intent(this, MainDrawerActivity.class);
                startActivity(intent);
                this.finish();

            } else if(isSocialSignOn==false) {

                AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                appPreferences.putString(AppConstants.USER_ID, userProfile.getmUser_id());
                appPreferences.putString(AppConstants.USER_NAME, userProfile.getmFirst_name());
                appPreferences.putString(AppConstants.USER_PROFILE, userProfile.getmUser_profile_image());
                appPreferences.putString(AppConstants.USER_EMAIL, userProfile.getmEmail_id());
                appPreferences.putString(AppConstants.USER_NUMBER, userProfile.getmPhone());
                appPreferences.putString(AppConstants.USER_ADDRESS, userProfile.getmAddress());
                appPreferences.putString(AppConstants.SMS_ALERT, userProfile.getmSms_alert());
                appPreferences.putString(AppConstants.EMAIL_ALERT, userProfile.getmEmail_alert());
                appPreferences.putString(AppConstants.SIGN_UP_SOURCE, AppConstants.NEW_USER_SIGN_UP_SOURCE);

                Intent intent = new Intent(this, NewOTPActivity.class);
                startActivity(intent);
                this.finish();

            }
            else{
                MMToast.getInstance().showLongToast(message, this);
            }
        }
    }

    private void loginWithFB() {

        //MMToast.getInstance().showShortToast("Login with clicked", mContext);
        isSocialSignOn=true;
        btnFacebookSdk.setReadPermissions("user_friends", "public_profile","email");
        btnFacebookSdk.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                showLoadingDialog();

                //MMToast.getInstance().showLongToast("Success Response", mContext);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                //Log.v("LoginActivity", response.toString());

                                try {

                                    if (response != null) {

                                        //MMToast.getInstance().showLongToast("Response = " + response.toString(), mContext);
                                        JSONObject jsonObject = response.getJSONObject();
                                        String name = jsonObject.getString("name");
                                        String email = jsonObject.getString("email");
                                        String id = jsonObject.getString("id");
                                        String imagePath = "http://graph.facebook.com/" + id + "/picture";
                                        if (!TextUtils.isEmpty(email)) {
                                            try {

                                                JSONObject jsonObj = new JSONObject();
                                                jsonObj.put("email", email);
                                                jsonObj.put("phone", "");
                                                jsonObj.put("first_name", name);
                                                jsonObj.put("user_profile_image", imagePath);
                                                jsonObj.put("sign_up_source", AppConstants.FACEBOOK_SIGN_UP);

                                                AppPreferences appPreferences = AppPreferences.getInstance(SignUpActivity.this);

                                                jsonObj.put("latitude", appPreferences.getString(AppConstants.LOC_LATITUDE, ""));
                                                jsonObj.put("longitude", appPreferences.getString(AppConstants.LOC_LONGITUDE, ""));

                                                entrySource = AppConstants.FACEBOOK_SIGN_UP;

                                                ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.SIGN_IN_GOOGLE_FACEBOOK, jsonObj.toString(), SignUpActivity.this);
                                                lSignUpApi.mApiID = ApiConstants.NEW_SIGN_UP_ID;
                                                lSignUpApi.invokeAPI();

                                            } catch (Exception em) {
                                                em.printStackTrace();
                                            }
                                        } else {
                                            showErrorDialog();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            private void showErrorDialog() {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("We are unable to get your email id. Please Gmail to login.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        alertDialog.cancel();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onCancel() {
                MMToast.getInstance().showLongToast("Cancel Response", mContext);
            }
            @Override
            public void onError(FacebookException exception) {
                MMToast.getInstance().showLongToast("Exception Response=" + exception.toString(), mContext);
                Log.d("Exception", "" + exception.toString());
            }
        });
    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast("Sign Up fail=" + pBaseResponse.toString(), this);
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
                            //Not able to fetch location
                        }
                    }
                });

        locationManager.fetchLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgMenu.setVisibility(View.INVISIBLE);
        txtTitle.setVisibility(View.INVISIBLE);

        accessTokenTracker.stopTracking();
        dismissLoadingDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isGoogleLoginValid) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {

        mSignInClicked = false;
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
        getGmailProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (isGoogleLoginValid) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                isGLogingGoingOn = false;
                mGoogleApiClient.connect();
            }
        } else {
            mIntentInProgress = false;
            isGLogingGoingOn = false;
            mGoogleApiClient.connect();
        }
    }

    protected boolean checkPlayServices() {
        final int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        resultCode, SignUpActivity.this, 100);
                if (dialog != null) {
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {

                            if (ConnectionResult.SERVICE_INVALID == resultCode)
                                finish();
                        }
                    });
                    return false;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    SignUpActivity.this);
            builder.setMessage("Google Play Services Error, This device is not supported for required Goole Play Services.");
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        //MMToast.getInstance().showLongToast("Message", mContext);

        if (requestCode == RC_SIGN_IN) {

            if (responseCode == RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                countGoogleConnectAttempt = countGoogleConnectAttempt + 1;
                if (countGoogleConnectAttempt < 2)
                    mGoogleApiClient.connect();
            }
        } else {
            callbackManager.onActivityResult(requestCode, responseCode, intent);
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {
        getGmailProfileInformation();
    }

    private void getGmailProfileInformation() {
        try {
            if (!isGLogingGoingOn) {
                isGLogingGoingOn = true;
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                    String personName = currentPerson.getDisplayName();
                    String personPhotoUrl = currentPerson.getImage().getUrl();
                    //image clr code
                    personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + 200;
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    //	Log.d(TAG, "Gmail Photo url :-"+personPhotoUrl);

                    //new LoginUser().execute(personName,email,personPhotoUrl,"GOOGLE_ANDROID",""+latitude,""+longitude);
                    // isGoogleLoginValid=AppPreferences.getInstance(this).getBoolean("isGoogleLogin",false);
                    AppPreferences.getInstance(this).putBoolean("isGoogleLogin", true);

                    try {
                        showLoadingDialog();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("email", email);
                        jsonObject.put("phone", "");
                        jsonObject.put("first_name", personName);
                        jsonObject.put("user_profile_image", personPhotoUrl);
                        jsonObject.put("sign_up_source", AppConstants.GOOGLE_SIGN_UP);

                        AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                        jsonObject.put("latitude", appPreferences.getString(AppConstants.LOC_LATITUDE, ""));
                        jsonObject.put("longitude", appPreferences.getString(AppConstants.LOC_LONGITUDE, ""));

                        entrySource = AppConstants.GOOGLE_SIGN_UP;

                        ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.SIGN_IN_GOOGLE_FACEBOOK, jsonObject.toString(), this);
                        lSignUpApi.mApiID = ApiConstants.SIGN_UP_ID;
                        lSignUpApi.invokeAPI();

                    } catch (Exception em) {
                        em.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void logoutFromGoogle() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(NewLoginActivity.mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(NewLoginActivity.mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status result) {
                    appPreferences.deleteAllPreferences();
                    flagBtnGoogleClicked = false;
                    MMToast.getInstance().showLongToast("Logout", mContext);
                }
            });
        }
    }
}

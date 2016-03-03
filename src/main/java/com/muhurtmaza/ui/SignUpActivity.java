package com.muhurtmaza.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.muhurtmaza.R;
import com.muhurtmaza.model.User;
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


public class SignUpActivity extends ParentActivity implements BaseHttpHelper.ResponseHelper,ResultCallback<People.LoadPeopleResult>,GoogleApiClient.OnConnectionFailedListener {
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

    Boolean isSocialSignOn = false;

    /*Linear vew of the fb and google plus*/

    LinearLayout fb_signupLayout,gp_signupLayout;
    LoginButton btnFacebookSdk;

    android.support.v7.widget.Toolbar mToolbar;
    private boolean isGLogingGoingOn = false;

    //Google login
    GoogleSignInAccount acct;
    GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN=101;
    public static final String TAG="GoogleSignIn";

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
        isSocialSignOn = false;
        mContext = SignUpActivity.this;
        getCurrentLocation();

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("New User");

        callbackManager = CallbackManager.Factory.create();

        appPreferences = AppPreferences.getInstance(mContext);
        conDetect = new ConnectionDetector();


        isGoogleLoginValid = AppPreferences.getInstance(this).getBoolean("isGoogleLogin", false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        String logoutFrom = getIntent().getStringExtra(AppConstants.LOGOUT_FROM);

       /* String logoutFrom = getIntent().getStringExtra(AppConstants.LOGOUT_FROM);
        if (logoutFrom != null && !logoutFrom.equals(null) && !logoutFrom.equals("")) {
            if (logoutFrom.equals(AppConstants.GOOGLE_SIGN_UP)) {
                isGoogleLoginValid = false;
                //logoutFromGoogle();
            }
        }*/
        setupUI();

    }


    private void setupUI() {
        fb_signupLayout= (LinearLayout) findViewById(R.id.lLayout_SignupViaFacebook);
        gp_signupLayout= (LinearLayout) findViewById(R.id.lLayout_SignupViaGooglePlus);
        btnFacebookSdk= (LoginButton) findViewById(R.id.btn_facebook_sdk);
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
                    isSocialSignOn = true;
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
                            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                            startActivityForResult(signInIntent, RC_SIGN_IN);

                        } else {
                            Toast.makeText(mContext, AppConstants.UPDATE_PLAY_SERVICES, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(mContext, AppConstants.CHECK_INTERNET_CONNECTION, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignUpClick();
            }
        });


        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validateName();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validateMobile();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validateEmail();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validatePassword();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void validate() {
        if (validateName() && validateMobile() && validateEmail()) {
            signup();
        }
    }

    private void btnSignUpClick() {
        isSocialSignOn = false;
        validate();
    }

    void signup() {
        try {
            showLoadingDialog();

            String strName = edtName.getText().toString().trim();
            String strEmail = edtEmail.getText().toString().trim();
            String strPass = edtPass.getText().toString().trim();
            String strMobile = edtMobile.getText().toString().trim();

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

    private boolean validateMobile() {
        if (edtMobile.getText().toString().trim().matches("\\d{10}")) {
            edtMobile.setError(null);
            return true;
        } else {
            edtMobile.setError("Enter valid mobile no");
            edtMobile.requestFocus();
            return false;
        }
    }

    private boolean validateEmail() {
        if (CommonUtility.getInstance(this).isValidEmail(edtEmail.getText().toString())) {
            edtEmail.setError(null);
            return true;
        } else {
            edtEmail.setError("Enter valid email id");
            edtEmail.requestFocus();
            return false;
        }
    }

    private boolean validatePassword() {
        if (edtPass != null && edtPass.length() > 6) {
            return true;
        }
        else {
            edtPass.setError("Enter valid email id");
            edtPass.requestFocus();
            return false;
        }
    }

    private boolean validateName() {
        if (edtName.getText().toString().length() == 0) {
            edtName.setError("Name is required");
            edtName.requestFocus();
            return false;
        } else if (edtName.getText().toString().trim().matches("[a-zA-Z]*")) {
            edtName.setError(null);
            return true;
        } else {
            edtName.setError("Enter valid name");
            edtName.requestFocus();
            return false;
        }
    }

/*    }*/

    @Override
    public void onSuccess(BaseResponse pResponse) {

        dismissLoadingDialog();
        int apiId = pResponse.getmAPIType();

        if (apiId == ApiConstants.NEW_SIGN_UP_ID) {
            isGLogingGoingOn = false;
            NewUserResponse userProfile = (NewUserResponse) pResponse;
            String message = userProfile.getmMessage();

            if (message.equals("User Already Exist")) {
                MMToast.getInstance().showLongToast(message, this);
            } else if (isSocialSignOn == true) {
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
                User user = User.getInstance();
                user.setUserid(userProfile.getmUser_id());
                user.setUserFirstname(userProfile.getmFirst_name());
                user.setUserProfilePicURL(userProfile.getmUser_profile_image());
                user.setUserEmailId(userProfile.getmEmail_id());
                user.setUserContactNo(userProfile.getmPhone());
                user.setUserAddress(userProfile.getmAddress());
                user.setUserSMSAlert(userProfile.getmSms_alert());
                user.setUserEmailAlert(userProfile.getmEmail_alert());
                user.setUserSignUpSource(AppConstants.NEW_USER_SIGN_UP_SOURCE);
                Intent intent = new Intent(this, MainDrawerActivity.class);
                startActivity(intent);
                this.finish();

            } else if (isSocialSignOn == false) {

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
                User user = User.getInstance();
                user.setUserid(userProfile.getmUser_id());
                user.setUserFirstname(userProfile.getmFirst_name());
                user.setUserProfilePicURL(userProfile.getmUser_profile_image());
                user.setUserEmailId(userProfile.getmEmail_id());
                user.setUserContactNo(userProfile.getmPhone());
                user.setUserAddress(userProfile.getmAddress());
                user.setUserSMSAlert(userProfile.getmSms_alert());
                user.setUserEmailAlert(userProfile.getmEmail_alert());
                user.setUserSignUpSource(AppConstants.NEW_USER_SIGN_UP_SOURCE);
                Intent intent = new Intent(this, NewOTPActivity.class);
                startActivity(intent);
                this.finish();

            } else {
                MMToast.getInstance().showLongToast(message, this);
            }
        }
    }

    private void loginWithFB() {

        //MMToast.getInstance().showShortToast("Login with clicked", mContext);
        isSocialSignOn = true;
        btnFacebookSdk.setReadPermissions("user_friends", "public_profile", "email");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(mContext, TutorialActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // app icon in action bar clicked; goto parent activity.

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, responseCode, intent);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            getGmailProfileInformation();
           /* MMToast.getInstance().showLongToast("Data"+"User name:"+acct.getDisplayName()+" Email="+acct.getEmail()
                    +" Id="+acct.getId()+" Image Url="+acct.getPhotoUrl(),mContext);*/
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {
        getGmailProfileInformation();
    }

    private void getGmailProfileInformation() {   String personName = acct.getDisplayName();
        Uri personPhotoUrl = acct.getPhotoUrl();
        //personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + 200;
        String email = acct.getEmail();
        Log.d(TAG, "GPlus get g+ profile information");
        try {
            showLoadingDialog();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("phone", "");
            jsonObject.put("first_name", personName);
            jsonObject.put("user_profile_image", "");
            jsonObject.put("sign_up_source", AppConstants.GOOGLE_SIGN_UP);

            AppPreferences appPreferences = AppPreferences.getInstance(mContext);

            jsonObject.put("latitude", appPreferences.getString(AppConstants.LOC_LATITUDE, ""));
            jsonObject.put("longitude", appPreferences.getString(AppConstants.LOC_LONGITUDE, ""));

            entrySource = AppConstants.GOOGLE_SIGN_UP;

            Log.d(TAG, "GPlus json=" + jsonObject.toString());

            ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.SIGN_IN_GOOGLE_FACEBOOK, jsonObject.toString(), this);
            lSignUpApi.mApiID = ApiConstants.NEW_SIGN_UP_ID;
            lSignUpApi.invokeAPI();


        } catch (Exception em) {
            em.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
  /*  public void logoutFromGoogle() {
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
    }*/
}

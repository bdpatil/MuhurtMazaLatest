package com.muhurtmaza.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.muhurtmaza.R;
import com.muhurtmaza.model.User;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.CommonUtility;
import com.muhurtmaza.utility.ConnectionDetector;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.NewUserResponse;
import com.muhurtmaza.webservice.response.UserProfileResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONObject;

import java.net.URI;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by bharat on 11/22/15.
 */

public class NewLoginActivity extends ParentActivity
        implements BaseHttpHelper.ResponseHelper,
        ResultCallback<People.LoadPeopleResult>,GoogleApiClient.OnConnectionFailedListener{

    // UI references.
    private EditText mEmailView, userInput;
    private EditText mPasswordView;
    private Button mSignInButton;
    private LinearLayout fb_loginLayout, gp_loginLayout;
    private  TextView txtForgorPass_Login;
    private Context mContext;
    private AlertDialog alertDialog;

    GoogleSignInAccount acct;
    LoginButton btnFacebookSdk;
    // SignInButton btnGoogleSdk;

    android.support.v7.widget.Toolbar mToolbar;


    private boolean isGLogingGoingOn = false;
    Boolean isSocialSignOn=false;
    //Google login
    GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN=101;
    public static final String TAG="GoogleSignIn";
    TextView mstatusTextView;

    //Facebook login
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ConnectionDetector conDetect;
    int countGoogleConnectAttempt = 0;

    private String entrySource = "";
    AppPreferences appPreferences;
    boolean flagShowDrawer = false;
    boolean flagBtnGoogleClicked  = false;
    boolean isGoogleLoginValid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(NewLoginActivity.this);
        setContentView(R.layout.activity_new_login);
        ButterKnife.inject(this);
        mContext = this;
        isSocialSignOn=false;
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("Existing User");
        callbackManager = CallbackManager.Factory.create();
        appPreferences = AppPreferences.getInstance(mContext);
        conDetect = new ConnectionDetector();

        isGoogleLoginValid=AppPreferences.getInstance(this).getBoolean("isGoogleLogin",false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        String logoutFrom = getIntent().getStringExtra(AppConstants.LOGOUT_FROM);
      /*  if (logoutFrom != null && !logoutFrom.equals(null) && !logoutFrom.equals("")) {
            if (logoutFrom.equals(AppConstants.GOOGLE_SIGN_UP)) {
                isGoogleLoginValid=false;
                logoutFromGoogle();
            }
        }*/
        setupUI();
    }
    protected boolean checkPlayServices() {
        final int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, NewLoginActivity.this, 100);
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
                    NewLoginActivity.this);
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
    public void onResult(People.LoadPeopleResult loadPeopleResult) {
        getProfileInformationGPlus();
    }

    private void setupUI() {
        // Set up the login form.
        isSocialSignOn=false;
        mEmailView = (EditText) findViewById(R.id.input_email);
        mPasswordView = (EditText) findViewById(R.id.input_password);
        mSignInButton = (Button) findViewById(R.id.btn_new_login);
        txtForgorPass_Login= (TextView) findViewById(R.id.txt_new_forgot_pass);
        fb_loginLayout= (LinearLayout) findViewById(R.id.lLayout_loginViaFacebook);
        gp_loginLayout= (LinearLayout) findViewById(R.id.lLayout_loginViaGooglePlus);
        btnFacebookSdk= (LoginButton) findViewById(R.id.btn_facebook_sdk);
        // btnGoogleSdk= (SignInButton) findViewById(R.id.sign_in_buttonGPLus);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    /*    gp_loginLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MMToast.getInstance().showShortToast("Login google+",mContext);

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });*/
        gp_loginLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                flagBtnGoogleClicked = true;

                isSocialSignOn=true;
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

        fb_loginLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //MMToast.getInstance().showShortToast("Button clicked", mContext);
                btnFacebookSdk.callOnClick();
            }
        });

        btnFacebookSdk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conDetect.checkConnectivity(mContext)) {
                    isSocialSignOn=true;
                    //MMToast.getInstance().showShortToast("facebook Button clicked", mContext);
                    loginWithFB();
                } else {
                    Toast.makeText(mContext, AppConstants.CHECK_INTERNET_CONNECTION, Toast.LENGTH_LONG).show();
                }
            }
        });




        txtForgorPass_Login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,NewForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            getProfileInformationGPlus();
           /* MMToast.getInstance().showLongToast("Data"+"User name:"+acct.getDisplayName()+" Email="+acct.getEmail()
                    +" Id="+acct.getId()+" Image Url="+acct.getPhotoUrl(),mContext);*/
        }
    }

    public void getProfileInformationGPlus() {

        String personName = acct.getDisplayName();
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

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!CommonUtility.getInstance(this).isValidEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            /**
             * Represents an asynchronous login/registration task used to authenticate
             * the user.
             */

            try {

                showLoadingDialog();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", email);
                jsonObject.put("password", password);

                entrySource = AppConstants.MANUAL_LOGIN;

                ApiHelper lLoginApi = new ApiHelper(ApiConstants.POST, ApiConstants.LOGIN_URL, jsonObject.toString(), this);
                lLoginApi.mApiID = ApiConstants.LOGIN_ID;
                lLoginApi.invokeAPI();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();

        int apiId = pResponse.getmAPIType();
        if (apiId == ApiConstants.LOGIN_ID || apiId == ApiConstants.NEW_SIGN_UP_ID) {

            isGLogingGoingOn = false;
            NewUserResponse userProfile = (NewUserResponse) pResponse;
            String message = userProfile.getmMessage();
            Log.d("Response", "" + pResponse.toString());
            if(message.equals("Invalid Username or Password")){
                MMToast.getInstance().showLongToast(message, this);
            }
            else
            /*true-facebook false-lginbtn*/
                if(isSocialSignOn==true){
                    MMToast.getInstance().showLongToast("FaceBook Button cilck ", mContext);
                    AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                    Log.e("Response UserProfile ", "" + userProfile.toString());

                    appPreferences.putString(AppConstants.USER_ID, "" + userProfile.getmUser_id());
                    appPreferences.putString(AppConstants.USER_NAME, "" + userProfile.getmFirst_name());
                    appPreferences.putString(AppConstants.USER_PROFILE, "" + userProfile.getmUser_profile_image());
                    appPreferences.putString(AppConstants.USER_EMAIL, "" + userProfile.getmEmail_id());
                    appPreferences.putString(AppConstants.USER_NUMBER, "" + userProfile.getmPhone());
                    appPreferences.putString(AppConstants.USER_ADDRESS, "" + userProfile.getmAddress());
                    appPreferences.putString(AppConstants.SMS_ALERT, "" + userProfile.getmSms_alert());
                    appPreferences.putString(AppConstants.EMAIL_ALERT, "" + userProfile.getmEmail_alert());
                    appPreferences.putString(AppConstants.SIGN_UP_SOURCE, "" + entrySource);

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
                    Log.d("Image",appPreferences.getString(AppConstants.USER_PROFILE, ""));

                    Intent intent = new Intent(this, MainDrawerActivity.class);
                    startActivity(intent);
                    this.finish();

                }else if(isSocialSignOn==false) {
                /*Login Button*/
                    MMToast.getInstance().showLongToast("Signup Button", mContext);
                    AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                    //Log.e("Response UserProfile ", "" + userProfile.toString());

                    appPreferences.putString(AppConstants.USER_ID, "" + userProfile.getmUser_id());
                    appPreferences.putString(AppConstants.USER_NAME, "" + userProfile.getmFirst_name());
                    appPreferences.putString(AppConstants.USER_PROFILE, "" + userProfile.getmUser_profile_image());
                    appPreferences.putString(AppConstants.USER_EMAIL, "" + userProfile.getmEmail_id());
                    appPreferences.putString(AppConstants.USER_NUMBER, "" + userProfile.getmPhone());
                    appPreferences.putString(AppConstants.USER_ADDRESS, "" + userProfile.getmAddress());
                    appPreferences.putString(AppConstants.SMS_ALERT, "" + userProfile.getmSms_alert());
                    appPreferences.putString(AppConstants.EMAIL_ALERT, "" + userProfile.getmEmail_alert());
                    appPreferences.putString(AppConstants.SIGN_UP_SOURCE, "" + entrySource);
                    User user = User.getInstance();
                    user.setUserid(userProfile.getmUser_id());
                    user.setUserFirstname(userProfile.getmFirst_name());
                    user.setUserProfilePicURL(userProfile.getmUser_profile_image());
                    user.setUserEmailId(userProfile.getmEmail_id());
                    user.setUserContactNo(userProfile.getmPhone());
                    user.setUserAddress(userProfile.getmAddress());
                    user.setUserSMSAlert(userProfile.getmSms_alert());
                    user.setUserEmailAlert(userProfile.getmEmail_alert());
                    user.setUserSignUpSource(entrySource);
                    Intent intent = new Intent(this, MainDrawerActivity.class);
                    startActivity(intent);
                    this.finish();
                }else{
                    MMToast.getInstance().showLongToast(message, this);
                }

        }

        if (apiId == ApiConstants.FORGOT_PASSWORD_ID) {
            boolean blStatus = pResponse.ismStatus();
            //String strMessage = pResponse.getmMessage();

            if (blStatus) {
                MMToast.getInstance().showLongToast("We have sent you an email that will allow you to reset your password quickly and easily", mContext);
            } else {
                userInput.setText("");
                userInput.requestFocus();
                MMToast.getInstance().showLongToast("Invalid email id", mContext);
            }

        }
    }

    public void showDrawerScreen() {
        //MMToast.getInstance().showLongToast("Call drawer", mContext);
        if(flagShowDrawer == true){
            flagShowDrawer = false;
            Intent intent = new Intent(this, MainDrawerActivity.class);
            startActivity(intent);
            this.finish();
        }
    }


    @Override
    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        isGLogingGoingOn = false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent = new Intent(mContext, TutorialActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loginWithFB() {
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
                                Log.v("LoginActivity", response.toString());

                                try {

                                    if (response != null) {

                                        MMToast.getInstance().showLongToast("Response = " + response.toString(), mContext);

                                        Log.d("Response", "" + response.toString());
                                        Log.d("Response1", "" + response.getJSONObject());
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

                                                AppPreferences appPreferences = AppPreferences.getInstance(NewLoginActivity.this);

                                                jsonObj.put("latitude", appPreferences.getString(AppConstants.LOC_LATITUDE, ""));
                                                jsonObj.put("longitude", appPreferences.getString(AppConstants.LOC_LONGITUDE, ""));

                                                entrySource = AppConstants.FACEBOOK_SIGN_UP;

                                                Log.d("Facebook login send", jsonObj.toString());

                                                ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.SIGN_IN_GOOGLE_FACEBOOK, jsonObj.toString(), NewLoginActivity.this);
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
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        dismissLoadingDialog();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
 /*   public void logoutFromGoogle() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(NewLoginActivity.mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(NewLoginActivity.mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status result) {
                    appPreferences.deleteAllPreferences();
                    flagBtnGoogleClicked = false;
                    MMToast.getInstance().showLongToast("Logout",mContext);
                }
            });
        }
    }*/
}


package com.muhurtmaza.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.CommonUtility;
import com.muhurtmaza.utility.ConnectionDetector;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.UserProfileResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Sandeep on 11/22/15.
 */

public class LoginActivity
        extends ParentActivity
        implements BaseHttpHelper.ResponseHelper,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult> {

    // UI references.
    private EditText mEmailView, userInput;
    private EditText mPasswordView;
    private Button mSignInButton, btnSkipLogin, mSignUp, btnCancel, btnOkay;
    private LinearLayout layoutLogin;
    FloatingActionButton btnShowExistingLogin;
    @InjectView(R.id.btn_google)
    FloatingActionButton btnGoogle;
    @InjectView(R.id.btn_facebook)
    FloatingActionButton btnFacebook;
    @InjectView(R.id.btn_facebook_sdk)
    LoginButton btnFacebookSdk;
    private Context mContext;
    private AlertDialog alertDialog;
    @InjectView(R.id.btn_forgotpassword)
    Button btnForgortPassword;

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
    boolean flagBtnGoogleClicked  = false;
    boolean isGoogleLoginValid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        mContext = this;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        callbackManager = CallbackManager.Factory.create();

        appPreferences = AppPreferences.getInstance(mContext);
        conDetect = new ConnectionDetector();


              isGoogleLoginValid=AppPreferences.getInstance(this).getBoolean("isGoogleLogin",false);

        String logoutFrom = getIntent().getStringExtra(AppConstants.LOGOUT_FROM);
        if (logoutFrom != null && !logoutFrom.equals(null) && !logoutFrom.equals("")) {
            if (logoutFrom.equals(AppConstants.GOOGLE_SIGN_UP)) {
                isGoogleLoginValid=false;
                logoutFromGoogle();
            }
        }
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isGoogleLoginValid){
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
    public void onConnectionSuspended(int i) {
        if(isGoogleLoginValid) {
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
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult!=null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                isGLogingGoingOn = false;
                mGoogleApiClient.connect();
            }
        }else{
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
                        resultCode, LoginActivity.this, 100);
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
                    LoginActivity.this);
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
    public void onConnected(Bundle arg0) {

        mSignInClicked = false;
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
        getGmailProfileInformation();
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
                    AppPreferences.getInstance(this).putBoolean("isGoogleLogin",true);

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

    private void setupUI() {
        // Set up the login form.

        mEmailView = (EditText) findViewById(R.id.tv_username);
        mPasswordView = (EditText) findViewById(R.id.tv_password);
        btnSkipLogin = (Button) findViewById(R.id.btn_skip_login);
        mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnShowExistingLogin = (FloatingActionButton) findViewById(R.id.btn_exiting_login);
        layoutLogin = (LinearLayout) findViewById(R.id.layout_login);

        btnShowExistingLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(layoutLogin.isShown())
                    layoutLogin.setVisibility(View.INVISIBLE);
                else
                    layoutLogin.setVisibility(View.VISIBLE);
            }
        });

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

        btnSkipLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences appPreferences = AppPreferences.getInstance(mContext);
                appPreferences.putString(AppConstants.SIGN_UP_SOURCE, AppConstants.SKIP_LOGIN);
                flagShowDrawer = true;
                showDrawerScreen();
            }
        });

        mSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUp();
            }
        });

        btnGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                flagBtnGoogleClicked = true;

                if(flagBtnGoogleClicked = true){

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

        btnFacebook.setOnClickListener(new OnClickListener() {
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
                    //MMToast.getInstance().showShortToast("facebook Button clicked", mContext);
                    loginWithFB();
                } else {
                    Toast.makeText(mContext, AppConstants.CHECK_INTERNET_CONNECTION, Toast.LENGTH_LONG).show();
                }
            }
        });

        //mEmailView.setText("tungstenbitbucket@gmail.com");
        //mPasswordView.setText("android@123");

        btnForgortPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conDetect.checkConnectivity(mContext)) {

                    LayoutInflater li = LayoutInflater.from(mContext);
                    View promptsView = li.inflate(R.layout.dialog_forgot_password, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext);

                    alertDialogBuilder.setView(promptsView);

                    userInput = (EditText) promptsView
                            .findViewById(R.id.edt_email_address);

                    btnOkay = (Button) promptsView.findViewById(R.id.btn_okay);
                    btnCancel = (Button) promptsView.findViewById(R.id.btn_cancel);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setTitle("Forgot Password?");

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    btnOkay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Calling api here

                            MMToast.getInstance().showLongToast("Ok clicked", mContext);

                            String strEmail = userInput.getText().toString().trim();
                            boolean cancel = false;
                            View focusView = null;

                            userInput.setError(null);

                            if (strEmail.isEmpty()) {
                                userInput.setError(getString(R.string.error_field_required));
                                focusView = userInput;
                                cancel = true;
                            }
                            if (cancel) {
                                focusView.requestFocus();
                            } else {
                                //MMToast.getInstance().showLongToast("Call api",mContext);
                                showLoadingDialog();
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("email_id", strEmail);
                                    ApiHelper lForgotPassApi = new ApiHelper(ApiConstants.POST, ApiConstants.FORGET_PASSWORD_URL, jsonObject.toString(), LoginActivity.this);
                                    lForgotPassApi.mApiID = ApiConstants.FORGOT_PASSWORD_ID;
                                    lForgotPassApi.invokeAPI();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MMToast.getInstance().showLongToast("Cancel clicked", mContext);
                            alertDialog.cancel();
                        }
                    });

                } else {
                    MMToast.getInstance().showLongToast(AppConstants.CHECK_INTERNET_CONNECTION, mContext);
                }
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
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

        if (apiId == ApiConstants.LOGIN_ID || apiId == ApiConstants.SIGN_UP_ID) {

            isGLogingGoingOn = false;
            UserProfileResponse userProfile = (UserProfileResponse) pResponse;
            //MMToast.getInstance().showLongToast("Login success", this);

            AppPreferences appPreferences = AppPreferences.getInstance(mContext);

            //MMToast.getInstance().showLongToast("User response = "+userProfile.toString(), mContext);

            Log.e("Response", "" + userProfile.toString());

            appPreferences.putString(AppConstants.USER_ID, "" + userProfile.getmUser_id());
            appPreferences.putString(AppConstants.USER_NAME, "" + userProfile.getmFirst_name());
            appPreferences.putString(AppConstants.USER_PROFILE, "" + userProfile.getmUser_profile_image());
            appPreferences.putString(AppConstants.USER_EMAIL, "" + userProfile.getmEmail_id());
            appPreferences.putString(AppConstants.USER_NUMBER, "" + userProfile.getmPhone());
            appPreferences.putString(AppConstants.USER_ADDRESS, "" + userProfile.getmAddress());
            appPreferences.putString(AppConstants.USER_CITY, "" + userProfile.getmCity());
            appPreferences.putString(AppConstants.USER_STATE, "" + userProfile.getmCity());
            appPreferences.putString(AppConstants.SMS_ALERT, "" + userProfile.getmSms_alert());
            appPreferences.putString(AppConstants.EMAIL_ALERT, "" + userProfile.getmEmail_alert());
            appPreferences.putString(AppConstants.SIGN_UP_SOURCE, "" + entrySource);

            flagShowDrawer = true;
            showDrawerScreen();
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
        //MMToast.getInstance().showLongToast("Login fail", this);
    }

    private void showSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void loginWithFB() {

        //MMToast.getInstance().showShortToast("Login with clicked", mContext);

        btnFacebookSdk.setReadPermissions("user_friends", "public_profile");
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

                                                AppPreferences appPreferences = AppPreferences.getInstance(LoginActivity.this);

                                                jsonObj.put("latitude", appPreferences.getString(AppConstants.LOC_LATITUDE, ""));
                                                jsonObj.put("longitude", appPreferences.getString(AppConstants.LOC_LONGITUDE, ""));

                                                entrySource = AppConstants.FACEBOOK_SIGN_UP;

                                                ApiHelper lSignUpApi = new ApiHelper(ApiConstants.POST, ApiConstants.SIGN_IN_GOOGLE_FACEBOOK, jsonObj.toString(), LoginActivity.this);
                                                lSignUpApi.mApiID = ApiConstants.SIGN_UP_ID;
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

                //Log.d(TAG, "Login result :-" + loginResult);
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

    public void logoutFromGoogle() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(LoginActivity.mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(LoginActivity.mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status result) {
                    appPreferences.deleteAllPreferences();
                    flagBtnGoogleClicked = false;
                    MMToast.getInstance().showLongToast("Logout",mContext);
                }
            });
        }
    }
}


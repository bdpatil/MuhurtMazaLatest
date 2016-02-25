package com.muhurtmaza.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.muhurtmaza.R;
import com.muhurtmaza.ui.NotificationsSettings;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingFragment extends ParentFragment implements BaseHttpHelper.ResponseHelper {
    @InjectView(R.id.imgAlert_Setting)
    ImageView imgSmsAlert;
    @InjectView(R.id.tvSMSAlerts_Settings)
    TextView txtSmsAlert;
    ToggleButton togBtnSmsAlert;
    @InjectView(R.id.imgEmailAlert_Setting)
    ImageView imgEmailAlert;
    @InjectView(R.id.txtEmailAlert_Settings)
    TextView txtEmailAlert;
    ToggleButton togBtnEmailAlert;
    LinearLayout GoToNotify_Setting;
    TextView txtTitle;
    private ImageView imgSave;
    private Toolbar mToolBar;
    private AppPreferences appPreferences;
    int isActivateEmail, isActivateSms;
    private Context mContext;
    private String alertType = "";
    private String setAlertState = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.inject(getActivity());
        mContext = getActivity();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        mToolBar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        setHasOptionsMenu(true);

        imgSave = (ImageView) mToolBar.findViewById(R.id.img_edit);
        imgSave.setVisibility(View.VISIBLE);
        imgSave.setBackgroundResource(R.drawable.ic_country);
        txtTitle= (TextView) mToolBar.findViewById(R.id.txt_title1);
        txtTitle.setVisibility(View.INVISIBLE);

        appPreferences = AppPreferences.getInstance(getActivity());
        String email = appPreferences.getString(AppConstants.EMAIL_ALERT, "");
        String sms = appPreferences.getString(AppConstants.SMS_ALERT, "");
        String sign_up_source = appPreferences.getString(AppConstants.SIGN_UP_SOURCE, "");

        GoToNotify_Setting = (LinearLayout) view.findViewById(R.id.lLayout_Notifications);
        togBtnEmailAlert = (ToggleButton)view.findViewById(R.id.toggleBtnEmailAlert_Settings);
        togBtnSmsAlert = (ToggleButton)view.findViewById(R.id.toggleBtnSMSAlert_Settings);

        if (sign_up_source != null && !sign_up_source.equals(null) && !sign_up_source.equals("")) {

            if (sign_up_source.equals(AppConstants.NEW_USER_SIGN_UP_SOURCE) || sign_up_source.equals(AppConstants.MANUAL_LOGIN)) {
                GoToNotify_Setting.setVisibility(View.VISIBLE);
            } else {
                GoToNotify_Setting.setVisibility(View.INVISIBLE);
            }
        }


        if (email != null && !email.equals(null) && !email.equals("")) {
            if (email.equals(AppConstants.ACTIVATE_SETTING)) {
                togBtnEmailAlert.setChecked(true);
            } else {
                togBtnEmailAlert.setChecked(false);
            }
        }

        if (sms != null && !sms.equals(null) && !sms.equals("")) {
            if (sms.equals(AppConstants.ACTIVATE_SETTING)) {
                togBtnSmsAlert.setChecked(true);
            } else {
                togBtnSmsAlert.setChecked(false);
            }
        }
        togBtnSmsAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isActivateSms = 0;
                if (isChecked) {
                    isActivateSms = 1;
                } else {
                    isActivateSms = 0;
                }
            }
        });

        togBtnEmailAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isActivateEmail = 0;
                if (isChecked) {
                    isActivateEmail = 1;
                } else {
                    isActivateEmail = 0;
                }
            }
        });
        GoToNotify_Setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationsSettings.class);
                startActivity(intent);
            }
        });
        imgSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isActivateSms == 0 || isActivateSms == 1) {
                    try {

                        alertType = AppConstants.SMS_ALERT;
                        setAlertState = "" + isActivateSms;

                        showLoadingDialog();
                        JSONObject jsonObject = new JSONObject();
                        appPreferences = AppPreferences.getInstance(getActivity());
                        String userId = appPreferences.getString(AppConstants.USER_ID, "");

                        jsonObject.put(AppConstants.USER_ID, userId);
                        jsonObject.put(alertType, setAlertState);


                        ApiHelper lSettingApi = new ApiHelper(ApiConstants.POST, ApiConstants.SMS_ALERT_ACTIVATION, jsonObject.toString(), SettingFragment.this);
                        lSettingApi.mApiID = ApiConstants.SMS_ALERT_ACTIVATION_ID;
                        lSettingApi.invokeAPI();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (isActivateEmail == 0 || isActivateEmail == 1) {
                    try {

                        alertType = AppConstants.EMAIL_ALERT;
                        setAlertState = "" + isActivateEmail;

                        showLoadingDialog();
                        JSONObject jsonObject = new JSONObject();
                        appPreferences = AppPreferences.getInstance(getActivity());
                        String userId = appPreferences.getString(AppConstants.USER_ID, "");

                        jsonObject.put(AppConstants.USER_ID, userId);
                        jsonObject.put(alertType, setAlertState);


                        ApiHelper lSettingApi = new ApiHelper(ApiConstants.POST, ApiConstants.EMAIL_ALERT_ACTIVATION, jsonObject.toString(), SettingFragment.this);
                        lSettingApi.mApiID = ApiConstants.EMAIL_ALERT_ACTIVATION_ID;
                        lSettingApi.invokeAPI();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        });
    }

    public void onSuccess(BaseResponse pResponse) {
        dismissLoadingDialog();
        int checkAPI = pResponse.getmAPIType();

        AppPreferences appPreferences = AppPreferences.getInstance(mContext);

        if (checkAPI == ApiConstants.SMS_ALERT_ACTIVATION_ID) {
            appPreferences.putString(AppConstants.SMS_ALERT, setAlertState);
            MMToast.getInstance().showLongToast(pResponse.getmMessage(), mContext);
        }

        if (checkAPI == ApiConstants.EMAIL_ALERT_ACTIVATION_ID) {
            appPreferences.putString(AppConstants.EMAIL_ALERT, setAlertState);
            MMToast.getInstance().showLongToast(pResponse.getmMessage(), mContext);
        }
    }


    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast(AppConstants.CHECK_INTERNET_CONNECTION, mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imgSave.setVisibility(View.INVISIBLE);
        txtTitle.setVisibility(View.INVISIBLE);
    }

}
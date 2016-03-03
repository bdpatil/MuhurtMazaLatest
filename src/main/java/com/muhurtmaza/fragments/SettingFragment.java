package com.muhurtmaza.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.muhurtmaza.webservice.response.CheckOTPResponse;
import com.muhurtmaza.widget.MMToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingFragment extends ParentFragment implements BaseHttpHelper.ResponseHelper {

    ToggleButton togBtnSmsAlert;
    ToggleButton togBtnEmailAlert;
    TextView txtTitle;
    private ImageView imgSave;
    private Toolbar mToolBar;
    private AppPreferences appPreferences;
    int isActivateEmail, isActivateSms;
    private Context mContext;
    private Button btnSave;
    private String alertType = "";
    private String setAlertState = "";

    private OnFragmentInteractionListener mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.inject(getActivity());
        mContext = getActivity();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        mToolBar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        mToolBar.setTitle("Settings");
        setHasOptionsMenu(true);
        togBtnEmailAlert = (ToggleButton) view.findViewById(R.id.toggleBtnEmailAlert_Settings);
        togBtnSmsAlert = (ToggleButton) view.findViewById(R.id.toggleBtnSMSAlert_Settings);
        btnSave = (Button) view.findViewById(R.id.btnSave_settingFragment);
    /*    txtTitle= (TextView) mToolBar.findViewById(R.id.txt_title1);
        txtTitle.setVisibility(View.INVISIBLE);
*/
        appPreferences = AppPreferences.getInstance(getActivity());
        String email = appPreferences.getString(AppConstants.EMAIL_ALERT, "");
        String sms = appPreferences.getString(AppConstants.SMS_ALERT, "");
        String sign_up_source = appPreferences.getString(AppConstants.SIGN_UP_SOURCE, "");
        String user_id = appPreferences.getString(AppConstants.USER_ID, "");

        Log.e("Preferences Data ","" + appPreferences.toString());

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

        btnSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showLoadingDialog();
                if (isActivateSms == 0 || isActivateSms == 1) {
                    try {

                        alertType = AppConstants.SMS_ALERT;
                        setAlertState = "" + isActivateSms;


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

        Log.e("Response UserProfile ", "" + pResponse.toString());
        AppPreferences appPreferences = AppPreferences.getInstance(mContext);
        CheckOTPResponse obj = (CheckOTPResponse) pResponse;

        if (checkAPI == ApiConstants.SMS_ALERT_ACTIVATION_ID) {

            if (obj.getmSuccess().equals("true")) {
                appPreferences.putString(AppConstants.SMS_ALERT, setAlertState);
                MMToast.getInstance().showLongToast(pResponse.getmMessage(), mContext);
            } /* else {
                 MMToast.getInstance().showLongToast("Sms", mContext);
             }*/
        }

        if (checkAPI == ApiConstants.EMAIL_ALERT_ACTIVATION_ID) {
            if (obj.getmSuccess().equals("true")) {
                appPreferences.putString(AppConstants.EMAIL_ALERT, setAlertState);
                MMToast.getInstance().showLongToast(pResponse.getmMessage(), mContext);
            } /*else {
                MMToast.getInstance().showLongToast("Email", mContext);
            }*/
        }
    }

    public void onFail(BaseResponse pBaseResponse) {
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast(AppConstants.CHECK_INTERNET_CONNECTION, mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
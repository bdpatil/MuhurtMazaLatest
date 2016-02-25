package com.muhurtmaza.webservice;


import android.os.AsyncTask;
import android.util.Log;

import com.muhurtmaza.webservice.response.BookingDetailsResponse;
import com.muhurtmaza.webservice.response.CheckOTPResponse;
import com.muhurtmaza.webservice.response.EditProfileResponse;
import com.muhurtmaza.webservice.response.GetBookedPoojaResponse;
import com.muhurtmaza.webservice.response.GetCityNamesResponse;
import com.muhurtmaza.webservice.response.NewPoojaDetailResponse;
import com.muhurtmaza.webservice.response.NewUserResponse;
import com.muhurtmaza.webservice.response.PoojaBookResponse;
import com.muhurtmaza.webservice.response.PoojaDetailsResponse;
import com.muhurtmaza.webservice.response.PoojaListResponse;
import com.muhurtmaza.webservice.response.UpdateUserProfile;
import com.muhurtmaza.webservice.response.UserProfileResponse;
import com.muhurtmaza.widget.MMException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class BaseHttpHelper extends AsyncTask<Void, Void, BaseResponse>  {
    private static final String TAG = "BaseHttpHelper";
    private String mApi="";
    public int mApiID;
    protected String mJsonInput;
    public ResponseHelper mResponseHelper;
    public String mApiType;

    @Override
    protected BaseResponse doInBackground(Void... pParams) {

        String lResult = sendRequest();
        if (lResult == null)
            return null;
        Log.v(TAG, "Response" + lResult);

        try {
            return retriveResponse(lResult);
        } catch (MMException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected BaseHttpHelper(String Type,String ApiName,String inputParameter,ResponseHelper responseHelper) {
        mApiType = Type;
        mApi=ApiName;
        mJsonInput=inputParameter;
        mResponseHelper = responseHelper;
    }

    /**
     * @return
     */
    public String sendRequest() {

        try {

            URL mUrl = new URL(mApi);
            HttpURLConnection lHttpUrlConnection = (HttpURLConnection) mUrl.openConnection();

            lHttpUrlConnection.setRequestProperty("Content-Type", "application/json");
            lHttpUrlConnection.setRequestProperty("Accept", "application/json");
            lHttpUrlConnection.setRequestProperty("app-platform", "android");
            lHttpUrlConnection.setRequestProperty("App-version", "1.0");
            //  lHttpUrlConnection.setRequestProperty("auth-key", "25");
            lHttpUrlConnection.setDoInput(true);

            Log.d(TAG, "Api:" + mApi);
            Log.d(TAG, "Request:" + mJsonInput);

            lHttpUrlConnection.setRequestMethod("POST");

            if (mApiType.equalsIgnoreCase(ApiConstants.GET)){
                lHttpUrlConnection.setRequestMethod("GET");
                lHttpUrlConnection.connect();
            }else{

                lHttpUrlConnection.setDoOutput(true);
                OutputStream lOutputStream = lHttpUrlConnection.getOutputStream();
                lOutputStream.write(mJsonInput.getBytes());
                lOutputStream.flush();
            }

            int code = lHttpUrlConnection.getResponseCode();

            if (lHttpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.i("HTTP CHECK", "Response Code=" + lHttpUrlConnection.getResponseCode());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (lHttpUrlConnection.getInputStream())));

            String output;
            StringBuilder lResponse = new StringBuilder();

            Log.d(TAG, "Response Start");

            while ((output = br.readLine()) != null) {

                lResponse.append(output);
                lResponse.append('\r');
            }
            Log.d(TAG, "Response:" + lResponse);
            if (lHttpUrlConnection != null)
                lHttpUrlConnection.disconnect();
            return lResponse.toString();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }


    /**
     * @author Sandeep
     */
    public interface ResponseHelper {
        public void onSuccess(BaseResponse pResponse);
        public void onFail(BaseResponse pBaseResponse);
    }

    /**
     *
     */
    protected abstract void invokeAPI();

    /**
     * Parse response to respective object
     *
     * @param pResponse
     * @return
     */
    private BaseResponse retriveResponse(String pResponse) throws MMException {
        BaseResponse lBaseResponse = null;
        switch(mApiID){
            case ApiConstants.LOGIN_ID:
                lBaseResponse = NewUserResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.LOGIN_ID);
                break;

            case ApiConstants.GET_ALL_BOOKINGS_ID:
                lBaseResponse = GetBookedPoojaResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.GET_ALL_BOOKINGS_ID);
                break;

            case ApiConstants.GET_UPCOMING_BOOKINGS_ID:
                lBaseResponse = GetBookedPoojaResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.GET_UPCOMING_BOOKINGS_ID);
                break;

            case ApiConstants.NEW_SIGN_UP_ID:
                lBaseResponse = NewUserResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.NEW_SIGN_UP_ID);
                break;

            case ApiConstants.GENERATE_OTP_ID:
                lBaseResponse = BaseResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.GENERATE_OTP_ID);
                break;

            case ApiConstants.CHECK_OTP_ID:
                lBaseResponse = CheckOTPResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.CHECK_OTP_ID);
                break;

            case ApiConstants.REGENERATE_OTP_ID:
                lBaseResponse = BaseResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.REGENERATE_OTP_ID);
                break;

            case ApiConstants.EDIT_PROFILE_ID:
                lBaseResponse = EditProfileResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.EDIT_PROFILE_ID);
                break;

            case ApiConstants.FORGOT_PASSWORD_ID:
                lBaseResponse = CheckOTPResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.FORGOT_PASSWORD_ID);
                break;

            case ApiConstants.EMAIL_ALERT_ACTIVATION_ID:
                lBaseResponse = BaseResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.EMAIL_ALERT_ACTIVATION_ID);
                break;

            case ApiConstants.SMS_ALERT_ACTIVATION_ID:
                lBaseResponse = BaseResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.SMS_ALERT_ACTIVATION_ID);
                break;

            case ApiConstants.CHANGE_PASSWORD_ID:
                lBaseResponse = BaseResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.CHANGE_PASSWORD_ID);
                break;

            case ApiConstants.SEARCH_POOJA_ID:
                lBaseResponse = PoojaListResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.SEARCH_POOJA_ID);
                break;

            case ApiConstants.GET_ALL_POOJA_LIST_ID:
                lBaseResponse = PoojaListResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.GET_ALL_POOJA_LIST_ID);
                break;

            case ApiConstants.GET_POOJA_DETAILS_ID:
                lBaseResponse = NewPoojaDetailResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.GET_POOJA_DETAILS_ID);
                break;

            case ApiConstants.GET_CITY_NAMES_ID:
                lBaseResponse = GetCityNamesResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.GET_CITY_NAMES_ID);
                break;

            case ApiConstants.BOOK_POOJA_ID:
                lBaseResponse = PoojaBookResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.BOOK_POOJA_ID);
                break;

            case ApiConstants.POOJA_BOOKINGS_DETAILS_ID:
                lBaseResponse = BookingDetailsResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.POOJA_BOOKINGS_DETAILS_ID);
                break;

            case ApiConstants.REPORT_ISSUES_ID:
                lBaseResponse = BaseResponse.fromJson(pResponse);
                lBaseResponse.setmAPIType(ApiConstants.REPORT_ISSUES_ID);
                break;

        }

        return lBaseResponse;
    }

    @Override
    protected void onPostExecute(BaseResponse pResult) {
        super.onPostExecute(pResult);
        if (pResult == null) {
            mResponseHelper.onFail(null);
            return;
        }
        if (pResult.ismStatus()) {
            mResponseHelper.onFail(pResult);
            return;
        }
        mResponseHelper.onSuccess(pResult);
        return;


    }
}
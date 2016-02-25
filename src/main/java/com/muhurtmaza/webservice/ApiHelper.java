package com.muhurtmaza.webservice;

import android.os.AsyncTask;

/**
 * Created by Sandeep on 11/22/15.
 */
public class ApiHelper extends BaseHttpHelper{

    public ApiHelper(String Type,String ApiName,String inputParameter, ResponseHelper pResponseHelper) {
        super(Type,ApiName,inputParameter, pResponseHelper);
    }

    @Override
    public void invokeAPI() {

        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

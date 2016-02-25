package com.muhurtmaza.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by Sandeep on 11/22/15.
 */

public class AppPreferences {
    private static final String TAG = "AppPreferences";
    private static SharedPreferences mAppSharedPrefs;
    private static AppPreferences mInstance;


    public static AppPreferences getInstance(Context pContext) {
        if (mInstance == null)
            mInstance = new AppPreferences();


        mAppSharedPrefs = pContext.getSharedPreferences(
                AppConstants.SHARED_PREF, Activity.MODE_PRIVATE);

        return mInstance;
    }

    public void putString(String pKey, String pDefaultVal) {
        Log.d(TAG, "key :: " + pDefaultVal + " pDefaultVal = " + pDefaultVal);
        Editor prefsEditor = mAppSharedPrefs.edit();
        prefsEditor.putString(pKey, pDefaultVal);
        prefsEditor.apply();
    }

    public void putInt(String pKey, int pDefaultVal) {
        Log.d(TAG, "key :: " + pDefaultVal + " pDefaultVal = " + pDefaultVal);
        Editor prefsEditor = mAppSharedPrefs.edit();
        prefsEditor.putInt(pKey, pDefaultVal);
        prefsEditor.apply();

    }

    public void putFloat(String pKey, float pDefaultVal) {
        Log.d(TAG, "key :: " + pDefaultVal + " pDefaultVal = " + pDefaultVal);
        Editor prefsEditor = mAppSharedPrefs.edit();
        prefsEditor.putFloat(pKey, pDefaultVal);
        prefsEditor.apply();

    }

    public void putBoolean(String pKey, boolean pDefaultVal) {
        Log.d(TAG, "key :: " + pDefaultVal + " pDefaultVal = " + pDefaultVal);
        Editor prefsEditor = mAppSharedPrefs.edit();
        prefsEditor.putBoolean(pKey, pDefaultVal);
        prefsEditor.apply();
    }

    public void putLong(String pKey, long pDefaultVal) {
        Log.d(TAG, "key :: " + pDefaultVal + " pDefaultVal = " + pDefaultVal);
        Editor prefsEditor = mAppSharedPrefs.edit();
        prefsEditor.putLong(pKey, pDefaultVal);
        prefsEditor.apply();
    }

    public String getString(String key, String pDefaultVal) {
        return mAppSharedPrefs.getString(key, pDefaultVal);
    }


    public float getFloat(String pKey, float pDefaultVal) {
        return mAppSharedPrefs.getFloat(pKey, pDefaultVal);
    }

    public int getInt(String pKey, int pDefaultVal) {
        return mAppSharedPrefs.getInt(pKey, pDefaultVal);
    }


    public long getLong(String pKey, long pDefaultVal) {
        return mAppSharedPrefs.getLong(pKey, pDefaultVal);
    }

    public boolean getBoolean(String pKey, boolean pDefaultVal) {
        return mAppSharedPrefs.getBoolean(pKey, pDefaultVal);
    }

    public void deleteAllPreferences() {
        Editor prefsEditor = mAppSharedPrefs.edit();
        prefsEditor.clear();
        prefsEditor.apply();
    }

    public void deletePreferences(String name) {

        Editor prefsEditor = mAppSharedPrefs.edit();
        prefsEditor.remove(name);
        prefsEditor.apply();
    }
}

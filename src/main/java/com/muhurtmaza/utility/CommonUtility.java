package com.muhurtmaza.utility;

import android.content.Context;

/**
 * Created by Sandeep on 11/22/15.
 */
public class CommonUtility {

    private static CommonUtility nUtileHelper;
    private static Context mContext;

    public static CommonUtility getInstance(Context context) {
        if (nUtileHelper == null) {
            mContext = context;
            nUtileHelper = new CommonUtility();
        }
        return nUtileHelper;
    }
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isValidNumber(String number){
        return android.util.Patterns.PHONE.matcher(number).matches();
    }
}

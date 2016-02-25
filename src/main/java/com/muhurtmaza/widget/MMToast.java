package com.muhurtmaza.widget;

import android.content.Context;


/**
 * Created by Sandeep on 11/22/15.
 */

public class MMToast {

    private static MMToast mToast;



    public static synchronized MMToast getInstance() {
        if (mToast == null)
            mToast = new MMToast();

        return mToast;

    }

    /**
     * Show long toast message
     * @param pMessage
     * @param pContext
     */
    public void showLongToast(String pMessage,Context pContext) {
          android.widget.Toast.makeText(pContext, pMessage, android.widget.Toast.LENGTH_LONG).show();
    }

    /**
     * Show short toast message
     * @param pMessage
     * @param pContext
     */
    public void showShortToast(String pMessage,Context pContext) {
        if (pContext!= null){
           android.widget.Toast.makeText(pContext, pMessage, android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}

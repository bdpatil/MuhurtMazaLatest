package com.muhurtmaza.widget;


import android.content.Context;
import android.content.DialogInterface;

import com.muhurtmaza.interfaces.DialogClickInterface;


/**
 * Created by Sandeep on 11/22/15.
 */

public class AlertDialog implements DialogInterface.OnClickListener {

    private DialogClickInterface mDialogClickInterface;

    public static AlertDialog mDialog;
    private int mDialogIdentifier;

    public static AlertDialog getInstance() {
        if (mDialog == null)
            mDialog = new AlertDialog();

        return mDialog;

    }

    /**
     * Show confirmation dialog with two buttons
     *
     * @param pTitle
     * @param pMessage
     * @param pPositiveButton
     * @param pNegativeButton
     * @param pContext
     * @param pDialogIdentifier
     */
    public void showConfirmDialog(String pTitle, String pMessage,
                                  String pPositiveButton, String pNegativeButton,
                                  Context pContext, int pDialogIdentifier) {


        mDialogClickInterface = (DialogClickInterface) pContext;
        mDialogIdentifier = pDialogIdentifier;
        android.support.v7.app.AlertDialog.Builder lBuilder = new android.support.v7.app.AlertDialog.Builder(pContext)
                .setTitle(pTitle)
                .setMessage(pMessage)
                .setPositiveButton(pPositiveButton, this)
                .setNegativeButton(pNegativeButton, this);

        android.support.v7.app.AlertDialog lDialog = lBuilder.show();


    }


    @Override
    public void onClick(DialogInterface pDialog, int pWhich) {

        switch (pWhich) {
            case DialogInterface.BUTTON_POSITIVE:
                mDialogClickInterface.onClickPositiveButton(pDialog, mDialogIdentifier);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mDialogClickInterface.onClickNegativeButton(pDialog, mDialogIdentifier);
                break;

        }

    }
}

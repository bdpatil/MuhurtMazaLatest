package com.muhurtmaza.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

/**
 * Created by Sandeep on 11/22/15.
 */
public class ParentFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    // dismiss loading dialog
    protected void dismissLoadingDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // show loading dialog
    protected void showLoadingDialog() {

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    // show loading dialog
    protected void showLoadingDialog(String msg) {

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }
}

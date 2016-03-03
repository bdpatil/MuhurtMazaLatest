package com.muhurtmaza.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.muhurtmaza.R;
import com.muhurtmaza.cropImage.CropImage;
import com.muhurtmaza.cropImage.ProcessImage;
import com.muhurtmaza.fragments.ProfileFragment;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.MuhurtMazaApplication;
import com.muhurtmaza.webservice.ApiConstants;
import com.muhurtmaza.webservice.ApiHelper;
import com.muhurtmaza.webservice.BaseHttpHelper;
import com.muhurtmaza.webservice.BaseResponse;
import com.muhurtmaza.webservice.response.EditProfileResponse;
import com.muhurtmaza.webservice.response.NewUserResponse;
import com.muhurtmaza.widget.MMToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends ParentActivity implements View.OnClickListener, BaseHttpHelper.ResponseHelper {

    private static final int IMAGE_CAPTURE = 101;
    private static final int SELECT_FILE = 102;
    final int PIC_CROP = 2;
    private Uri mVideoImageUri = null;
    private String TAG = getClass().getSimpleName();
    private AppPreferences appPreferences;
    private boolean isProfileUpdated = false;
    private String updatedImagePath;
    private ImageLoader imageLoader;
    public String strfName, strlName, strLanguage, strEmail, strMobileNo, strAddress, strCity;
    //@InjectView(R.id.img_edit)ImageView imgEdit;
    private Context context;
    de.hdodenhof.circleimageview.CircleImageView imgUserProfile;
    private ImageView  imgEditBtnProfile;
    android.support.v7.widget.Toolbar mToolbar;
    EditText etFirstName_EditProfile, etLastName_EditProfile, etLanguage_EditProfile, etEmail_EditProfile, etMobileNo_EditProfile, etAddress_EditProfile, etCity_EditProfile;
    Button btnSaveClick;
    private Context mContext;
    boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mContext = this;

        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAllValues();
        getAllValues();
    }

    private void setupUI() {
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle("Edit Profile");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageLoader = MuhurtMazaApplication.getImageLoader(this);
        appPreferences = AppPreferences.getInstance(this);
        imgEditBtnProfile = (ImageView) findViewById(R.id.imgCloud_EditProfile);
        imgUserProfile = (CircleImageView) findViewById(R.id.imgUserProfile_EditProfile);
        //Ids related to the activity_edit_profile xml..bharat
        etFirstName_EditProfile = (EditText) findViewById(R.id.et_fnameEditProfile);
        etLastName_EditProfile = (EditText) findViewById(R.id.et_lnameEditProfile);
        etLanguage_EditProfile = (EditText) findViewById(R.id.et_languageEditProfile);
        etEmail_EditProfile = (EditText) findViewById(R.id.et_emailEditProfile);
        etMobileNo_EditProfile = (EditText) findViewById(R.id.et_mobileNoEditProfile);
        etAddress_EditProfile = (EditText) findViewById(R.id.et_addressEditProfile);
        etCity_EditProfile = (EditText) findViewById(R.id.et_cityEditProfile);

        btnSaveClick = (Button) findViewById(R.id.btnSave_EditProfile);

        getAllValues();
        setAllValues();

        imgUserProfile.setOnClickListener(this);
        imgEditBtnProfile.setOnClickListener(this);
        btnSaveClick.setOnClickListener(this);
    }

    private void getAllValues() {
        strfName = etFirstName_EditProfile.getText().toString().trim();
        strlName = etLastName_EditProfile.getText().toString().trim();
        strLanguage = etLanguage_EditProfile.getText().toString().trim();
        strEmail = etEmail_EditProfile.getText().toString().trim();
        strMobileNo = etMobileNo_EditProfile.getText().toString().trim();
        strAddress = etAddress_EditProfile.getText().toString().trim();
        strCity = etCity_EditProfile.getText().toString().trim();
    }

    private void setAllValues() {
        etFirstName_EditProfile.setText(appPreferences.getString(AppConstants.USER_FNAME.trim(), ""));
        etLastName_EditProfile.setText(appPreferences.getString(AppConstants.USER_LNAME.trim(), ""));
        etLanguage_EditProfile.setText(appPreferences.getString(AppConstants.USER_LANGUAGE.trim(), ""));
        etEmail_EditProfile.setText(appPreferences.getString(AppConstants.USER_EMAIL.trim(), ""));
        etMobileNo_EditProfile.setText(appPreferences.getString(AppConstants.USER_NUMBER.trim(), ""));
        etAddress_EditProfile.setText(appPreferences.getString(AppConstants.USER_ADDRESS.trim(), ""));
        etCity_EditProfile.setText(appPreferences.getString(AppConstants.USER_CITY.trim(), ""));
        imageLoader.displayImage(appPreferences.getString(AppConstants.USER_PROFILE.trim(), ""), imgUserProfile);
        updatedImagePath = appPreferences.getString(AppConstants.USER_PROFILE.trim(), "");

    }

    private void btnSave() {

        View focusView = null;

        getAllValues();

        etFirstName_EditProfile.setError(null);
        etLastName_EditProfile.setError(null);
        etLanguage_EditProfile.setError(null);
        etEmail_EditProfile.setError(null);
        etMobileNo_EditProfile.setError(null);
        etAddress_EditProfile.setError(null);
        etCity_EditProfile.setError(null);

        if (strfName.isEmpty()) {
            etFirstName_EditProfile.setError(getString(R.string.error_field_required));
            focusView = etFirstName_EditProfile;
            cancel = true;
        }
        if (strlName.isEmpty()) {
            etLastName_EditProfile.setError(getString(R.string.error_field_required));
            focusView = etLastName_EditProfile;
            cancel = true;
        }
        if (strLanguage.isEmpty()) {
            etLanguage_EditProfile.setError(getString(R.string.error_invalid_email));
            focusView = etLanguage_EditProfile;
            cancel = true;
        }
        if (strEmail.isEmpty()) {
            etEmail_EditProfile.setError(getString(R.string.error_field_required));
            focusView = etEmail_EditProfile;
            cancel = true;
        }
        if (strMobileNo.isEmpty()) {
            etMobileNo_EditProfile.setError(getString(R.string.error_field_required));
            focusView = etMobileNo_EditProfile;
            cancel = true;
        }
        if (strAddress.isEmpty()) {
            etAddress_EditProfile.setError(getString(R.string.error_field_required));
            focusView = etAddress_EditProfile;
            cancel = true;
        }
        if (strCity.isEmpty()) {
            etCity_EditProfile.setError(getString(R.string.error_field_required));
            focusView = etCity_EditProfile;
            cancel = true;
        }
        int len = strMobileNo.length();
        if (len < 10) {
            etMobileNo_EditProfile.setError("Please enter valid phone number.");
            focusView = etMobileNo_EditProfile;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            cancel = false;
            try {
                showLoadingDialog();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("first_name", strfName);
                jsonObject.put("last_name", strlName);
                jsonObject.put("cust_language", strLanguage);
                jsonObject.put(AppConstants.USER_ID, appPreferences.getString(AppConstants.USER_ID.trim(), ""));
                jsonObject.put("user_profile_image", "");
                jsonObject.put("phone", strMobileNo);
                jsonObject.put("city", strCity);
                jsonObject.put("address", strAddress);
                jsonObject.put("email", strEmail);

                ApiHelper lEditProfileApi = new ApiHelper(ApiConstants.POST, ApiConstants.EDIT_PROFILE_URL, jsonObject.toString(), this);
                lEditProfileApi.mApiID = ApiConstants.EDIT_PROFILE_ID;
                lEditProfileApi.invokeAPI();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(BaseResponse pResponse) {
        Log.d("Success Response", pResponse.toString());
        dismissLoadingDialog();

        EditProfileResponse editProfResponce = (EditProfileResponse) pResponse;
        String message = editProfResponce.getmMessage();

        if (message.equals("Profile Update successfully")) {
            AppPreferences appPreferences = AppPreferences.getInstance(mContext);
            appPreferences.putString(AppConstants.USER_ADDRESS, "" + editProfResponce.getmAddress());
            appPreferences.putString(AppConstants.USER_NUMBER, "" + editProfResponce.getmPhone());
            appPreferences.putString(AppConstants.USER_FNAME, "" + editProfResponce.getmFName());
            appPreferences.putString(AppConstants.USER_LNAME, "" + editProfResponce.getmLName());
            appPreferences.putString(AppConstants.USER_CITY, "" + editProfResponce.getmCity());
            appPreferences.putString(AppConstants.USER_EMAIL, "" + editProfResponce.getmEmailId());
            appPreferences.putString(AppConstants.USER_LANGUAGE, "" + editProfResponce.getmLanguage());
            appPreferences.putString(AppConstants.USER_PROFILE, "" + editProfResponce.getmUserImage());

            this.finish();
        } else {
            MMToast.getInstance().showLongToast("Something went Wrong ", this);
        }

    }

    @Override
    public void onFail(BaseResponse pBaseResponse) {
        Log.d("Fail Response", pBaseResponse.toString());
        dismissLoadingDialog();
        MMToast.getInstance().showLongToast("Sign Up fail = " + pBaseResponse.getmMessage(), this);
    }

    protected void onActivityResult(int pRequestCode, int pResultCode,
                                    Intent pData) {
        super.onActivityResult(pRequestCode, pResultCode, pData);

        if (pResultCode != RESULT_OK)
            return;

        if (pRequestCode == IMAGE_CAPTURE) {
            if (mVideoImageUri == null)
                return;
            performCrop(mVideoImageUri);

        } else if (pRequestCode == SELECT_FILE) {
            if (pData == null)
                return;
            getCroppedImage(pData);

        } else if (pRequestCode == PIC_CROP) {
            Bundle extras = pData.getExtras();

            if (extras != null) {
                try {
                    Uri lImageUri = Uri.parse(extras.getString(AppConstants.CROPPED_IMAGE));
                    updatedImagePath = lImageUri.toString();

                    Log.d("Image path", "" + updatedImagePath);
                    //Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), lImageUri);
                    // Bitmap photo = (Bitmap) extras.getParcelable("data");

                    //updateImageView(photo);

                    Glide.with(context).load(updatedImagePath).into(imgUserProfile);
                    isProfileUpdated = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private void showImageOptionDialog() {
        final String CHOOSE_GALLERY = "Choose Gallery", USE_CAMERA = "Use Camera";

        ArrayList<String> list = new ArrayList<String>();
        final CharSequence items[];
        list.add(CHOOSE_GALLERY);
        list.add(USE_CAMERA);

        items = list.toArray(new CharSequence[list.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Profile Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // mRemoveImage = false;
                if (items[item].equals(USE_CAMERA)) {

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    mVideoImageUri = ProcessImage.getOutputUri(false);
                    if (mVideoImageUri != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoImageUri);
                    }
                    startActivityForResult(intent, IMAGE_CAPTURE);
                } else if (items[item].equals(CHOOSE_GALLERY)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.setType("image*//*");
                    startActivityForResult(Intent.createChooser(intent, "select file"), SELECT_FILE);

                }
            }
        });
        builder.show();
    }

    private void getCroppedImage(Intent pData) {
        if (pData.getData() != null) {
            performCrop(pData.getData());
        } else {
            Bundle extras = pData.getExtras();
            if (extras == null)
                return;
            Bitmap limageBitmap = (Bitmap) extras.get("data");
            if (limageBitmap == null)
                return;
            Uri lUri = ProcessImage.getImageUri(context, limageBitmap);
            if (lUri == null)
                return;
            performCrop(lUri);

        }

    }


    private void performCrop(Uri pPicUri) {
        // call the standard crop action intent (the user device may not support
        // it)

        try {
            Intent lIntent = new Intent(context, CropImage.class);

            lIntent.putExtra("image-path", pPicUri.toString());

            lIntent.putExtra("aspectX", 1);
            lIntent.putExtra("aspectY", 1);

            // lIntent.putExtra("outputX", 256);
            // lIntent.putExtra("outputY", 256);
            lIntent.putExtra("return-data", false);
            startActivityForResult(lIntent, PIC_CROP);
        } catch (Exception e) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void updateImageView(Bitmap pNewImage) {

        if (pNewImage != null) {

            BitmapProcessor bitmapProcessor = new BitmapProcessor() {

                @Override
                public Bitmap process(Bitmap bitmap) {

                    return bitmap;
                }
            };

            Bitmap image = bitmapProcessor.process(pNewImage);
            //imgUserProfile.setImageBitmap(image);
            Glide.with(context).load(image).into(imgUserProfile);
            isProfileUpdated = true;
        }
    }

    public void onClick(View v) {
        if (v == imgEditBtnProfile) {
            showImageOptionDialog();
        } else if (v == imgUserProfile) {
            int len = etMobileNo_EditProfile.getText().toString().trim().length();
        }
        if (v == btnSaveClick) {
            btnSave();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
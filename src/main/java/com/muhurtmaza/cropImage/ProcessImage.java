package com.muhurtmaza.cropImage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 15-12-2015.
 */
public class ProcessImage {
    public static String getEncodedBitmap(ImageView pImageView) {

        Bitmap bMap = ((BitmapDrawable) pImageView.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedString;
    }

    // convert bitmap to uri
    public static Uri getImageUri(Context pContext, Bitmap pImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        pImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(pContext.getContentResolver(),
                pImage, "Title", null);
        return Uri.parse(path);
    }
    private static void setImageBound(ImageView pImageView, int pScaleType,
                                      Drawable pDrawable) {
        ImageView lImageView = pImageView;// mImageViewReference.get();
        lImageView.getLayoutParams().height = pDrawable.getIntrinsicHeight()
                * pScaleType;
        lImageView.getLayoutParams().width = pDrawable.getIntrinsicWidth()
                * pScaleType;
    }

    /**
     *
     * @param pPath
     * @param pReqHeight
     * @param pReqWidth
     * @return
     */
    public static int getSampleSize(String pPath, int pReqHeight, int pReqWidth) {
        if (!isPathExist(pPath))
            return 0;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pPath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > pReqHeight || width > pReqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > pReqHeight
                    && (halfWidth / inSampleSize) > pReqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     *
     * @param pPath
     * @return
     */
    private static boolean isPathExist(String pPath) {
        if (pPath == null)
            return false;
        return new File(pPath).exists();
    }

    /**
     *
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     *
     * @return
     */
    public static Uri getOutputUri(boolean pIsVideo) {
        File lFile = Environment.getExternalStorageDirectory();
        if (lFile == null) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                return null;
            }

        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(new Date());

        String lPath;

        String lFileName;

        if (pIsVideo) {
            lPath = com.muhurtmaza.utility.AppConstants.VIDEO_PATH;
            lFileName = "VID_" + timeStamp + ".mp4";
        } else {
            lPath = com.muhurtmaza.utility.AppConstants.IMAGE_PATH;
            lFileName = "IMG_" + timeStamp + ".jpg";
        }
        lFile = new File(Environment.getExternalStorageDirectory() + lPath);

        if (!lFile.exists()) {
            lFile.mkdirs();
        }

        // Create a media file name

        lFile = new File(Environment.getExternalStorageDirectory() + lPath
                + "/" + lFileName);

        //	Log.v("ProcessImage", "" + lFile.getAbsolutePath());

        return Uri.fromFile(lFile);
    }
    public static String removeUriFromPath(String pUri) {
        return pUri.substring(7, pUri.length());
    }
    public static Bitmap rotateBitmap(Bitmap pBitmap, String pFilepath,
                                      Context pContext) {
        String lPicturePath;
        try {

            if (pFilepath.startsWith("file")) {
                lPicturePath = pFilepath;
            } else {
                String[] lFilePathColumn = { MediaStore.Images.Media.DATA };

                Cursor lCursor = pContext.getContentResolver()
                        .query(Uri.parse(pFilepath), lFilePathColumn, null,
                                null, null);
                lCursor.moveToFirst();

                int lColumnIndex = lCursor.getColumnIndex(lFilePathColumn[0]);
                lPicturePath = lCursor.getString(lColumnIndex);
            }
            ExifInterface lExif = new ExifInterface(lPicturePath);
            int exifOrientation = lExif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            // Log.d(TAG,
            // "Rotation bitmap.getWidth = "+bitmap.getWidth()+" bitmap.getHeight = "+bitmap.getHeight());
            //
            // rotate = bitmap.getWidth() > bitmap.getHeight() ? 0 : 90;

            if (rotate != 0) {

                int w = pBitmap.getWidth();
                int h = pBitmap.getHeight();

                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                pBitmap = Bitmap.createBitmap(pBitmap, 0, 0, w, h, mtx, false);
            }

            pBitmap = pBitmap.copy(Bitmap.Config.ARGB_8888, true);

            return pBitmap;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception lE) {
            lE.printStackTrace();
        }

        return pBitmap;

    }

}

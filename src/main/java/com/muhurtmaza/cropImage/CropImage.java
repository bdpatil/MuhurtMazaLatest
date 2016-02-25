package com.muhurtmaza.cropImage;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * The activity can crop specific region of interest from an image.
 */
@SuppressLint("NewApi")
public class CropImage extends MonitoredActivity {
	private static final String TAG = "CropImage";

	// These are various options can be specified in the intent.

	private Uri mSaveUri;
	private int mAspectX, mAspectY;
	private boolean mCircleCrop = false;
	private final Handler mHandler = new Handler();

	// These options specifiy the output image size and whether we should
	// scale the output to fit it (or just crop it).
	private int mOutputX, mOutputY;
	private boolean mScale;
	private boolean mScaleUp = true;

	private boolean mDoFaceDetection = true;

	boolean mWaitingToPick; // Whether we are wait the user to pick a face.
	boolean mSaving = false; // Whether the "save" button is already clicked.

	private CropImageView mImageView;
	private ContentResolver mContentResolver;

	private Bitmap mBitmap;
	private final BitmapManager.ThreadSet mDecodingThreads = new BitmapManager.ThreadSet();
	HighlightView mCrop;

	private IImage mImage;

	private String mImagePath;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mContentResolver = getContentResolver();

		setContentView(R.layout.cropimage);

		mImageView = (CropImageView) findViewById(R.id.image);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		/*getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeButtonEnabled(true);*/

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.getString("circleCrop") != null) {
				mCircleCrop = true;
				mAspectX = 1;
				mAspectY = 1;
			}

			mImagePath = extras.getString("image-path");
		//	Log.v(TAG, "" + mImagePath);
			if (mImagePath == null) {
				Toast.makeText(CropImage.this,
						"Image not found in local storage", Toast.LENGTH_SHORT)
						.show();
				finish();
				return;
			}

			mSaveUri = getImageUri(mImagePath);

			if (mSaveUri == null) {
				Toast.makeText(CropImage.this,
						"Image not found in local storage", Toast.LENGTH_SHORT)
						.show();
				finish();
				return;
			}

			if (extras.getString(MediaStore.EXTRA_OUTPUT) != null) {
				mSaveUri = getImageUri(extras
						.getString(MediaStore.EXTRA_OUTPUT));
			}

			mBitmap = getBitmap(mImagePath);

			mAspectX = extras.getInt("aspectX");
			mAspectY = extras.getInt("aspectY");
			mOutputX = extras.getInt("outputX");
			mOutputY = extras.getInt("outputY");
			mScale = extras.getBoolean("scale", true);
			mScaleUp = extras.getBoolean("scaleUpIfNeeded", true);

		}

		if (mBitmap == null) {
			Toast.makeText(CropImage.this, "Image not found in local storage",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		// Make UI fullscreen.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		findViewById(R.id.discard).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});

		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSaveClicked();
			}
		});

		startFaceDetection();

	}

	private static Uri getImageUri(String path) {

		try {
			return Uri.parse(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int getOrientation(Context context, Uri photoUri) {
		/* it's on the external media. */
		if (photoUri == null)
			return -1;

		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		if (cursor == null)
			return -1;

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	private Bitmap getBitmap(String path) {
		Uri uri = getImageUri(path);
		// Log.v("oreintation", "" + uri);
		InputStream lIn = null;
		// int lOrientation = getOrientation(CropImage.this, uri);
		// if (lOrientation == -1) {
		// try {
		// ExifInterface lExif;
		//
		// lExif = new ExifInterface(ProcessImage.removeUriFromPath(path));
		//
		// lOrientation = lExif.getAttributeInt(
		// ExifInterface.TAG_ORIENTATION,
		// ExifInterface.ORIENTATION_UNDEFINED);
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		// }
		// }
		//
		// Log.v("oreintation", "" + lOrientation);
		try {
			// final int IMAGE_MAX_SIZE = 2048;
			final int IMAGE_MAX_SIZE = 1024;
			lIn = mContentResolver.openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			BitmapFactory.decodeStream(lIn, null, o);
			lIn.close();

			int lScale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				lScale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			BitmapFactory.Options lO2 = new BitmapFactory.Options();
			lO2.inSampleSize = lScale;
			lIn = mContentResolver.openInputStream(uri);
			Bitmap lBit = BitmapFactory.decodeStream(lIn, null, lO2);
			lIn.close();

			lBit = ProcessImage.rotateBitmap(lBit, path, CropImage.this);
			// if (orientation > 0) {
			// Matrix matrix = new Matrix();
			// matrix.postRotate(orientation);
			//
			// b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
			// matrix, true);
			// } else if (orientation == -1) {
			// b=ProcessImage.rotateBitmap(b, orientation)
			// // Matrix matrix = new Matrix();
			// // matrix.preRotate(90);
			// //
			// // b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
			// // matrix, true);
			// }

			return lBit;
		} catch (FileNotFoundException e) {
		//	Log.e(TAG, "file " + path + " not found");
		} catch (IOException e) {
		//	Log.e(TAG, "file " + path + " not found");
		} catch (Exception e) {
		//	Log.e(TAG, "file " + path + " not found");
		}
		return null;
	}

	private void startFaceDetection() {
		if (isFinishing()) {
			return;
		}

		mImageView.setImageBitmapResetBase(mBitmap, true);
		mImageView.setScaleType(ScaleType.FIT_CENTER);

		Util.startBackgroundJob(this, null, "Please wait\u2026",
				new Runnable() {
					public void run() {
						final CountDownLatch latch = new CountDownLatch(1);
						final Bitmap b = (mImage != null) ? mImage
								.fullSizeBitmap(IImage.UNCONSTRAINED,
										1024 * 1024) : mBitmap;
						mHandler.post(new Runnable() {
							public void run() {
								if (b != mBitmap && b != null) {
									mImageView.setImageBitmapResetBase(b, true);
									mImageView
											.setScaleType(ScaleType.FIT_CENTER);
									mBitmap.recycle();
									mBitmap = b;
								}
								if (mImageView.getScale() == 1F) {
									mImageView
											.setScaleType(ScaleType.FIT_CENTER);
								}
								latch.countDown();
							}
						});
						try {
							latch.await();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						mRunFaceDetection.run();
					}
				}, mHandler);
	}

	private void onSaveClicked() {
		// TODO this code needs to change to use the decode/crop/encode single
		// step api so that we don't require that the whole (possibly large)
		// bitmap doesn't have to be read into memory
		if (mSaving)
			return;

		if (mCrop == null) {
			return;
		}

		mSaving = true;

		Rect r = mCrop.getCropRect();

		int width = r.width();
		int height = r.height();

		// If we are circle cropping, we want alpha channel, which is the
		// third param here.
		Bitmap croppedImage = Bitmap.createBitmap(width, height,
				mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		{
			Canvas canvas = new Canvas(croppedImage);
			Rect dstRect = new Rect(0, 0, width, height);
			canvas.drawBitmap(mBitmap, r, dstRect, null);
		}

		if (mCircleCrop) {
			// OK, so what's all this about?
			// Bitmaps are inherently rectangular but we want to return
			// something that's basically a circle. So we fill in the
			// area around the circle with alpha. Note the all important
			// PortDuff.Mode.CLEAR.
			Canvas c = new Canvas(croppedImage);
			Path p = new Path();
			p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
			c.clipPath(p, Region.Op.DIFFERENCE);
			c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
		}

		/* If the output is required to a specific size then scale or fill */
		if (mOutputX != 0 && mOutputY != 0) {
			if (mScale) {
				/* Scale the image to the required dimensions */
				Bitmap old = croppedImage;
				croppedImage = Util.transform(new Matrix(), croppedImage,
						mOutputX, mOutputY, mScaleUp);
				if (old != croppedImage) {
					old.recycle();
				}
			} else {

				/*
				 * Don't scale the image crop it to the size requested. Create
				 * an new image with the cropped image in the center and the
				 * extra space filled.
				 */

				// Don't scale the image but instead fill it so it's the
				// required dimension
				Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY,
						Bitmap.Config.RGB_565);
				Canvas canvas = new Canvas(b);

				Rect srcRect = mCrop.getCropRect();
				Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

				int dx = (srcRect.width() - dstRect.width()) / 2;
				int dy = (srcRect.height() - dstRect.height()) / 2;

				/* If the srcRect is too big, use the center part of it. */
				srcRect.inset(Math.max(0, dx), Math.max(0, dy));

				/* If the dstRect is too big, use the center part of it. */
				dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

				/* Draw the cropped bitmap in the center */
				canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

				/* Set the cropped bitmap as the new bitmap */
				croppedImage.recycle();
				croppedImage = b;
			}
		}

		final Bitmap b = croppedImage;
		Util.startBackgroundJob(this, null, "Saving image", new Runnable() {
			public void run() {
				saveOutput(b);
			}
		}, mHandler);

	}

	/**
	 * 
	 * @param croppedImage
	 */
	private void saveOutput(Bitmap croppedImage) {

		String lSaveUri = MediaStore.Images.Media.insertImage(
				getContentResolver(), croppedImage, "Titlec", "");

		Intent lIntent = new Intent();
		lIntent.putExtra(AppConstants.CROPPED_IMAGE, lSaveUri);
		setResult(RESULT_OK, lIntent);
		finish();

	}

	@Override
	protected void onPause() {
		super.onPause();
		BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBitmap != null && mBitmap.isRecycled())
			mBitmap.recycle();
	}

	Runnable mRunFaceDetection = new Runnable() {

		float mScale = 1F;
		Matrix mImageMatrix;
		FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
		int mNumFaces;

		// For each face, we create a HightlightView for it.
		private void handleFace(FaceDetector.Face f) {
			PointF midPoint = new PointF();

			int r = ((int) (f.eyesDistance() * mScale)) * 2;
			f.getMidPoint(midPoint);
			midPoint.x *= mScale;
			midPoint.y *= mScale;

			int midX = (int) midPoint.x;
			int midY = (int) midPoint.y;

			HighlightView hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			RectF faceRect = new RectF(midX, midY, midX, midY);
			faceRect.inset(-r, -r);
			if (faceRect.left < 0) {
				faceRect.inset(-faceRect.left, -faceRect.left);
			}

			if (faceRect.top < 0) {
				faceRect.inset(-faceRect.top, -faceRect.top);
			}

			if (faceRect.right > imageRect.right) {
				faceRect.inset(faceRect.right - imageRect.right, faceRect.right
						- imageRect.right);
			}

			if (faceRect.bottom > imageRect.bottom) {
				faceRect.inset(faceRect.bottom - imageRect.bottom,
						faceRect.bottom - imageRect.bottom);
			}

			hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);

			mImageView.add(hv);
		}

		// Create a default HightlightView if we found no face in the picture.
		private void makeDefault() {
			HighlightView hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			// make the default size about 4/5 of the width or height
			int cropWidth = Math.min(width, height) * 4 / 5;
			int cropHeight = cropWidth;

			if (mAspectX != 0 && mAspectY != 0) {
				if (mAspectX > mAspectY) {
					cropHeight = cropWidth * mAspectY / mAspectX;
				} else {
					cropWidth = cropHeight * mAspectX / mAspectY;
				}
			}

			int x = (width - cropWidth) / 2;
			int y = (height - cropHeight) / 2;

			RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
			hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);

			mImageView.mHighlightViews.clear(); // Thong added for rotate

			mImageView.add(hv);
		}

		// Scale the image down for faster face detection.
		private Bitmap prepareBitmap() {
			if (mBitmap == null) {
				return null;
			}

			// 256 pixels wide is enough.
			if (mBitmap.getWidth() > 256) {
				mScale = 256.0F / mBitmap.getWidth();
			}
			Matrix matrix = new Matrix();
			matrix.setScale(mScale, mScale);
			Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
					mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
			return faceBitmap;
		}

		public void run() {
			mImageMatrix = mImageView.getImageMatrix();
			Bitmap faceBitmap = prepareBitmap();

			mScale = 1.0F / mScale;
			if (faceBitmap != null && mDoFaceDetection) {
				FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),
						faceBitmap.getHeight(), mFaces.length);
				mNumFaces = detector.findFaces(faceBitmap, mFaces);
			}

			if (faceBitmap != null && faceBitmap != mBitmap) {
				faceBitmap.recycle();
			}

			mHandler.post(new Runnable() {
				public void run() {
					mWaitingToPick = mNumFaces > 1;
					if (mNumFaces > 0) {
						for (int i = 0; i < mNumFaces; i++) {
							handleFace(mFaces[i]);
						}
					} else {
						makeDefault();
					}
					mImageView.invalidate();
					if (mImageView.mHighlightViews.size() == 1) {
						mCrop = mImageView.mHighlightViews.get(0);
						mCrop.setFocus(true);
					}

					if (mNumFaces > 1) {
						Toast t = Toast.makeText(CropImage.this,
								"Multi face crop help", Toast.LENGTH_SHORT);
						t.show();
					}
				}
			});
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem pItem) {

		switch (pItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return false;
	}

	public static final int NO_STORAGE_ERROR = -1;
	public static final int CANNOT_STAT_ERROR = -2;

}

class CropImageView extends ImageViewTouchBase {
	ArrayList<HighlightView> mHighlightViews = new ArrayList<HighlightView>();
	HighlightView mMotionHighlightView = null;
	float mLastX, mLastY;
	int mMotionEdge;

	private Context mContext;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mBitmapDisplayed.getBitmap() != null) {
			for (HighlightView hv : mHighlightViews) {
				hv.mMatrix.set(getImageMatrix());
				hv.invalidate();
				if (hv.mIsFocused) {
					centerBasedOnHighlightView(hv);
				}
			}
		}
	}

	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	@Override
	protected void zoomTo(float scale, float centerX, float centerY) {
		super.zoomTo(scale, centerX, centerY);
		for (HighlightView hv : mHighlightViews) {
			hv.mMatrix.set(getImageMatrix());
			hv.invalidate();
		}
	}

	@Override
	protected void zoomIn() {
		super.zoomIn();
		for (HighlightView hv : mHighlightViews) {
			hv.mMatrix.set(getImageMatrix());
			hv.invalidate();
		}
	}

	@Override
	protected void zoomOut() {
		super.zoomOut();
		for (HighlightView hv : mHighlightViews) {
			hv.mMatrix.set(getImageMatrix());
			hv.invalidate();
		}
	}

	@Override
	protected void postTranslate(float deltaX, float deltaY) {
		super.postTranslate(deltaX, deltaY);
		for (int i = 0; i < mHighlightViews.size(); i++) {
			HighlightView hv = mHighlightViews.get(i);
			hv.mMatrix.postTranslate(deltaX, deltaY);
			hv.invalidate();
		}
	}

	// According to the event's position, change the focus to the first
	// hitting cropping rectangle.
	private void recomputeFocus(MotionEvent event) {
		for (int i = 0; i < mHighlightViews.size(); i++) {
			HighlightView hv = mHighlightViews.get(i);
			hv.setFocus(false);
			hv.invalidate();
		}

		for (int i = 0; i < mHighlightViews.size(); i++) {
			HighlightView hv = mHighlightViews.get(i);
			int edge = hv.getHit(event.getX(), event.getY());
			if (edge != HighlightView.GROW_NONE) {
				if (!hv.hasFocus()) {
					hv.setFocus(true);
					hv.invalidate();
				}
				break;
			}
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		CropImage cropImage = (CropImage) mContext;
		if (cropImage.mSaving) {
			return false;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (cropImage.mWaitingToPick) {
				recomputeFocus(event);
			} else {
				for (int i = 0; i < mHighlightViews.size(); i++) {
					HighlightView hv = mHighlightViews.get(i);
					int edge = hv.getHit(event.getX(), event.getY());
					if (edge != HighlightView.GROW_NONE) {
						mMotionEdge = edge;
						mMotionHighlightView = hv;
						mLastX = event.getX();
						mLastY = event.getY();
						mMotionHighlightView
								.setMode((edge == HighlightView.MOVE) ? HighlightView.ModifyMode.Move
										: HighlightView.ModifyMode.Grow);
						break;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (cropImage.mWaitingToPick) {
				for (int i = 0; i < mHighlightViews.size(); i++) {
					HighlightView hv = mHighlightViews.get(i);
					if (hv.hasFocus()) {
						cropImage.mCrop = hv;
						for (int j = 0; j < mHighlightViews.size(); j++) {
							if (j == i) {
								continue;
							}
							mHighlightViews.get(j).setHidden(true);
						}
						centerBasedOnHighlightView(hv);
						((CropImage) mContext).mWaitingToPick = false;
						return true;
					}
				}
			} else if (mMotionHighlightView != null) {
				centerBasedOnHighlightView(mMotionHighlightView);
				mMotionHighlightView.setMode(HighlightView.ModifyMode.None);
			}
			mMotionHighlightView = null;
			break;
		case MotionEvent.ACTION_MOVE:
			if (cropImage.mWaitingToPick) {
				recomputeFocus(event);
			} else if (mMotionHighlightView != null) {
				mMotionHighlightView.handleMotion(mMotionEdge, event.getX()
						- mLastX, event.getY() - mLastY);
				mLastX = event.getX();
				mLastY = event.getY();

				if (true) {
					// This section of code is optional. It has some user
					// benefit in that moving the crop rectangle against
					// the edge of the screen causes scrolling but it means
					// that the crop rectangle is no longer fixed under
					// the user's finger.
					ensureVisible(mMotionHighlightView);
				}
			}
			break;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			center(true, true);
			break;
		case MotionEvent.ACTION_MOVE:
			// if we're not zoomed then there's no point in even allowing
			// the user to move the image around. This call to center puts
			// it back to the normalized location (with false meaning don't
			// animate).
			if (getScale() == 1F) {
				center(true, true);
			}
			break;
		}

		return true;
	}

	// Pan the displayed image to make sure the cropping rectangle is visible.
	private void ensureVisible(HighlightView hv) {
		Rect r = hv.mDrawRect;

		int panDeltaX1 = Math.max(0, mLeft - r.left);
		int panDeltaX2 = Math.min(0, mRight - r.right);

		int panDeltaY1 = Math.max(0, mTop - r.top);
		int panDeltaY2 = Math.min(0, mBottom - r.bottom);

		int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
		int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

		if (panDeltaX != 0 || panDeltaY != 0) {
			panBy(panDeltaX, panDeltaY);
		}
	}

	// If the cropping rectangle's size changed significantly, change the
	// view's center and scale according to the cropping rectangle.
	private void centerBasedOnHighlightView(HighlightView hv) {
		Rect drawRect = hv.mDrawRect;

		float width = drawRect.width();
		float height = drawRect.height();

		float thisWidth = getWidth();
		float thisHeight = getHeight();

		float z1 = thisWidth / width * .6F;
		float z2 = thisHeight / height * .6F;

		float zoom = Math.min(z1, z2);
		zoom = zoom * this.getScale();
		zoom = Math.max(1F, zoom);
		if ((Math.abs(zoom - getScale()) / zoom) > .1) {
			float[] coordinates = new float[] { hv.mCropRect.centerX(),
					hv.mCropRect.centerY() };
			getImageMatrix().mapPoints(coordinates);
			zoomTo(zoom, coordinates[0], coordinates[1], 300F);
		}

		ensureVisible(hv);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < mHighlightViews.size(); i++) {
			mHighlightViews.get(i).draw(canvas);
		}
	}

	public void add(HighlightView hv) {
		mHighlightViews.add(hv);
		invalidate();
	}

}

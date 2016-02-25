package com.muhurtmaza.utility;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MuhurtMazaApplication extends Application {

	public static ImageLoader imageLoader;

	@Override
	public void onCreate() {
		super.onCreate();

		printhashkey();
	}

	public static ImageLoader getImageLoader(Context context) {

		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(getConfiguration(context));
		}

		return imageLoader;
	}

	private static ImageLoaderConfiguration getConfiguration(Context context) {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();

		return config;
	}
	
	public void printhashkey() {

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.muhurt.maza", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String s = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Toast.makeText(getApplicationContext(), "KeyHash are :- " + s, Toast.LENGTH_LONG).show();
          
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        
}

}

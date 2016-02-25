package com.muhurtmaza.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.muhurtmaza.R;

/**
 * Created by sandeep on 11/12/15.
 */
public class BadgeUtils {
    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}

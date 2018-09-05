package com.shabayekdes.easybaking.imageUtils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

public class ImageHelper {
    public static RoundedBitmapDrawable getRoundedCornerBitmap(Resources mResources, int imageResource, float cornerRadius) {
        Bitmap mBitmap = BitmapFactory.decodeResource(mResources, imageResource);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, mBitmap);
        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        roundedBitmapDrawable.setAntiAlias(true);

        return roundedBitmapDrawable;
    }
}

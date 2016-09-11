package com.esummer.android.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.esummer.android.R;

/**
 * Created by cwj on 16/7/28.
 */
public class BadgeUtils {
    private static Bitmap badgeBitmap;

    private static final int BADGE_CIRCLE_COLOR = R.color.Badge_Color_Red;
    private static final int BADGE_TEXT_COLOR = Color.WHITE;

    private static Bitmap buildTransparentBitmap(Drawable drawable, int padding) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth() + padding * 2, bitmap.getHeight() + padding * 2, bitmap.getConfig());
        canvas = new Canvas(bitmap2);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bitmap, padding, padding, null);
        return bitmap2;
    }

    public static BitmapDrawable buildBadgeDrawable(Drawable drawable, String msg, Resources resources) {
        int badgeRadius = resources.getDimensionPixelSize(R.dimen.Badge_Circle_Radius);
        int badgePadding = resources.getDimensionPixelSize(R.dimen.Badge_Icon_Padding);
        if(badgeBitmap == null) {
            badgeBitmap = buildTransparentBitmap(drawable, badgePadding);
        }

        Paint paint = new Paint();
        Canvas canvas = new Canvas(badgeBitmap);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(resources.getColor(BADGE_CIRCLE_COLOR));
        canvas.drawCircle((badgeBitmap.getWidth() - badgeRadius), badgeRadius, badgeRadius, paint);

        paint.setColor(BADGE_TEXT_COLOR);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(resources.getDimensionPixelSize(R.dimen.Badge_TextSize));
        Rect rect = new Rect();
        paint.getTextBounds(msg, 0, msg.length(), rect);
        int textOriginX = canvas.getWidth() - badgeRadius;
        int textBaselineY = (int) (badgeRadius - (paint.descent() + paint.ascent()/2.0F));
        canvas.drawText(msg, textOriginX, textBaselineY, paint);

        return new BitmapDrawable(resources, badgeBitmap);
    }
}

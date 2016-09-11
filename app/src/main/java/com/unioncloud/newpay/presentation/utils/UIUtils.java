package com.unioncloud.newpay.presentation.utils;

import android.widget.ListPopupWindow;
import android.widget.Spinner;

import com.raizlabs.coreutils.view.ViewUtils;

import java.lang.reflect.Field;

/**
 * Created by cwj on 16/8/15.
 */
public class UIUtils {

    public static void setSpinnerHeight(Spinner spinner, int heightDp) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(spinner);

            int heightPx = (int) ViewUtils.dipsToPixels(heightDp, spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(heightPx);
            spinner.requestLayout();
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }
}

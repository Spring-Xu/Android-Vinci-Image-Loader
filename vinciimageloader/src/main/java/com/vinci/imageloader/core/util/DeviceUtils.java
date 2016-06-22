package com.vinci.imageloader.core.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by SpringXu on 15/9/3.
 */
public class DeviceUtils {
    public static int getScreenWith(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }
}

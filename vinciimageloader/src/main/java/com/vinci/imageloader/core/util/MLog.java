package com.vinci.imageloader.core.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * VinciProxy log printer
 *
 * @author SpringXu
 * @Email hquspring@gmail.com
 */
public class MLog {


    public static final String TAG = "VinciProxy";

    public static boolean DEBUG = false;

    public static void i(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (DEBUG) {
            Log.i(tag, msg);
        }

    }

    public static void d(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        Log.e(tag, msg);
    }

    public static void i(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Log.e(TAG, msg);
    }
}
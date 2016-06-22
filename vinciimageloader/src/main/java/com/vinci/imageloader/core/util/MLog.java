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
    private static final String TAG = "Vinci";

    public static boolean DEBUG = false;

    public static void i(String tag, String msg) {
        if (DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.e(tag, msg);
    }

    public static void i(String msg) {
        if (DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (DEBUG || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.e(TAG, msg);
    }
}
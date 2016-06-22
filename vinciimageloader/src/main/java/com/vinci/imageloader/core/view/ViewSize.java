package com.vinci.imageloader.core.view;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.vinci.imageloader.core.util.DeviceUtils;
import com.vinci.imageloader.core.util.MLog;

import java.lang.reflect.Field;

/**
 * Created by SpringXu on 6/13/16.
 */
public class ViewSize {
    public int width;
    public int height;

    public ViewSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ViewSize(ImageView imageView) {

        if (imageView == null) {
            return;
        }

        if (imageView.getLayoutParams() != null) {
            this.width = imageView.getLayoutParams().width;
            this.height = imageView.getLayoutParams().height;
        }

        if (this.width != 0 || this.height != 0) {
            return;
        }

        this.width = imageView.getWidth();
        this.height = imageView.getHeight();

        if (this.width != 0 || this.height != 0) {
            return;
        }

        if (getDeclaredField(imageView, "mMaxWidth") != Integer.MAX_VALUE && getDeclaredField(imageView, "mMaxHeight") != Integer.MAX_VALUE) {
            this.width = getDeclaredField(imageView, "mMaxWidth");
            this.height = getDeclaredField(imageView, "mMaxHeight");
        }

        if (this.width != 0 || this.height != 0) {
            return;
        }

        //if the image view is match parent or wrap content, set the default by screen size
        boolean isWrapOrMatch = this.width == ViewGroup.LayoutParams.WRAP_CONTENT || this.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                this.width == ViewGroup.LayoutParams.MATCH_PARENT || this.height == ViewGroup.LayoutParams.MATCH_PARENT;

        if (imageView != null && (isWrapOrMatch || this.width == 0 || this.height == 0)) {
            //default set the desW as half of the screen width,and the same as height
            this.width = DeviceUtils.getScreenWith(imageView.getContext()) / 5;
            this.height = DeviceUtils.getScreenHeight(imageView.getContext()) / 10;
        }
    }

    protected int getDeclaredField(ImageView imageView, String fieldName) {
        int fieldValue = Integer.MAX_VALUE;
        try {
            Field privateIntField = ImageView.class.getDeclaredField(fieldName);
            privateIntField.setAccessible(true);//允许访问私有字段
            fieldValue = privateIntField.getInt(imageView);//获得私有字段值
        } catch (Exception ne) {
            MLog.e(ne.getMessage());
        }

        return fieldValue;
    }
}

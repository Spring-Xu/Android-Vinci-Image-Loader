package com.vinci.imageloader.core.view;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by SpringXu on 6/16/16.
 */
public interface VinciView {
    ViewSize getViewSize();

    void setBitmap(Bitmap bitmap);

    String getViewKey();

    void setViewKey(String key);

    int getWidth();

    int getHeight();

    void invalidate();

    ViewGroup.LayoutParams getLayoutParams();

    boolean equals(VinciView imageView);

    void clear();

    int getId();

    void setImageResource(int resId);

    ImageView getWapView();
}

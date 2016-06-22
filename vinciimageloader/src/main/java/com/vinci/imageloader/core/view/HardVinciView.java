package com.vinci.imageloader.core.view;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by SpringXu on 6/13/16.
 */
public class HardVinciView implements VinciView {
    private WeakReference<ImageView> mImageView;
    private ViewSize viewSize;
    private String viewKey;

    public HardVinciView(ImageView imageView) {
        this.mImageView = new WeakReference<ImageView>(imageView);
    }

    public ViewSize getViewSize() {
        if (viewSize == null) {
            viewSize = new ViewSize(mImageView.get());
        }

        return viewSize;
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled() && mImageView.get() != null) {
            mImageView.get().setImageBitmap(bitmap);
        }
    }

    public String getViewKey() {
        return viewKey;
    }

    public void setViewKey(String key) {
        this.viewKey = key;
    }

    public int getWidth() {
        return getViewSize().width;
    }

    public int getHeight() {
        return getViewSize().height;
    }

    public void invalidate() {
        if (mImageView.get() != null) {
            mImageView.get().invalidate();
        }
    }

    public ViewGroup.LayoutParams getLayoutParams() {
        if (mImageView.get() != null) {
            return mImageView.get().getLayoutParams();
        } else {
            return null;
        }
    }

    public boolean equals(VinciView imageView) {
        if (this == imageView) {
            return true;
        }

        if (imageView.getWapView() != null) {
            return mImageView.get() == imageView.getWapView();
        }

        return false;
    }

    public void clear() {
        mImageView.clear();
    }

    @Override
    public int getId() {
        ImageView view = getWapView();
        return view == null ? super.hashCode() : view.hashCode();
    }

    @Override
    public void setImageResource(int resId) {
        if (getWapView() != null) {
            getWapView().setImageDrawable(mImageView.get().getResources().getDrawable(resId));
        }
    }

    @Override
    public ImageView getWapView() {
        return mImageView.get();
    }
}

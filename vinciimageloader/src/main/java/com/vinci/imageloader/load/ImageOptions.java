package com.vinci.imageloader.load;

/**
 * @author Spring-Xu
 * @email hquspring@gmail.com
 * @date 2016-6-20
 */
public class ImageOptions {
    private boolean useMemoryCache;
    private boolean useFileCache;
    private int defaultImageResId;
    private int errorImageResId;
    private int loadingImageResId;

    /**
     * This tag is used to control whether it will show original image
     */
    private boolean showOriginal;

    /**
     * Round image corner size
     */
    private int cornerSize;

    private ImageOptions(){
        //TODO corner size and SHOW_ORIGINAL is not used
    }

    public int getCornerSize() {
        return cornerSize;
    }

    public boolean hasCorner() {
        return cornerSize != 0;
    }

    public boolean isShowOriginal() {
        return showOriginal;
    }

    public boolean isUseMemoryCache() {
        return useMemoryCache;
    }

    public boolean isUseFileCache() {
        return useFileCache;
    }

    public int getDefaultImageResId() {
        return defaultImageResId;
    }

    public boolean shouldShowDefaultImage() {
        return defaultImageResId != 0;
    }

    public int getErrorImageResId() {
        return errorImageResId;
    }

    public boolean shouldShowErrorImage() {
        return errorImageResId != 0;
    }

    public int getLoadingImageResId() {
        return loadingImageResId;
    }

    public boolean shouldShowLoadingIamge() {
        return loadingImageResId != 0;
    }

    public static class Builder {
        private boolean useMemoryCache;
        private boolean useFileCache;
        private int defaultImageResId;
        private int errorImageResId;
        private int loadingImageResId;
        /**
         * This tag is used to control whether it will show original image
         */
        private boolean showOriginal;

        /**
         * Round image corner size
         */
        private int cornerSize;

        public Builder setCornerSize(int cornerSize) {
            this.cornerSize = cornerSize;
            return this;
        }

        public Builder setShowOriginal(boolean showOriginal) {
            this.showOriginal = showOriginal;
            return this;
        }

        public Builder setUseMemoryCache(boolean useMemoryCache) {
            this.useMemoryCache = useMemoryCache;
            return this;
        }

        public Builder setUseFileCache(boolean useFileCache) {
            this.useFileCache = useFileCache;
            return this;
        }

        public Builder setDefaultImageResId(int defaultImageResId) {
            this.defaultImageResId = defaultImageResId;
            return this;
        }

        public Builder setErrorImageResId(int errorImageResId) {
            this.errorImageResId = errorImageResId;
            return this;
        }

        public Builder setLoadingImageResId(int loadingImageResId) {
            this.loadingImageResId = loadingImageResId;
            return this;
        }

        public ImageOptions build() {
            ImageOptions options = new ImageOptions();
            options.defaultImageResId = this.defaultImageResId;
            options.errorImageResId = this.errorImageResId;
            options.useFileCache = this.useFileCache;
            options.useMemoryCache = this.useMemoryCache;
            options.loadingImageResId = this.loadingImageResId;
            options.showOriginal = this.showOriginal;
            options.cornerSize = this.cornerSize;
            return options;
        }
    }
}

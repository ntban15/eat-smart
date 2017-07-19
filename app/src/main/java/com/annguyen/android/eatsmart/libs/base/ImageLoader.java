package com.annguyen.android.eatsmart.libs.base;

import android.widget.ImageView;

/**
 * Created by annguyen on 6/1/2017.
 */

public interface ImageLoader {
    void load(ImageView imageView, String imageUrl);
    void setRequestListener(Object requestListener);
}

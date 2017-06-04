package com.annguyen.android.eatsmart.libs;

import android.widget.ImageView;

import com.annguyen.android.eatsmart.libs.base.ImageLoader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by annguyen on 6/1/2017.
 */

public class GlideImageLoader implements ImageLoader {
    private RequestManager glideRequestManager;
    private RequestListener requestListener;

    public GlideImageLoader(RequestManager glideRequestManager) {
        this.glideRequestManager = glideRequestManager;
    }

    public void setRequestListener(Object requestListener) {
        if (requestListener instanceof  RequestListener)
            this.requestListener = (RequestListener) requestListener;
    }

    @Override
    public void load(ImageView imageView, String imageUrl) {
        if (requestListener == null) {
            glideRequestManager.load(imageUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(imageView);
        }
        else {
            glideRequestManager.load(imageUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(requestListener)
                    .into(imageView);

        }
    }
}

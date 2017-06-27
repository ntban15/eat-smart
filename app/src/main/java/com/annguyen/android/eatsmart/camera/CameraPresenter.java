package com.annguyen.android.eatsmart.camera;

/**
 * Created by annguyen on 6/2/2017.
 */

public interface CameraPresenter {
    void initCamera();
    void onStart();
    void onStop();
    void takePicture();
}

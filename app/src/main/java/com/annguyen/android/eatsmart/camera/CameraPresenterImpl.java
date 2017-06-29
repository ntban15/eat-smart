package com.annguyen.android.eatsmart.camera;

import android.hardware.camera2.CameraManager;

import com.annguyen.android.eatsmart.libs.base.EventBus;

/**
 * Created by khoanguyen on 6/8/17.
 */

public class CameraPresenterImpl implements CameraPresenter {

    private EventBus eventBus;
    private byte[] bytes;

    public CameraPresenterImpl(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void onStart() {
        //register to the EventBus
        eventBus.register(this);
    }

    @Override
    public void onStop() {
        //unregister to the EventBus
        eventBus.unregister(this);
    }

    @Override
    public void onAnalyzeButtonClicked() {

    }
}

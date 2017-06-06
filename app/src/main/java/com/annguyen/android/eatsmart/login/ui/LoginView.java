package com.annguyen.android.eatsmart.login.ui;

/**
 * Created by annguyen on 6/4/2017.
 */

public interface LoginView {

    void showProgressBar();

    void hideProgressBar();

    void showContent();

    void hideContent();

    void goToMainActivity();

    void onError(String errMsg);
}

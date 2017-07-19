package com.annguyen.android.eatsmart.login;

import com.annguyen.android.eatsmart.login.events.LoginEvent;

/**
 * Created by annguyen on 6/4/2017.
 */

public interface LoginPresenter {

    void onFacebookLoginSuccess(String token);

    void onFacebookLoginCancel();

    void onFacebookLoginError(String message);

    void checkLoginState();

    void onLoginEvent(LoginEvent loginEvent);

    void start();

    void stop();
}

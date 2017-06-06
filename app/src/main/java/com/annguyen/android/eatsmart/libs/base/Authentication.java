package com.annguyen.android.eatsmart.libs.base;

/**
 * Created by annguyen on 6/4/2017.
 */

public interface Authentication {

    boolean checkLoginState();

    void loginWithFacebook(String token);

    void setOnLoginCompleteListener(Object onLoginCompleteListener);

    void logout();
}

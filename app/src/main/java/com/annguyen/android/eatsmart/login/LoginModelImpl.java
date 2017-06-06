package com.annguyen.android.eatsmart.login;

import android.support.annotation.NonNull;

import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.login.events.LoginEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by annguyen on 6/4/2017.
 */

public class LoginModelImpl implements LoginModel {

    private EventBus eventBus;
    private Authentication authentication;

    public LoginModelImpl(EventBus eventBus, Authentication authentication) {
        this.eventBus = eventBus;
        this.authentication = authentication;
    }

    @Override
    public void loginWithFacebook(String token) {
        //create a new listener for loginWithFacebook
        OnCompleteListener onCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    postLoginEvent(LoginEvent.SUCCESS);
                }
                else {
                    postLoginEvent(LoginEvent.FAIL);
                }
            }
        };

        authentication.setOnLoginCompleteListener(onCompleteListener);
        authentication.loginWithFacebook(token);
    }

    @Override
    public void checkLogin() {
        if (authentication.checkLoginState()) {
            postLoginEvent(LoginEvent.LOGGED_IN);
        }
        else {
            postLoginEvent(LoginEvent.NOT_LOGGED_IN);
        }
    }

    private void postLoginEvent(String code) {
        eventBus.post(new LoginEvent(code));
    }
}

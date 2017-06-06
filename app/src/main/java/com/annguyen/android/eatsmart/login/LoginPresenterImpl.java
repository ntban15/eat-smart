package com.annguyen.android.eatsmart.login;

import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.login.events.LoginEvent;
import com.annguyen.android.eatsmart.login.ui.LoginView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by annguyen on 6/4/2017.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private EventBus eventBus;
    private LoginView loginView;
    private LoginModel loginModel;

    public LoginPresenterImpl(EventBus eventBus, LoginView loginView, LoginModel loginModel) {
        this.eventBus = eventBus;
        this.loginView = loginView;
        this.loginModel = loginModel;
    }

    @Override
    public void onFacebookLoginSuccess(String token) {
        loginView.hideContent();
        loginView.showProgressBar();
        loginModel.loginWithFacebook(token);
    }

    @Override
    public void onFacebookLoginCancel() {}

    @Override
    public void onFacebookLoginError(String message) {
        loginView.onError(message);
    }

    @Override
    public void checkLoginState() {
        loginView.hideContent();
        loginView.showProgressBar();
        loginModel.checkLogin();
    }

    @Subscribe
    @Override
    public void onLoginEvent(LoginEvent loginEvent) {
        switch(loginEvent.getEventCode()) {
            case LoginEvent.SUCCESS:
                loginView.hideProgressBar();
                loginView.showContent();
                loginView.goToMainActivity();
                break;
            case LoginEvent.FAIL:
                loginView.hideProgressBar();
                loginView.showContent();
                loginView.onError(LoginEvent.FAIL);
                break;
            case LoginEvent.LOGGED_IN:
                loginView.hideProgressBar();
                loginView.showContent();
                loginView.goToMainActivity();
                break;
            case LoginEvent.NOT_LOGGED_IN:
                loginView.hideProgressBar();
                loginView.showContent();
                break;
        }
    }

    @Override
    public void start() {
        eventBus.register(this);
    }

    @Override
    public void stop() {
        eventBus.unregister(this);
        loginView = null;
    }
}

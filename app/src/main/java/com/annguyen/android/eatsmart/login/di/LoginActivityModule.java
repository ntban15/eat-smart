package com.annguyen.android.eatsmart.login.di;

import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.login.LoginModel;
import com.annguyen.android.eatsmart.login.LoginModelImpl;
import com.annguyen.android.eatsmart.login.LoginPresenter;
import com.annguyen.android.eatsmart.login.LoginPresenterImpl;
import com.annguyen.android.eatsmart.login.ui.LoginView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by annguyen on 6/4/2017.
 */

@Module
public class LoginActivityModule {

    LoginView loginView;

    public LoginActivityModule(LoginView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    LoginPresenter provideLoginPresenter(EventBus eventBus, LoginView loginView, LoginModel loginModel) {
        return new LoginPresenterImpl(eventBus, loginView, loginModel);
    }

    @Provides
    @Singleton
    LoginView provideLoginView() {
        return this.loginView;
    }

    @Provides
    @Singleton
    LoginModel provideLoginModel(EventBus eventBus, Authentication authentication) {
        return new LoginModelImpl(eventBus, authentication);
    }
}

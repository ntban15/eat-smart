package com.annguyen.android.eatsmart.login.di;

import com.annguyen.android.eatsmart.libs.di.LibsModule;
import com.annguyen.android.eatsmart.login.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by annguyen on 6/4/2017.
 */
@Singleton @Component(modules = {LibsModule.class, LoginActivityModule.class})
public interface LoginActivityComponent {
    void inject(LoginActivity loginActivity);
}

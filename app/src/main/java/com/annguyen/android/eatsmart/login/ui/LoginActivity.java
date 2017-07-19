package com.annguyen.android.eatsmart.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.annguyen.android.eatsmart.EatSmartApp;
import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.login.LoginPresenter;
import com.annguyen.android.eatsmart.login.di.LoginActivityComponent;
import com.annguyen.android.eatsmart.main.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.fb_login_button)
    LoginButton facebookLoginBtn;
    @BindView(R.id.login_activity_container)
    RelativeLayout loginContainer;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    LoginPresenter presenter;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupInjection();
        presenter.start();

        //check if user is logged in
        presenter.checkLoginState();

        //setup facebook callback
        setupFacebookCallback();
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }

    private void setupInjection() {
        EatSmartApp app = (EatSmartApp) getApplication();
        LoginActivityComponent loginActivityComponent = app.getLoginActivityComponent(this);
        loginActivityComponent.inject(this);
    }

    @Override
    public void onError(String errMsg) {
        Snackbar.make(loginContainer, errMsg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        facebookLoginBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        facebookLoginBtn.setVisibility(View.GONE);
    }

    @Override
    public void goToMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setupFacebookCallback() {
        callbackManager = CallbackManager.Factory.create();
        facebookLoginBtn.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        presenter.onFacebookLoginSuccess(loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        presenter.onFacebookLoginCancel();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        presenter.onFacebookLoginError(error.getLocalizedMessage());
                    }
                });
    }
}

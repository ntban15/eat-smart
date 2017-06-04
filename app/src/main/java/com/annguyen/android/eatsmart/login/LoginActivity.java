package com.annguyen.android.eatsmart.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.main.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    CallbackManager facebookCallback;

    @BindView(R.id.login_button)
    LoginButton facebookLoginBtn;
    @BindView(R.id.login_activity_container)
    RelativeLayout loginContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        facebookCallback = CallbackManager.Factory.create();
        //check if already login
        if (AccessToken.getCurrentAccessToken() == null) {
            facebookLoginBtn.registerCallback(facebookCallback,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            goToMainActivity();
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(FacebookException error) {
                            showError(error.getLocalizedMessage());
                        }
                    });
        }
        else
            goToMainActivity();
    }

    private void showError(String localizedMessage) {
        Snackbar.make(loginContainer, localizedMessage, Snackbar.LENGTH_SHORT).show();
    }

    private void goToMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallback.onActivityResult(requestCode, resultCode, data);
    }
}

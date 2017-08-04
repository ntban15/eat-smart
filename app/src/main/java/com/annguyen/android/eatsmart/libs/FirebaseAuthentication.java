package com.annguyen.android.eatsmart.libs;

import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by annguyen on 6/4/2017.
 */

public class FirebaseAuthentication implements Authentication {


    private FirebaseAuth firebaseAuth;
    private OnCompleteListener onCompleteListener;

    public FirebaseAuthentication(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void setOnLoginCompleteListener(Object onCompleteListener) {
        if (onCompleteListener instanceof OnCompleteListener) {
            this.onCompleteListener = (OnCompleteListener) onCompleteListener;
        }
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public String getUserUID() {
        return checkLoginState() ? firebaseAuth.getCurrentUser().getUid() : null;
    }

    @Override
    public boolean checkLoginState() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public void loginWithFacebook(String token) {
        if (onCompleteListener != null) {
            firebaseAuth.signInWithCredential(
                    FacebookAuthProvider.getCredential(token))
                    .addOnCompleteListener(onCompleteListener);
        }
    }
}

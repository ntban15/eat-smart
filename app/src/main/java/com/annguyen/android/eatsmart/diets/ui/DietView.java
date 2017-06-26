package com.annguyen.android.eatsmart.diets.ui;

import com.annguyen.android.eatsmart.entities.Diet;

import java.util.List;

/**
 * Created by annguyen on 6/1/2017.
 */

public interface DietView {

    void onError(String errMsg);

    void goToLogin();

    void showProgressBar();

    void hideProgressBar();

    void showContent();

    void hideContent();

    void setDietContent(List<Diet> dietList);
}

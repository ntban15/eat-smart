package com.annguyen.android.eatsmart;

import android.app.Application;

import com.annguyen.android.eatsmart.diets.di.DaggerDietFragmentComponent;
import com.annguyen.android.eatsmart.diets.di.DietFragmentComponent;
import com.annguyen.android.eatsmart.diets.di.DietFragmentModule;
import com.annguyen.android.eatsmart.diets.ui.DietView;
import com.annguyen.android.eatsmart.libs.di.LibsModule;

/**
 * Created by annguyen on 6/1/2017.
 */

public class EatSmartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public DietFragmentComponent getDietFragmentComponent(DietView dietView) {
        return DaggerDietFragmentComponent.builder()
                .dietFragmentModule(new DietFragmentModule(dietView))
                .libsModule(new LibsModule(null))
                .build();
    }
}
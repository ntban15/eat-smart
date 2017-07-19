package com.annguyen.android.eatsmart.diets.di;

import com.annguyen.android.eatsmart.diets.ui.DietFragment;
import com.annguyen.android.eatsmart.libs.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by annguyen on 6/4/2017.
 */

@Singleton @Component(modules = {DietFragmentModule.class, LibsModule.class})
public interface DietFragmentComponent {
    void inject(DietFragment dietFragment);
}

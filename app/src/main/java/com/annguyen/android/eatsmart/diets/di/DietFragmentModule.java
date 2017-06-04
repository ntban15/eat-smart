package com.annguyen.android.eatsmart.diets.di;

import com.annguyen.android.eatsmart.diets.DietModel;
import com.annguyen.android.eatsmart.diets.DietModelImpl;
import com.annguyen.android.eatsmart.diets.DietPresenter;
import com.annguyen.android.eatsmart.diets.DietPresenterImpl;
import com.annguyen.android.eatsmart.diets.ui.DietView;
import com.annguyen.android.eatsmart.libs.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by annguyen on 6/4/2017.
 */

@Module
public class DietFragmentModule {

    private DietView dietView;

    public DietFragmentModule(DietView dietView) {
        this.dietView = dietView;
    }

    @Provides
    @Singleton
    DietPresenter provideDietPresenter(EventBus eventBus, DietView dietView, DietModel dietModel) {
        return new DietPresenterImpl(eventBus, dietView, dietModel);
    }

    @Provides
    @Singleton
    DietView provideDietView() {
        return this.dietView;
    }

    @Provides
    @Singleton
    DietModel provideDietModel(EventBus eventBus) {
        return new DietModelImpl(eventBus);
    }
}

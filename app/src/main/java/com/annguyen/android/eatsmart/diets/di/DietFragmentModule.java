package com.annguyen.android.eatsmart.diets.di;

import com.annguyen.android.eatsmart.diets.DietModel;
import com.annguyen.android.eatsmart.diets.DietModelImpl;
import com.annguyen.android.eatsmart.diets.DietPresenter;
import com.annguyen.android.eatsmart.diets.DietPresenterImpl;
import com.annguyen.android.eatsmart.diets.adapters.DietListAdapter;
import com.annguyen.android.eatsmart.diets.adapters.OnDietItemClickListener;
import com.annguyen.android.eatsmart.diets.ui.DietView;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by annguyen on 6/4/2017.
 */

@Module
public class DietFragmentModule {

    private DietView dietView;
    private OnDietItemClickListener onDietItemClickListener;

    public DietFragmentModule(DietView dietView, OnDietItemClickListener onDietItemClickListener) {
        this.dietView = dietView;
        this.onDietItemClickListener = onDietItemClickListener;
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
    DietModel provideDietModel(EventBus eventBus, Authentication authentication, RealtimeDatabase realtimeDatabase) {
        return new DietModelImpl(eventBus, authentication, realtimeDatabase);
    }

    @Provides
    @Singleton
    DietListAdapter provideDietListAdapter(List<Diet> dietDataset, OnDietItemClickListener onDietItemClickListener) {
        return new DietListAdapter(dietDataset, onDietItemClickListener);
    }

    @Provides
    @Singleton
    OnDietItemClickListener provideOnDietItemClickListener() {
        return onDietItemClickListener;
    }

    @Provides
    @Singleton
    List<Diet> provideDietDataset() {
        return new ArrayList<>();
    }
}

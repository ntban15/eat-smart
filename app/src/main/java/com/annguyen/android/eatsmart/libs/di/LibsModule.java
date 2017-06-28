package com.annguyen.android.eatsmart.libs.di;

import android.support.v4.app.Fragment;

import com.annguyen.android.eatsmart.libs.FirebaseAuthentication;
import com.annguyen.android.eatsmart.libs.FirebaseRealtimeDatabase;
import com.annguyen.android.eatsmart.libs.GlideImageLoader;
import com.annguyen.android.eatsmart.libs.GreenRobotEventBus;
import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.libs.base.ImageLoader;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by annguyen on 6/3/2017.
 */

@Module
public class LibsModule {

    private Fragment target;

    public LibsModule(Fragment fragment) {
        this.target = fragment;
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader(RequestManager requestManager) {
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager provideRequestManager(Fragment fragment) {
        return Glide.with(fragment);
    }

    @Provides
    @Singleton
    Fragment provideFragment() {
        return this.target;
    }

    @Provides
    @Singleton
    EventBus provideEventBus(org.greenrobot.eventbus.EventBus eventBus) {
        return new GreenRobotEventBus(eventBus);
    }

    @Provides
    @Singleton
    org.greenrobot.eventbus.EventBus provideGreenRobot() {
        return org.greenrobot.eventbus.EventBus.getDefault();
    }

    @Provides
    @Singleton
    Authentication provideAuthentication(FirebaseAuth firebaseAuth) {
        return new FirebaseAuthentication(firebaseAuth);
    }

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    RealtimeDatabase provideRealtimeDatabase(FirebaseDatabase firebaseDatabase, Authentication authentication) {
        return new FirebaseRealtimeDatabase(firebaseDatabase, authentication);
    }

    @Provides
    @Singleton
    FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }
}

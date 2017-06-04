package com.annguyen.android.eatsmart.diets;

import com.annguyen.android.eatsmart.diets.events.LogoutEvent;
import com.annguyen.android.eatsmart.diets.ui.DietView;
import com.annguyen.android.eatsmart.libs.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by annguyen on 6/4/2017.
 */

public class DietPresenterImpl implements DietPresenter {

    private EventBus eventBus;
    private DietView dietView;
    private DietModel dietModel;

    public DietPresenterImpl(EventBus eventBus, DietView dietView, DietModel dietModel) {
        this.eventBus = eventBus;
        this.dietView = dietView;
        this.dietModel = dietModel;
    }

    @Override
    public void start() {
        eventBus.register(this);
    }

    @Override
    public void stop() {
        eventBus.unregister(this);
        dietView = null;
    }

    @Override
    public void onLogoutSelected() {
        dietModel.logout();
    }

    @Subscribe
    @Override
    public void onLogoutEvent(LogoutEvent logoutEvent) {
        dietView.goToLogin();
    }
}

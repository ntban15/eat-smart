package com.annguyen.android.eatsmart.diets;

import com.annguyen.android.eatsmart.diets.events.LogoutEvent;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.facebook.login.LoginManager;

/**
 * Created by annguyen on 6/4/2017.
 */

public class DietModelImpl implements DietModel {

    private EventBus eventBus;

    public DietModelImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
        eventBus.post(new LogoutEvent());
    }
}

package com.annguyen.android.eatsmart.diets;

import com.annguyen.android.eatsmart.diets.events.LogoutEvent;
import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.facebook.login.LoginManager;

/**
 * Created by annguyen on 6/4/2017.
 */

public class DietModelImpl implements DietModel {

    private EventBus eventBus;
    private Authentication authentication;

    public DietModelImpl(EventBus eventBus, Authentication authentication) {
        this.eventBus = eventBus;
        this.authentication = authentication;
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut(); //sign out from facebook
        authentication.logout();    //sign out form firebase
        eventBus.post(new LogoutEvent());
    }
}

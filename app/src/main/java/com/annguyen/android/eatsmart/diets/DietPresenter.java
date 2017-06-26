package com.annguyen.android.eatsmart.diets;

import com.annguyen.android.eatsmart.diets.events.LogoutEvent;

/**
 * Created by annguyen on 6/1/2017.
 */

public interface DietPresenter {
    void start();

    void stop();

    void onLogoutSelected();

    void onLogoutEvent(LogoutEvent logoutEvent);

    void initUI();
}

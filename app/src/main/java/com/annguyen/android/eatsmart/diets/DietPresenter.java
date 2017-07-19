package com.annguyen.android.eatsmart.diets;

import com.annguyen.android.eatsmart.diets.events.ActiveDietChange;
import com.annguyen.android.eatsmart.diets.events.DietMetaEvent;
import com.annguyen.android.eatsmart.diets.events.LogoutEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by annguyen on 6/1/2017.
 */

public interface DietPresenter {
    void start();

    void stop();

    void onLogoutSelected();

    void onLogoutEvent(LogoutEvent logoutEvent);

    void initUI();

    @Subscribe
    void onDietMetaEvent(DietMetaEvent dietMetaEvent);

    @Subscribe
    void onActiveDietEvent(ActiveDietChange activeDietChange);

    void setActive(String dietKey);
}

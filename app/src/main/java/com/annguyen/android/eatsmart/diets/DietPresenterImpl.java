package com.annguyen.android.eatsmart.diets;

import com.annguyen.android.eatsmart.diets.events.ActiveDietChange;
import com.annguyen.android.eatsmart.diets.events.DietMetaEvent;
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
        dietModel.stop();
    }

    @Override
    public void onLogoutSelected() {
        dietModel.logout();
    }


    @Override
    public void initUI() {
        //dietView.hideContent();
        //dietView.showProgressBar();
        dietModel.getDietsMeta();
    }

    @Subscribe
    @Override
    public void onLogoutEvent(LogoutEvent logoutEvent) {
        dietView.goToLogin();
    }

    @Subscribe
    @Override
    public void onDietMetaEvent(DietMetaEvent dietMetaEvent) {
        //dietView.showContent();
        //dietView.hideProgressBar();
        switch (dietMetaEvent.getEventCode()) {
            case DietMetaEvent.DIET_META_SUCCESS: {
                dietView.addNewDiet(dietMetaEvent.getNewDiet());
                break;
            }
            case DietMetaEvent.DIET_META_FAIL: {
                dietView.onError(dietMetaEvent.getErrMsg());
                break;
            }
        }
    }

    @Subscribe
    @Override
    public void onActiveDietEvent(ActiveDietChange activeDietChange) {
        String dietKey = activeDietChange.getDietKey();
        if (dietKey != null) {
            //dietView.setActiveDiet(dietKey);
        }
    }

    @Override
    public void setActive(String dietKey) {
        dietModel.setActive(dietKey);
    }
}

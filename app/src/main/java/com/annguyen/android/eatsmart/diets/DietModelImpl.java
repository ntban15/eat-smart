package com.annguyen.android.eatsmart.diets;

import com.annguyen.android.eatsmart.diets.events.LogoutEvent;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annguyen on 6/4/2017.
 */

public class DietModelImpl implements DietModel {

    private EventBus eventBus;
    private Authentication authentication;
    private RealtimeDatabase database;

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

    @Override
    public void getDietsMeta() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Diet> dietList = new ArrayList<>();
                for (DataSnapshot diet : dataSnapshot.getChildren()) {
                    Diet newDiet = diet.getValue(Diet.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.getDietsMeta();
    }
}

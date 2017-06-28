package com.annguyen.android.eatsmart.diets;

import com.annguyen.android.eatsmart.diets.events.ActiveDietChange;
import com.annguyen.android.eatsmart.diets.events.DietMetaEvent;
import com.annguyen.android.eatsmart.diets.events.LogoutEvent;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.facebook.login.LoginManager;
import com.google.firebase.database.ChildEventListener;
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

    public DietModelImpl(EventBus eventBus, Authentication authentication, RealtimeDatabase database) {
        this.eventBus = eventBus;
        this.authentication = authentication;
        this.database = database;
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut(); //sign out from facebook
        authentication.logout();    //sign out form firebase
        eventBus.post(new LogoutEvent());
    }

    @Override
    public void stop() {
        database.removeOnDietMetaListListener();
        database.removeActiveDietListener();
    }

    @Override
    public void getDietsMeta() {

        ValueEventListener activeDietSingleListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String activeDietKey = (String) dataSnapshot.getValue();
                getDiets(activeDietKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventBus.post(new DietMetaEvent(DietMetaEvent.DIET_META_FAIL,
                        databaseError.toException().getLocalizedMessage(), null));
            }
        };

        database.setValueListener(activeDietSingleListener);
        database.getActiveDiet();
    }

    private void getDiets(final String activeDietKey) {

        ChildEventListener dietMetaListener = new ChildEventListener() {

            private String activeDiet = activeDietKey;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Diet newDiet = dataSnapshot.getValue(Diet.class);
                    if (newDiet != null) {
                        newDiet.setDietKey(dataSnapshot.getKey());
                        if (dataSnapshot.getKey().equals(activeDiet))
                            newDiet.setActive(true);
                    }
                //if get diet is success -> attach listener to active diet
                attachActiveDietListener();
                eventBus.post(new DietMetaEvent(DietMetaEvent.DIET_META_SUCCESS, null, newDiet));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventBus.post(new DietMetaEvent(DietMetaEvent.DIET_META_FAIL,
                        databaseError.toException().getLocalizedMessage(), null));
            }
        };

        database.setOnDietMetaListListener(dietMetaListener);
        database.getDietMeta();
    }

    private void attachActiveDietListener() {

        ValueEventListener activeDietListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventBus.post(new ActiveDietChange((String) dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.setActiveDietListener(activeDietListener);
    }
}

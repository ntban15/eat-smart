package com.annguyen.android.eatsmart.libs;

import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khoanguyen on 6/7/17.
 */

public class FirebaseRealtimeDatabase implements RealtimeDatabase {

    private FirebaseDatabase firebaseDatabase;
    private Authentication authentication;
    private DatabaseReference dataRef;
    private String userUID;

    public FirebaseRealtimeDatabase(FirebaseDatabase firebaseDatabase, Authentication authentication) {
        this.firebaseDatabase = firebaseDatabase;
        this.authentication = authentication;
        dataRef = firebaseDatabase.getReference();
        userUID = authentication.getUserUID();
    }

    @Override
    public void addNewDiet(Diet newDiet) {
        //get key for diet
        String dietKey = dataRef.child("diets").push().getKey();
        Map<String, Object> dietValues = newDiet.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/diets/" + dietKey, dietValues);
        childUpdates.put("/users/" + userUID + "/diets/" + dietKey, true);

        dataRef.updateChildren(childUpdates);
    }

    @Override
    public void modifyDiet(String dietKey, Diet modifiedDiet) {
        //map modified diet values
        Map<String, Object> dietValues = modifiedDiet.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/diets/" + dietKey, dietValues);

        dataRef.updateChildren(childUpdates);
    }

    @Override
    public void removeDiet(String dietKey) {
        //get data ref to diet with given key
        DatabaseReference dietRef = dataRef.child("diets").child(dietKey);
        dietRef.removeValue();

        //get data ref to diet in user diets
        dietRef = dataRef.child("users").child(userUID).child("diets").child(dietKey);
        dietRef.removeValue();
    }

    @Override
    public void setDietListener(Object listener) {

    }
}

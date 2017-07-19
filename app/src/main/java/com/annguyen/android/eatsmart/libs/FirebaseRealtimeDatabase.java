package com.annguyen.android.eatsmart.libs;

import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.base.Authentication;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khoanguyen on 6/7/17.
 */

public class FirebaseRealtimeDatabase implements RealtimeDatabase {
    //TODO: CHECK ACTIVE: OK
    //TODO: CHECK DIET CONTAIN RECIPE: OK
    //TODO: ADD RECIPE TO DB IF QUERY RETURN NULL
    //TODO: BACKEND REMOVE RECIPES IF NO DIET USES IT
    private FirebaseDatabase firebaseDatabase;
    private Authentication authentication;
    private DatabaseReference dataRef;
    private String userUID;

    private OnCompleteListener onCompleteListener;
    private ValueEventListener valueEventListener;
    private ChildEventListener dietMetaListListener;
    private ValueEventListener activeDietListener;

    public FirebaseRealtimeDatabase(FirebaseDatabase firebaseDatabase, Authentication authentication) {
        this.firebaseDatabase = firebaseDatabase;
        this.authentication = authentication;
        dataRef = firebaseDatabase.getReference();
        userUID = authentication.getUserUID();
    }

    @Override
    public void addNewDiet(Diet newDiet) {
        //put all updates into this
        Map<String, Object> updateData = new HashMap<>();

        //push values of new diet into list of diets (detail)
        String dietKey = dataRef.child("diets").push().getKey();
        updateData.put("/diets/" + dietKey, newDiet.toFullMap());

        //take some meta-data (title, diet type, excluded ingredients)
        //to push into list of diets (compact) for current user
        updateData.put("/users/" + userUID + "/diets/" + dietKey, newDiet.toMetaMap());

        //init updating data
        Task addTask = dataRef.updateChildren(updateData);
        //if client-code require observation
        if (onCompleteListener != null)
            addTask.addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void modifyDiet(String dietKey, Map<String, Object> properties) {
        Map<String, Object> dietUpdates = new HashMap<>();
        dietUpdates.put("/diets/" + dietKey, properties);

        //check if meta data is changed
        Map<String, Object> metaValues = new HashMap<>();
        if (properties.containsKey("title"))
            metaValues.put("title", properties.get("title"));
        if (properties.containsKey("dietType"))
            metaValues.put("dietType", properties.get("dietType"));
        if (properties.containsKey("excludedIngredients"))
            metaValues.put("excludedIngredients", properties.get("excludedIngredients"));
        dietUpdates.put("/users/" + userUID + "/diets/" + dietKey, metaValues);

        Task modifyTask = dataRef.updateChildren(dietUpdates);
        if (onCompleteListener != null)
            modifyTask.addOnCompleteListener(onCompleteListener);
    }

    //Remove diet require a list of its recipe keys to remove its record in recipes too
    @Override
    public void removeDiet(String dietKey, List<String> recipeKeys) {
        //put all update into map
        Map<String, Object> dietRemoval = new HashMap<>();

        //delete diet's record in recipes accordingly
        for (String recipeKey : recipeKeys) {
            dietRemoval.put("/recipe-diet/" + recipeKey + "/" + dietKey, null);
        }

        //delete the record of diet in diet-recipe
        dietRemoval.put("/diet-recipe/" + dietKey, null);

        //delete the detail record of diet
        dietRemoval.put("/diets/" + dietKey, null);

        //delete diet from diet compact list in users
        dietRemoval.put("/users/" + userUID + "/diets/" + dietKey, null);

        Task removeDietTask = dataRef.updateChildren(dietRemoval);
        if (onCompleteListener != null)
            removeDietTask.addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void setActiveDiet(String dietKey) {
        Map<String, Object> setActive = new HashMap<>();
        setActive.put("/users/" + userUID + "/activeDiet", dietKey);
        Task setActiveTask = dataRef.updateChildren(setActive);
        if (onCompleteListener != null)
            setActiveTask.addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void addRecipeToDiet(String dietKey, Recipe recipe) {
        //store all updates into map
        Map<String, Object> addRecipe = new HashMap<>();

        //use id of recipe as key
        String recipeKey = String.valueOf(recipe.getId());

        //add recipe to diet-recipe under dietKey
        addRecipe.put("/diet-recipe/" + dietKey + "/" + recipeKey, recipe.toMetaMap());

        //add given diet under recipeKey to recipe-diet
        addRecipe.put("/recipe-diet/" + recipeKey + "/" + dietKey, true);

        Task addRecipeTask = dataRef.updateChildren(addRecipe);
        if (onCompleteListener != null)
            addRecipeTask.addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void getDietDetail(String dietKey) {
        DatabaseReference dietDetail = dataRef.child("diets").child(dietKey);
        if (valueEventListener != null)
            dietDetail.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getRecipeDetail(String recipeId) {
        DatabaseReference recipeDetail = dataRef.child("recipes").child(recipeId);
        if (valueEventListener != null)
            recipeDetail.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getDietRecipeRecord(String dietKey, String recipeId) {
        DatabaseReference recipeRecord = dataRef.child("diet-recipe").child(dietKey).child(recipeId);
        if (valueEventListener != null)
            recipeRecord.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getDietRecipes(String dietKey) {
        DatabaseReference dietRecipes = dataRef.child("diet-recipe").child(dietKey);
        if (valueEventListener != null)
            dietRecipes.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getActiveDiet() {
        DatabaseReference activeDiet = dataRef.child("users").child(userUID).child("activeDiet");
        if (valueEventListener != null) {
            activeDiet.addListenerForSingleValueEvent(valueEventListener);
        }
//        if (activeDietListener != null) {
//            activeDiet.addValueEventListener(activeDietListener);
//        }
    }

    @Override
    public void removeRecipeFromDiet(String dietKey, String recipeId) {
        //store all updates into map
        Map<String, Object> removeRecipe = new HashMap<>();

        //remove recipe record in diet-recipe
        removeRecipe.put("/diet-recipe/" + dietKey + "/" + recipeId, null);

        //remove diet record under recipeId in recipe-diet
        removeRecipe.put("/recipe-diet/" + recipeId + "/" + dietKey, null);

        //init removal
        Task removeRecipeTask = dataRef.updateChildren(removeRecipe);
        if (onCompleteListener != null) {
            removeRecipeTask.addOnCompleteListener(onCompleteListener);
        }
    }

    //for getting list of diets meta info to populate UI
    @Override
    public void getDietMeta() {
        DatabaseReference dietsMetaRef = dataRef.child("users").child(userUID).child("diets");
        if (dietMetaListListener != null) {
            dietsMetaRef.addChildEventListener(dietMetaListListener);
        }
    }

    @Override
    public void setOnCompleteListener(Object onCompleteListener) {
        if (onCompleteListener instanceof OnCompleteListener)
            this.onCompleteListener = (OnCompleteListener) onCompleteListener;
    }

    @Override
    public void setValueListener(Object valueListener) {
        if (valueListener instanceof ValueEventListener)
            this.valueEventListener = (ValueEventListener) valueListener;
    }

    @Override
    public void setOnDietMetaListListener(Object childListener) {
        if (childListener instanceof ChildEventListener)
            this.dietMetaListListener = (ChildEventListener) childListener;
    }

    @Override
    public void removeOnDietMetaListListener() {
        DatabaseReference dietsMetaRef = dataRef.child("users").child(userUID).child("diets");
        if (dietMetaListListener != null)
            dietsMetaRef.removeEventListener(dietMetaListListener);
    }

    @Override
    public void setActiveDietListener(Object activeDietListener) {
        if (activeDietListener instanceof  ValueEventListener)
            this.activeDietListener = (ValueEventListener) activeDietListener;
    }

    @Override
    public void removeActiveDietListener() {
        DatabaseReference activeDiet = dataRef.child("users").child(userUID).child("activeDiet");
        if (valueEventListener != null)
            activeDiet.removeEventListener(valueEventListener);
    }
}
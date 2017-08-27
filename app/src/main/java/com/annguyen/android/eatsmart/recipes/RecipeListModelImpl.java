package com.annguyen.android.eatsmart.recipes;

import com.annguyen.android.eatsmart.api.RetrofitClient;
import com.annguyen.android.eatsmart.api.SearchComplexResponse;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.FirebaseAuthentication;
import com.annguyen.android.eatsmart.libs.FirebaseRealtimeDatabase;
import com.annguyen.android.eatsmart.libs.GreenRobotEventBus;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.annguyen.android.eatsmart.recipes.events.RecipesEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by annguyen on 25/08/2017.
 */

public class RecipeListModelImpl implements RecipeListModel {

    private EventBus eventBus;
    private RealtimeDatabase realtimeDatabase;
    private RetrofitClient retrofitClient;
    private Diet curDiet;

    public RecipeListModelImpl() {
        realtimeDatabase = new FirebaseRealtimeDatabase(FirebaseDatabase.getInstance(),
                new FirebaseAuthentication(FirebaseAuth.getInstance()));
        retrofitClient = new RetrofitClient();
        curDiet = null;
        eventBus = new GreenRobotEventBus(org.greenrobot.eventbus.EventBus.getDefault());
    }

    @Override
    public void stop() {

    }

    @Override
    public void getRecipes() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String curDietKey = dataSnapshot.getValue(String.class);
                getActiveDiet(curDietKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventBus.post(new RecipesEvent(RecipesEvent.GET_RECIPES_FAIL, null, null));
            }
        };

        realtimeDatabase.setValueListener(eventListener);
        realtimeDatabase.getActiveDiet();
    }

    @Override
    public void removeFromDiet(long id) {
        realtimeDatabase.removeRecipeFromDiet(curDiet.getDietKey(), String.valueOf(id));
    }

    @Override
    public void addToDiet(Recipe recipe) {
        realtimeDatabase.addRecipeToDiet(curDiet.getDietKey(), recipe);
    }

    @Override
    public void getNewRecipes(String query) {
        if (null != curDiet)
            getSuggestions(query);
    }

    private void getActiveDiet(final String curDietKey) {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curDiet = dataSnapshot.getValue(Diet.class);
                curDiet.setDietKey(curDietKey);
                getSuggestions(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventBus.post(new RecipesEvent(RecipesEvent.GET_RECIPES_FAIL, null, null));
            }
        };
        realtimeDatabase.setValueListener(eventListener);
        realtimeDatabase.getDietDetail(curDietKey);
    }

    private void getSuggestions(final String query) {
        retrofitClient.getSearchComplexService().searchComplex(
                true,
                "",
                curDiet.getDietType(),
                curDiet.getExcludedIngredients(),
                false,
                "",
                false,
                "",
                false,
                curDiet.getMaxCalories(),
                curDiet.getMaxCarbs(),
                curDiet.getMaxFat(),
                curDiet.getMaxProtein(),
                curDiet.getMinCalories(),
                curDiet.getMinCarbs(),
                curDiet.getMinFat(),
                curDiet.getMinProtein(),
                10,
                0,
                (null != query ? query : ""),
                2,
                "").enqueue(new Callback<SearchComplexResponse>() {
            @Override
            public void onResponse(Call<SearchComplexResponse> call, Response<SearchComplexResponse> response) {
                List<Recipe> recipeList = null;
                if (null != response.body()) {
                    recipeList = new ArrayList<Recipe>();
                    recipeList.addAll(response.body().getResults());
                    if (null == query)
                        eventBus.post(new RecipesEvent(RecipesEvent.GET_RECIPES_COMPLETE, recipeList, curDiet));
                    else
                        eventBus.post(new RecipesEvent(RecipesEvent.GET_NEW_RECIPES_COMPLETE, recipeList, null));
                }
                else {
                    eventBus.post(new RecipesEvent(RecipesEvent.GET_RECIPES_FAIL, null, null));
                }
            }

            @Override
            public void onFailure(Call<SearchComplexResponse> call, Throwable t) {
                eventBus.post(new RecipesEvent(RecipesEvent.GET_RECIPES_FAIL, null, null));
            }
        });
    }
}

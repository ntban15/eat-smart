package com.annguyen.android.eatsmart.api;

import com.annguyen.android.eatsmart.entities.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by annguyen on 11/08/2017.
 */

public interface RecipeInformationService {
    @GET("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/{id}/information")
    Call<Recipe> getInfo(@Path("id") String id,
                         @Query("includeNutrition") boolean includeNutrition);
}

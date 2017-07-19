package com.annguyen.android.eatsmart.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by annguyen on 6/4/2017.
 */

public interface SearchComplexService {
    @GET("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex")
    Call<SearchComplexResponse> searchComplex(@Query("addRecipeInformation") boolean recipeInfo,
                                              @Query("cuisine") String cuisine,
                                              @Query("diet") String diet,
                                              @Query("excludeIngredients") String exclude,
                                              @Query("fillIngredients") boolean fillIngredients,
                                              @Query("includeIngredients") String include,
                                              @Query("instructionsRequired") boolean insRequired,
                                              @Query("intolerances") String intolerances,
                                              @Query("limitLicense") boolean license,
                                              @Query("maxCalories") int maxCalories,
                                              @Query("maxCarbs") int maxCarbs,
                                              @Query("maxFat") int maxFat,
                                              @Query("maxProtein") int maxProtein,
                                              @Query("minCalories") int minCalories,
                                              @Query("minCarbs") int minCarbs,
                                              @Query("minFat") int minFat,
                                              @Query("minProtein") int minProtein,
                                              @Query("number") int recipeCount,
                                              @Query("offset") int offset,
                                              @Query("query") String searchQuery,
                                              @Query("ranking") int sortStrategy,
                                              @Query("type") String type);
}

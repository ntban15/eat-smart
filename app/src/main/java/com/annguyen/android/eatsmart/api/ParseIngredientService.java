package com.annguyen.android.eatsmart.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by khoanguyen on 8/19/17.
 */

public interface ParseIngredientService {
    @FormUrlEncoded
    @POST("parseIngredients")
    Call<List<ParseIngredientResponse>> parseIngredient(@Query("includeNutrition") boolean includeNutrition,
                                                        @Field("ingredientList") String ingredientList,
                                                        @Field("servings") int serving);
}

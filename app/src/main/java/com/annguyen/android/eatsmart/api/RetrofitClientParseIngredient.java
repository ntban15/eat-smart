package com.annguyen.android.eatsmart.api;

import com.annguyen.android.eatsmart.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by khoanguyen on 8/19/17.
 */

public class RetrofitClientParseIngredient {
    private static final String MASHAPE_BASE_URL =
            "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";

    private Retrofit client;

    public RetrofitClientParseIngredient() {

        //build Interceptor to inject Header into request call
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//
//                HttpUrl originalUrl = originalRequest.url();
//
//                HttpUrl newUrl = originalUrl.newBuilder()
//                        .addQueryParameter("includeNutrition", "false")
//                        .build();
//
//                Request newRequest = originalRequest.newBuilder()
//                        .url(newUrl)
//                        .addHeader("X-Mashape-Key", BuildConfig.MASHAPE_KEY)
//                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                        .addHeader("Accept", "application/json")//receive JSON-formatted response
//                        .build();
//
//                return chain.proceed(newRequest);

                Request originalRequest = chain.request();

                //create a new Request with header from original one
                Request newRequest = originalRequest.newBuilder()
                        .addHeader("X-Mashape-Key", BuildConfig.MASHAPE_KEY)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Accept", "application/json")//receive JSON-formatted response
                        .build();

                return chain.proceed(newRequest);
            }
        };

        //build new OkHttpClient to plug it into Retrofit
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        //add OkHttpClient into retrofit
        this.client = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(MASHAPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //use GSON converter to convert JSON -> POJO
                .build();
    }

    //get searchComplexService
    public ParseIngredientService getParseIngredientService() {
        return client.create(ParseIngredientService.class);
    }
}

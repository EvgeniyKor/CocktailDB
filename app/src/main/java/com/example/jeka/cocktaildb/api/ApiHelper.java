package com.example.jeka.cocktaildb.api;

import android.util.Log;

import com.example.jeka.cocktaildb.data.Drinks;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiHelper {

    private String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/";
    private ICocktailAPI service;

    public ApiHelper() {
        init();
    }

    private void init(){
        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        service = client.create(ICocktailAPI.class);
    }


    public void requestCocktailsByCategory(final String category, final CallbackApiHelper callback){
        Call<Drinks> call = service.getByCategory(category);
        call.enqueue(new Callback<Drinks>() {
            @Override
            public void onResponse(Call<Drinks> call, Response<Drinks> response) {
                if (response.isSuccessful()){
                    callback.onComplete(response.body(), category);
                } else {
                    Log.e("Retrofit", "response not Successful");
                }
            }

            @Override
            public void onFailure(Call<Drinks> call, Throwable t) {
                Log.e("Retrofit", "onFailure");
                callback.onFailure();
                t.printStackTrace();
            }
        });
    }


    public interface CallbackApiHelper{
        void onComplete(Drinks drinks, String cat);
        void onFailure();
    }

}

package com.example.jeka.cocktaildb.api;

import com.example.jeka.cocktaildb.data.Drinks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICocktailAPI {

    @GET("1/filter.php")
    Call<Drinks> getByIngredient(@Query("i") String name);

    @GET("1/filter.php")
    Call<Drinks> getByAlcoholic(@Query("a") String name);

    @GET("1/filter.php")
    Call<Drinks> getByCategory(@Query("c") String name);

    @GET("1/filter.php")
    Call<Drinks> getByGlass(@Query("g") String name);


}

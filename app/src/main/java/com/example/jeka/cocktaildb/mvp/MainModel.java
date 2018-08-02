package com.example.jeka.cocktaildb.mvp;

import com.example.jeka.cocktaildb.api.ApiHelper;
import com.example.jeka.cocktaildb.data.Drink;

import java.util.ArrayList;
import java.util.List;

public class MainModel{

    private List<Drink> drinks = new ArrayList<>();
    private ApiHelper apiHelper;

    public MainModel() {
        init();

    }

    private void init(){
        apiHelper = new ApiHelper();
    }


    public void request(String category, ApiHelper.CallbackApiHelper callback){
        apiHelper.requestCocktailsByCategory(category , callback);
    }


    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }
}

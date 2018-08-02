package com.example.jeka.cocktaildb.data;

import com.example.jeka.cocktaildb.data.Drink;
import com.squareup.moshi.Json;

import java.util.List;

public class Drinks {
    @Json(name = "drinks")
    private List<Drink> drinks = null;

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }
}

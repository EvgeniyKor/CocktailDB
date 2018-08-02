package com.example.jeka.cocktaildb.data;

import com.squareup.moshi.Json;

public class Drink extends ListItem{
    @Json(name = "strDrink")
    private String strDrink;
    @Json(name = "strDrinkThumb")
    private String strDrinkThumb;
    @Json(name = "idDrink")
    private String idDrink;

    @Override
    public String getStrName() {
        return this.strDrink;
    }

    @Override
    public void setStrName(String strName) {
        this.strDrink = strName;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public void setStrDrinkThumb(String strDrinkThumb) {
        this.strDrinkThumb = strDrinkThumb;
    }

    public String getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(String idDrink) {
        this.idDrink = idDrink;
    }
}

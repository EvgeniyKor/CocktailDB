package com.example.jeka.cocktaildb.mvp;

import com.example.jeka.cocktaildb.api.ApiHelper;
import com.example.jeka.cocktaildb.data.Drinks;
import com.example.jeka.cocktaildb.data.Header;
import com.example.jeka.cocktaildb.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter {
    private MainActivity mainActivity;
    private List<ListItem> items = new ArrayList<>();
    private List<String> filters;
    private MainModel model;
    private int countHeader = 0;

    private int NUMBER_ON_PAGE = 20;

    public MainPresenter(MainModel model, List<String> filters) {
        this.model = model;
        this.filters = filters;
    }

    public void attachView(MainActivity activity){
        this.mainActivity = activity;
    }

    public void detachView(){
        this.mainActivity = null;
    }


    public void viewIsReady() {
        model.request(filters.get(0), new ApiHelper.CallbackApiHelper() {
            @Override
            public void onComplete(Drinks drinks, String cat) {
                Header header = new Header();
                header.setHeader(cat);
                countHeader++;
                items.add(header);
                items.addAll(drinks.getDrinks());
                if (items.size() < NUMBER_ON_PAGE) {
                    mainActivity.showDrinks(items.subList(0, items.size()));
                } else mainActivity.showDrinks(items.subList(0, NUMBER_ON_PAGE + countHeader));
            }

            @Override
            public void onFailure() {
                mainActivity.setLoading(false);
            }
        });
    }

    public void endOfList(final int count) {
        final int sum = count + NUMBER_ON_PAGE + countHeader;
        if (items.size() > sum)
            mainActivity.showNewDrinks(items.subList(count, sum));
        else  if ((items.size() < sum) && (filters.size() == (countHeader )))
            return;
        else model.request(filters.get(countHeader), new ApiHelper.CallbackApiHelper() {
            @Override
            public void onComplete(Drinks drinks, String cat) {
                Header header = new Header();
                header.setHeader(cat);
                countHeader++;
                items.add(header);
                items.addAll(drinks.getDrinks());
                if ((items.size() < sum) && (filters.size() == (countHeader - 2)))
                    return;
                if (items.size() < sum)
                    mainActivity.showNewDrinks(items.subList(count, items.size()));
                else mainActivity.showNewDrinks(items.subList(count, sum));
            }

                @Override
                public void onFailure() {
                    mainActivity.setLoading(false);
                }
            });
    }

    public void changedFilters(List<String> filters) {
        this.filters = filters;
        refreshedData();
    }

    public void refreshedData() {
        items.clear();
        countHeader = 0;
        viewIsReady();
    }
}

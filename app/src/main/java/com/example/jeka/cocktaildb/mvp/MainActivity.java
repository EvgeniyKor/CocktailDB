package com.example.jeka.cocktaildb.mvp;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jeka.cocktaildb.fragment.FilterFragment;
import com.example.jeka.cocktaildb.fragment.ICallbackFilterFragment;
import com.example.jeka.cocktaildb.fragment.ICallbackMainFragment;
import com.example.jeka.cocktaildb.fragment.MainFragment;
import com.example.jeka.cocktaildb.R;
import com.example.jeka.cocktaildb.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ICallbackMainFragment, ICallbackFilterFragment {

    private MainPresenter presenter;
    private MainFragment mainFragment;
    private FilterFragment filterFragment;
    private FragmentTransaction fTrans;
    private MenuItem filterMenu;
    private Toolbar toolbar;
    private List<String> mFilterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        initFilterList();

        MainModel model = new MainModel();
        presenter = new MainPresenter(model, mFilterList);
        presenter.attachView(this);

        mainFragment = new MainFragment();
        mainFragment.setCallbackMainFragment(this);
        filterFragment = new FilterFragment();
        filterFragment.setFilterList(mFilterList);
        filterFragment.setCallback(this);

        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.container, mainFragment, "fragmentMain");
        fTrans.commit();

        presenter.viewIsReady();
    }

    public void showDrinks(List<ListItem> items){
        mainFragment.setDrinks(items);
    }

    public void showNewDrinks(List<ListItem> items){
        mainFragment.addDrinks(items);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        filterMenu = menu.findItem(R.id.menu_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_filter){
            displayFilterFragment();
        }
        if (id == android.R.id.home){
            displayMainFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mainFragment.isHidden())
            displayMainFragment();
        else super.onBackPressed();
    }

    private void displayMainFragment() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        filterMenu.setVisible(true);
        toolbar.setTitle(R.string.drinks);
        fTrans = getFragmentManager().beginTransaction();
        fTrans.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_right);
        if (mainFragment.isAdded()) {
            fTrans.show(mainFragment);
        } else {
            fTrans.add(R.id.container, mainFragment, "fragmentMain");
        }

        if (mainFragment.isAdded()) { fTrans.hide(filterFragment); }

        fTrans.commit();
    }

    protected void displayFilterFragment() {
        filterMenu.setVisible(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle(R.string.str_filters);
        fTrans = getFragmentManager().beginTransaction();
        fTrans.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left);
        if (filterFragment.isAdded()) {
            fTrans.show(filterFragment);
        } else {
            fTrans.add(R.id.container, filterFragment, "fragmentFilter");
        }
        if (mainFragment.isAdded()) { fTrans.hide(mainFragment); }

        fTrans.commit();
    }


    private void initFilterList() {
        mFilterList.clear();
        mFilterList.add("Ordinary Drink");
        mFilterList.add("Cocktail");
        mFilterList.add("Milk / Float / Shake");
        mFilterList.add("Other/Unknown");
        mFilterList.add("Cocoa");
        mFilterList.add("Shot");
        mFilterList.add("Coffee / Tea");
        mFilterList.add("Homemade Liqueur");
        mFilterList.add("Punch / Party Drink");
        mFilterList.add("Beer");
        mFilterList.add("Soft Drink / Soda");
    }

    private Drawable buildCounterDrawable(int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.view_menu_icon_badge, null);
        view.setBackgroundResource(backgroundImageId);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void endOfList(int count) {
        presenter.endOfList(count);
    }

    @Override
    public void onRefresh() {
        presenter.refreshedData();
    }

    @Override
    public void onFiltersChanged(List<String> filters) {
        displayMainFragment();
        if (filters.isEmpty()){
            filterMenu.setIcon(R.drawable.filter);
            initFilterList();
            presenter.changedFilters(mFilterList);
            return;
        }
        if (!filters.equals(mFilterList)){
            filterMenu.setIcon(buildCounterDrawable(R.drawable.filter));
            this.mFilterList = filters;
            presenter.changedFilters(filters);
        }
    }
}

package com.example.jeka.cocktaildb.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeka.cocktaildb.R;
import com.example.jeka.cocktaildb.data.ListItem;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements MyAdapter.IMyAdapterCallback {


    private ICallbackMainFragment callbackMainFragment;

    public SwipeRefreshLayout swiper;
    private MyAdapter adapter;
    private boolean isLoading = false;



    public void setDrinks(List<ListItem> drinks) {
        adapter.setDrinks(drinks);
        isLoading = false;
        swiper.setRefreshing(false);
    }

    public void addDrinks(List<ListItem> drinks){
        adapter.addDrinks(drinks);
        isLoading = false;
    }



    public MainFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        swiper = view.findViewById(R.id.swiper);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        adapter = new MyAdapter(getActivity(), swiper);
        adapter.setIMyAdapterCallback(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(scrollListener);
        return view;
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int firstVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager())
                    .findFirstVisibleItemPosition();
            if (!isLoading)
                if ((visibleItemCount+firstVisibleItems) >= totalItemCount){
                    isLoading = true;
                    if (callbackMainFragment != null){
                        callbackMainFragment.endOfList(totalItemCount);
                    }
                }
        }
    };

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onRefresh() {
        callbackMainFragment.onRefresh();
    }

    public void setCallbackMainFragment(ICallbackMainFragment callbackMainFragment) {
        this.callbackMainFragment = callbackMainFragment;
    }

}

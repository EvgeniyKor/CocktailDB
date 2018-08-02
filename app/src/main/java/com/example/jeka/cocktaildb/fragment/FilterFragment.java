package com.example.jeka.cocktaildb.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.jeka.cocktaildb.R;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {


    private ICallbackFilterFragment callback;

    private List<String> mFilterList = new ArrayList<>();

    public void setFilterList(List<String> mFilterList) {
        this.mFilterList = mFilterList;
    }

    private MyArrayAdapter mArrayAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment, container, false);
        // ButterKnife.bind(this, view);

        ListView listView = view.findViewById(R.id.listView);
        mArrayAdapter = new MyArrayAdapter(getActivity(), R.layout.filter_item, mFilterList);
        Button btmApply = view.findViewById(R.id.btmApply);
        btmApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){
                    callback.onFiltersChanged(mArrayAdapter.getCheckedItems());
                    mArrayAdapter.resetChecked(mFilterList.size());
                }
            }
        });

        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mArrayAdapter.toggleChecked(position);
            }
        });

        return view;
    }

    public void setCallback(ICallbackFilterFragment callback) {
        this.callback = callback;
    }


    private class MyArrayAdapter extends ArrayAdapter<String>{

        private SparseBooleanArray mCheckedMap = new SparseBooleanArray();

        public MyArrayAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);

            resetChecked(objects.size());
        }

        void resetChecked(int size){
            for (int i = 0; i < size; i++) {
                mCheckedMap.put(i, false);
            }
            notifyDataSetChanged();
        }

        void toggleChecked(int position) {
            if (mCheckedMap.get(position)) {
                mCheckedMap.put(position, false);
            } else {
                mCheckedMap.put(position, true);
            }
            notifyDataSetChanged();
        }

        public List<Integer> getCheckedItemPositions() {
            List<Integer> checkedItemPositions = new ArrayList<>();

            for (int i = 0; i < mCheckedMap.size(); i++) {
                if (mCheckedMap.get(i)) {
                    (checkedItemPositions).add(i);
                }
            }
            return checkedItemPositions;
        }

        List<String> getCheckedItems() {
            List<String> checkedItems = new ArrayList<>();

            for (int i = 0; i < mCheckedMap.size(); i++) {
                if (mCheckedMap.get(i)) {
                    (checkedItems).add(mFilterList.get(i));
                }
            }
            return checkedItems;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.filter_item, parent, false);
            }

            CheckedTextView checkedTextView = (CheckedTextView) convertView
                    .findViewById(R.id.checkedTxt);
            checkedTextView.setText(mFilterList.get(position));

            Boolean checked = mCheckedMap.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
                if (checked){
                    checkedTextView.setCheckMarkDrawable(R.drawable.check);
                } else {
                    checkedTextView.setCheckMarkDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }
            return convertView;
        }
    }


}

package com.example.jeka.cocktaildb.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeka.cocktaildb.R;
import com.example.jeka.cocktaildb.data.Drink;
import com.example.jeka.cocktaildb.data.Header;
import com.example.jeka.cocktaildb.data.ListItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context c;
    private List<ListItem> items = new ArrayList<>();
    private SwipeRefreshLayout swiper;
    private IMyAdapterCallback IMyAdapterCallback;

    public void setDrinks(List<ListItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addDrinks(List<ListItem> items){
        List<ListItem> temp = new ArrayList<>(this.items);
        temp.addAll(items);
        this.items = temp;
        notifyDataSetChanged();
    }

    public MyAdapter(Context c, SwipeRefreshLayout swiper) {
        this.c = c;
        this.swiper = swiper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header, parent, false);
            return new VHHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            return new VHItem(v);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHHeader){
            Header currentItem = (Header) items.get(position);
            VHHeader holderHeader = (VHHeader)holder;
            holderHeader.txtHeader.setText(currentItem.getHeader());
        } else if (holder instanceof VHItem){
            Drink currentItem = (Drink) items.get(position);
            VHItem holderItem = (VHItem) holder;
            holderItem.tvDrink.setText(currentItem.getStrName());
            Picasso picasso = Picasso.get();
            picasso.load(((Drink) items.get(position)).getStrDrinkThumb())
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into(holderItem.imgDrink);
        }
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        IMyAdapterCallback.onRefresh();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return items.get(position) instanceof Header;

    }

    class VHHeader extends RecyclerView.ViewHolder{
        TextView txtHeader;

        public VHHeader(View itemView){
            super(itemView);
            this.txtHeader = (TextView) itemView.findViewById(R.id.txtHeader);
        }
    }

    class VHItem extends RecyclerView.ViewHolder{

        ImageView imgDrink;
        TextView tvDrink;

        public VHItem(View itemView) {
            super(itemView);
            imgDrink = itemView.findViewById(R.id.imgDrink);
            tvDrink = itemView.findViewById(R.id.tvDrink);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (IMyAdapterCallback != null)
                        IMyAdapterCallback.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    void setIMyAdapterCallback(IMyAdapterCallback IMyAdapterCallback){
        this.IMyAdapterCallback = IMyAdapterCallback;
    }

    public interface IMyAdapterCallback {
        void onItemClick(View view, int position);
        void onRefresh();
    }
}

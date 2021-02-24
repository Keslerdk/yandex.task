package com.example.yandextask;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CardItem> mCardList;

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
    Activity activity;
    int visibleThreshold = 5;
    int lastVisibleItem;
    int totalItemCount;

//    public MyAdapter(ArrayList<CardItem> mCardList) {
//        this.mCardList = mCardList;
//    }

    public MyAdapter(RecyclerView recyclerView, Activity activity, ArrayList<CardItem> items) {
        this.activity = activity;
        this.mCardList = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mCardList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            MyViewHolder ehv = new MyViewHolder(v);
            return ehv;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            CardItem currentPos = mCardList.get(position);
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.ticker.setText(currentPos.getmTicker());
            viewHolder.fullName.setText(currentPos.getmFullName());
            viewHolder.currentPrice.setText(currentPos.getmCurrentPrice());
            viewHolder.deltaPrice.setText(currentPos.getmDeltaPrice());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingHolder = (LoadingViewHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }
    }

//     @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        CardItem currentPos = mCardList.get(position);
//
////        holder.icon.setImageResource(currentPos.getmIcon());
//        holder.ticker.setText(currentPos.getmTicker());
//        holder.fullName.setText(currentPos.getmFullName());
//        holder.currentPrice.setText(currentPos.getmCurrentPrice());
//        holder.deltaPrice.setText(currentPos.getmDeltaPrice());
//    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int layoutPosition = holder.getLayoutPosition();
        Log.d(TAG, "onViewAttachedToWindow: getayoutPosition = " + layoutPosition);

        layoutPosition = holder.getAdapterPosition();
        Log.d(TAG, "onViewAttachedToWindow: getAdapterPosition = " + layoutPosition);

    }


}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView ticker;
    TextView fullName;
    TextView currentPrice;
    TextView deltaPrice;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.icon);
        ticker = itemView.findViewById(R.id.ticker);
        fullName = itemView.findViewById(R.id.fullName);
        currentPrice = itemView.findViewById(R.id.currentPrice);
        deltaPrice = itemView.findViewById(R.id.currentPrice);
    }
}

class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}

package com.raven.trcrypto.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raven.trcrypto.Interface.ILoadMore;
import com.raven.trcrypto.Model.CoinModel;
import com.raven.trcrypto.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ILoadMore iLoadMore;
    boolean isLoading;
    Activity activity;
    List<CoinModel> items;
    int visibleThreshold = 5, lastVisibleItem, totalItemCount;
    //   private AdapterView.OnItemClickListener mListener;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
//


    public CoinAdapter(RecyclerView recyclerView, Activity activity, List<CoinModel> items) {
        this.activity = activity;
        this.items = items;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (iLoadMore != null)
                        iLoadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    public void setiLoadMore(ILoadMore iLoadMore) {
        this.iLoadMore = iLoadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.coin_layout, parent, false);
        CoinViewHolder evh=new CoinViewHolder(view,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CoinModel item = items.get(position);
        CoinViewHolder holderItem = (CoinViewHolder) holder;
        holderItem.coin_name.setText(item.getName());
        holderItem.coin_symbol.setText(item.getSymbol());
        holderItem.coin_price.setText(item.getCurrent_price());
        holderItem.one_hour_change.setText(getDecimalFormat(item.getPrice_change_percentage_1h_in_currency())+"%");
        holderItem.twenty_hours_change.setText(getDecimalFormat(item.getPrice_change_percentage_24h_in_currency())+"%");
        holderItem.seven_days_change.setText(getDecimalFormat(item.getPrice_change_percentage_7d_in_currency())+"%");
        Picasso.with(activity).load(item.getImage()).into(holderItem.coin_icon);

        holderItem.one_hour_change.setTextColor(item.getPrice_change_percentage_1h_in_currency().contains("-") ?
                Color.parseColor("#FF0000") : Color.parseColor("#32CD32"));
        holderItem.twenty_hours_change.setTextColor(item.getPrice_change_percentage_24h_in_currency().contains("-") ?
                Color.parseColor("#FF0000") : Color.parseColor("#32CD32"));
        holderItem.seven_days_change.setTextColor(item.getPrice_change_percentage_7d_in_currency().contains("-") ?
                Color.parseColor("#FF0000") : Color.parseColor("#32CD32"));
//        holder.itemView.setOnClickListener(mListener(item));




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {
        isLoading = true;
    }

    public void updateData(List<CoinModel> coinModels) {
        this.items = coinModels;
        notifyDataSetChanged();
    }

    private static String getDecimalFormat(String value) {
        String getValue = String.valueOf(value).split("[.]")[1];
        if (getValue.length() == 1) {
            return String.valueOf(value).split("[.]")[0] +
                    "." + getValue.substring(0, 1) +
                    String.format("%0" + 1 + "d", 0);
        } else {
            return String.valueOf(value).split("[.]")[0]
                    + "." + getValue.substring(0, 2);
        }

    }

}
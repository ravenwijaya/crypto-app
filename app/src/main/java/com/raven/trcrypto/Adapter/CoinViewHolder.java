package com.raven.trcrypto.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raven.trcrypto.R;

public class CoinViewHolder extends RecyclerView.ViewHolder {
    public ImageView coin_icon;
    public TextView coin_symbol,coin_name,coin_price,one_hour_change,twenty_hours_change,seven_days_change;

    public CoinViewHolder(@NonNull View itemView) {
        super(itemView);
        coin_name=(TextView)itemView.findViewById(R.id.coinName);
        coin_symbol=(TextView)itemView.findViewById(R.id.coinSymbol);
        coin_icon=(ImageView)itemView.findViewById(R.id.coinIcon);
        coin_price=(TextView) itemView.findViewById(R.id.priceUsd);
        one_hour_change=(TextView) itemView.findViewById(R.id.oneHour);
        twenty_hours_change=(TextView) itemView.findViewById(R.id.twentyFourHour);
        seven_days_change=(TextView) itemView.findViewById(R.id.sevenDay);
    }
}

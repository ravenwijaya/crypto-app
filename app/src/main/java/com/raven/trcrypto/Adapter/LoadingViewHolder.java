package com.raven.trcrypto.Adapter;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raven.trcrypto.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;


    public LoadingViewHolder( View itemView) {
        super(itemView);
        progressBar=(ProgressBar)itemView.findViewById(R.id.progress_bar);
    }
}

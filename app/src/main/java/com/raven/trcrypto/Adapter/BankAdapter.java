package com.raven.trcrypto.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raven.trcrypto.AddBankDialog;
import com.raven.trcrypto.Model.bank_item;
import com.raven.trcrypto.R;

import java.util.ArrayList;
import java.util.List;

public class BankAdapter extends ArrayAdapter<bank_item> {
    public BankAdapter(Context context,ArrayList<bank_item> list_bank){
            super(context,0,list_bank);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return init_view(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return init_view(position, convertView, parent);
    }

    private View init_view(int position, View convertView,ViewGroup parent){
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_bank,parent,false
            );
        }
        ImageView logo = convertView.findViewById(R.id.logo_bank);
        TextView nama = convertView.findViewById(R.id.n_bank);
        bank_item bank_sekarang = getItem(position);
        if(bank_sekarang!=null) {
            logo.setImageResource(bank_sekarang.getLogo_bank());
            nama.setText(bank_sekarang.getNama_bank());
        }
        return convertView;
    }
}

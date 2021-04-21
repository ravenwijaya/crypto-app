package com.raven.trcrypto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ActionBar mActionBar;

    public ImageView coin_icon;
    public EditText amount;
    public TextView coin_name,coin_price,one_hour_change,twenty_hours_change,seven_days_change,priceorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        coin_icon=findViewById(R.id.iconbig);
        amount=findViewById(R.id.amount);
        coin_name=findViewById(R.id.coinnamee);
        coin_price=findViewById(R.id.currentprice);
        one_hour_change=findViewById(R.id.oneHour);
        twenty_hours_change=findViewById(R.id.twentyFourHour);
        seven_days_change=findViewById(R.id.sevenDay);
        priceorder=findViewById(R.id.priceorder);
        Spinner spinner=findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.ordertype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text=parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
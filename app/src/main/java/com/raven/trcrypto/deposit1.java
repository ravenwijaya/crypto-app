package com.raven.trcrypto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class deposit1 extends AppCompatActivity {
    ActionBar mActionBar;
    private Button go_to_depo2;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit1);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        go_to_depo2 = findViewById(R.id.btn_bca);
        Intent intent=getIntent();
        userID=intent.getStringExtra("uid");

        go_to_depo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(deposit1.this,deposit2.class);
                myIntent.putExtra("uid",userID);
                startActivity(myIntent);
            }
        });
    }
}
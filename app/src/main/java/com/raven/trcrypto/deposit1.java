package com.raven.trcrypto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class deposit1 extends AppCompatActivity {
    private Button go_to_depo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit1);

        go_to_depo2 = findViewById(R.id.btn_bca);

        go_to_depo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(deposit1.this, deposit2.class));
            }
        });
    }
}
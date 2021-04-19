package com.raven.trcrypto;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    //Variable Animasi
    Animation topAnim, bottomAnim;
    ImageView logo;
    TextView nameapp;

    //waktu spalshscreen 1000 = 1 detik

    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);
        //animasi
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //animasi
        logo = findViewById(R.id.logo);
        nameapp = findViewById(R.id.nameapp);
        logo.setAnimation(topAnim);
        nameapp.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, SigninActivity.class);
                    startActivity(intent);
                    finish();

            }
        }, SPLASH_SCREEN);
    }
}

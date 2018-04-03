package com.example.ashishaggarwal.notely;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((GradientDrawable) findViewById(R.id.splash_circle_green).getBackground()).setColor(Color.GREEN);
        ((GradientDrawable) findViewById(R.id.splash_circle_yellow).getBackground()).setColor(getResources().getColor(R.color.darkYellow));
        ((GradientDrawable) findViewById(R.id.splash_circle_red).getBackground()).setColor(Color.RED);
    }
}

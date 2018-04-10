package com.example.ashishaggarwal.notely.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.ashishaggarwal.notely.R;

public class SplashActivity extends AppCompatActivity {


    private Handler handler = new Handler();

    private Runnable launchRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, NotesActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((GradientDrawable) findViewById(R.id.splash_circle_green).getBackground()).setColor(Color.GREEN);
        ((GradientDrawable) findViewById(R.id.splash_circle_yellow).getBackground()).setColor(getResources().getColor(R.color.darkYellow));
        ((GradientDrawable) findViewById(R.id.splash_circle_red).getBackground()).setColor(Color.RED);
        handler.postDelayed(launchRunnable, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        handler.removeCallbacks(launchRunnable);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

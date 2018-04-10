package com.example.ashishaggarwal.notely.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.ashishaggarwal.notely.R;

public class SplashActivity extends AppCompatActivity {

    private boolean stopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((GradientDrawable) findViewById(R.id.splash_circle_green).getBackground()).setColor(Color.GREEN);
        ((GradientDrawable) findViewById(R.id.splash_circle_yellow).getBackground()).setColor(getResources().getColor(R.color.darkYellow));
        ((GradientDrawable) findViewById(R.id.splash_circle_red).getBackground()).setColor(Color.RED);
        launchNotesActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopped = false;
    }

    private void launchNotesActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (stopped)
                    return;
                Intent intent = new Intent(SplashActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        stopped = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.company.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    ImageView imageViewSplash;
    TextView textViewSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageViewSplash = findViewById(R.id.imageViewSplash);
        textViewSplash = findViewById(R.id.textViewSplash);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        textViewSplash.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash_Screen.this, LogInActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);
    }
}
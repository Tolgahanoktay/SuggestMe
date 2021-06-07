package com.tolgahanoktay.suggestme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class StarterActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    ImageView imageView,imageView2;
    Animation animationRight,animationLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starter);

        animationRight = AnimationUtils.loadAnimation(this,R.anim.right_animation);
        animationLeft = AnimationUtils.loadAnimation(this,R.anim.left_animation);

        imageView = findViewById(R.id.imageView_logo);
        imageView2 = findViewById(R.id.imageView_logo2);

        imageView.setAnimation(animationLeft);
        imageView2.setAnimation(animationRight);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
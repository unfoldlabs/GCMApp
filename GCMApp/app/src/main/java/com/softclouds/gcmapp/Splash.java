package com.softclouds.gcmapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Splash extends Activity {
    private int TIME_INTERVEL = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Resources res = this.getResources();
        TransitionDrawable transition = (TransitionDrawable)
                res.getDrawable(R.drawable.expand_colapse);
        ImageView image = (ImageView) findViewById(R.id.imageViewTransitionImage);
        image.setImageDrawable(transition);

        transition.startTransition(3000);

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(TIME_INTERVEL);
                    Intent intent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        };
        background.start();
    }
}

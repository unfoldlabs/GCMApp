package com.softclouds.gcmapp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutUS extends Activity {

    TransitionDrawable transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //*********************************************************************************
        Resources res = this.getResources();
        transition = (TransitionDrawable)
                res.getDrawable(R.drawable.skip_transition);
        ImageView image = (ImageView) findViewById(R.id.skip);
        image.setImageDrawable(transition);

        repeat();
        //*********************************************************************************

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void repeat(){
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
                transition.startTransition(3000);
            }

            public void onFinish() {
                transition.resetTransition();
                repeat();
            }
        }.start();
    }

    public void skipAction(View v){
        finish();
    }

}

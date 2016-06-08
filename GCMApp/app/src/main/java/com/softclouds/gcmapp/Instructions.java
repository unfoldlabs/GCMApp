package com.softclouds.gcmapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.TransitionDrawable;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.softclouds.gcmapp.Fragments.FragmentA;
import com.softclouds.gcmapp.Fragments.FragmentB;
import com.softclouds.gcmapp.Fragments.FragmentC;

public class Instructions extends FragmentActivity {

    ViewPager viewPager;
    ImageView buttonOne;
    ImageView buttonTwo;
    ImageView buttonThree;
    ImageView imgViewSkip;
    TransitionDrawable transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        ///////////////////////////////////////////////////////////////////////////////////
        // WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        //Display display = wm.getDefaultDisplay();

        //Point size = new Point();
        //display.getSize(size);
        //int width = size.x;
        //int height = size.y;

        imgViewSkip = (ImageView)this.findViewById(R.id.skip);

        //Bitmap bitmapImage = BitmapFactory.decodeFile(getActivity().getResources().getResourceName(R.mipmap.imagestutorialg));
        //Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.mipmap.skip);
        // int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
        //Bitmap scaledImage = Bitmap.createScaledBitmap(bitmapImage,(int)(width * .18),(int)((width * .18) * .69), false);
        //*********************************************************************************
        Resources res = this.getResources();
        transition = (TransitionDrawable)
                res.getDrawable(R.drawable.skip_transition);
        ImageView image = (ImageView) findViewById(R.id.skip);
        image.setImageDrawable(transition);

        repeat();
        //*********************************************************************************
        //imgViewSkip.setImageBitmap(scaledImage);
        ///////////////////////////////////////////////////////////////////////////////////

        viewPager =(ViewPager)findViewById(R.id.pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new MenuAdapter(fragmentManager));

        buttonOne = (ImageView)findViewById(R.id.imageViewButton1);
        buttonTwo = (ImageView)findViewById(R.id.imageViewButton2);
        buttonThree = (ImageView)findViewById(R.id.imageViewButton3);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if(position == 0){
                    buttonOne.setImageResource(R.mipmap.full);
                    buttonTwo.setImageResource(R.mipmap.empty);
                    buttonThree.setImageResource(R.mipmap.empty);
                    System.out.println("One");
                }
                else  if(position == 1){
                    buttonOne.setImageResource(R.mipmap.empty);
                    buttonTwo.setImageResource(R.mipmap.full);
                    buttonThree.setImageResource(R.mipmap.empty);
                    System.out.println("Two");
                }
                else  if(position == 2){
                    buttonOne.setImageResource(R.mipmap.empty);
                    buttonTwo.setImageResource(R.mipmap.empty);
                    buttonThree.setImageResource(R.mipmap.full);
                    System.out.println("Three");
                }
            }
        });
    }

    class MenuAdapter extends FragmentPagerAdapter{

        public MenuAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            if(position == 0){
                fragment = new FragmentA();
            }
            else  if(position == 1){
                fragment = new FragmentB();
            }
            else  if(position == 2){
                fragment = new FragmentC();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    //GPSTPYEYBSTN
}




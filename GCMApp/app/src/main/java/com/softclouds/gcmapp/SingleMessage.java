package com.softclouds.gcmapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class SingleMessage extends Activity {
    ImageButton imageToDisplay;
    String value = "Message";

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private View thumb1View;

    private String image_type;
    private ImageButton img_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_message);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString("SingleMessage");
            //Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        }
        TextView txt_message = (TextView)findViewById(R.id.txtviewMessage);






        img_button = (ImageButton)findViewById(R.id.thumb_button_1);

        //////////////////////////////////////////////////////////////////////////////

        //txt_message.setText(Html.fromHtml("<font color=#7030A0>" + value));
        //////////////////////////////////////////////////////////////////////////////

        txt_message.setText(value);
        getTypeOfImage(value);

        thumb1View = findViewById(R.id.thumb_button_1);

////////////////////////////////////////////////////////////////////////////////////////

        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (image_type == "10"){
                    zoomImageFromThumb(thumb1View, R.mipmap.tenb);
                }
                else if (image_type == "25"){
                    zoomImageFromThumb(thumb1View, R.mipmap.twentyfiveb);
                }
                else if (image_type == "50"){
                    zoomImageFromThumb(thumb1View, R.mipmap.fiftyb);
                }
                else if (image_type == "75"){
                    zoomImageFromThumb(thumb1View, R.mipmap.seventyfiveb);
                }
                else if (image_type == "100"){
                    zoomImageFromThumb(thumb1View, R.mipmap.hundredb);
                }
                else {
                    zoomImageFromThumb(thumb1View, R.mipmap.tenb);
                }
            }
        });

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

/////////////////////////////////////////////////////////////////////////////////////////
    }

    public void getTypeOfImage(String milage) {

        if (milage.contains("10000")  && !milage.contains("100000")){
            img_button.setImageResource(R.mipmap.ten);
            image_type = "10";
        }
        else if (milage.contains("25000")){
            img_button.setImageResource(R.mipmap.twentyfive);
            image_type = "25";
        }
        else if (milage.contains("50000")){
            img_button.setImageResource(R.mipmap.fifty);
            image_type = "50";
        }
        else if (milage.contains("75000")){
            img_button.setImageResource(R.mipmap.seventyfive);
            image_type = "75";
        }
        else if (milage.contains("100000")){
            img_button.setImageResource(R.mipmap.hundred);
            image_type = "100";
        }
        else {
            img_button.setImageResource(R.mipmap.ten);
            image_type = "10";
        }
    }

    private void zoomImageFromThumb(final View thumbView, int imageResId) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }


        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);


        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();


        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {

            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {

            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);


        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);


        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;


        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tmp = "";
        //sharedPreferences.edit().putString("messages", "No Messages").apply();

        tmp = sharedPreferences.getString("messages", "Messages");

        intent.putExtra("Message", tmp);

        startActivity(intent);
    }
    //GPSTPYEYBSTN
}

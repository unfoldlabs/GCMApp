package com.softclouds.gcmapp.Fragments;

/**
 * Created by SoftClouds on 6/2/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.softclouds.gcmapp.R;


public class FragmentB extends Fragment {

    ImageView imgView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        imgView = (ImageView)getActivity().findViewById(R.id.instructionsImages);

        //Bitmap bitmapImage = BitmapFactory.decodeFile(getActivity().getResources().getResourceName(R.mipmap.imagestutorialg));
        Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.mipmap.imagestutorialg);
        // int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
        Bitmap scaledImage = Bitmap.createScaledBitmap(bitmapImage,(int)(width * .45),(int)((width * .45) * 2.44), false);
        imgView.setImageBitmap(scaledImage);

    }
}
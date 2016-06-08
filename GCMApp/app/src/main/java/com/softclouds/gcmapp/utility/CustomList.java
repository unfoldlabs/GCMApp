package com.softclouds.gcmapp.utility;

/**
 * Created by SoftClouds on 5/25/2016.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softclouds.gcmapp.R;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList <String> items;



    public CustomList(Activity context,ArrayList items)
    {
        super(context, R.layout.list_single, items);
        this.context = context;
        this.items = items;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(items.get(position).toString());

        if(position == 0){
            imageView.setVisibility(View.GONE);
        }



        return rowView;
    }
    //GPSTPYEYBSTN
}


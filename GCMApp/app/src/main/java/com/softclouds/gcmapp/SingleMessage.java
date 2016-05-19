package com.softclouds.gcmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SingleMessage extends AppCompatActivity {
    ImageView imageToDisplay;
    String value = "Message";
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
        imageToDisplay = (ImageView)findViewById(R.id.imageViewCar);
        txt_message.setText(value);
        getTypeOfDayWithSwitchStatement(value);

    }

    public void getTypeOfDayWithSwitchStatement(String milage) {

       String image_type;

        if (milage .contains("10000")){
            imageToDisplay.setImageResource(R.mipmap.first);
        }
        else if (milage .contains("25000")){
            imageToDisplay.setImageResource(R.mipmap.second);
        }
        else if (milage .contains("50000")){
            imageToDisplay.setImageResource(R.mipmap.third);
        }
        else if (milage .contains("60000")){
            imageToDisplay.setImageResource(R.mipmap.last);
        }

    }
}

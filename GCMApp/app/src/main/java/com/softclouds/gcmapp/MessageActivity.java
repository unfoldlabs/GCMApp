package com.softclouds.gcmapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageActivity extends ListActivity {

    private String value;
    private ArrayList <String> array_of_messages;
    public static final String SEPARATE_MESSAGE = "DBV12261967";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString("Message");
            //Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

            array_of_messages = convertToArray(value);
        }



       // String[] values = new String[] {value};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, array_of_messages);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);


        if(position != 0){
            //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), SingleMessage.class);
            intent.putExtra("SingleMessage", item);
            startActivity(intent);
        }
    }

    private ArrayList<String> convertToArray(String string) {

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(SEPARATE_MESSAGE)));
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

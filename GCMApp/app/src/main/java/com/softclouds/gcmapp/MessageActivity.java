package com.softclouds.gcmapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.softclouds.gcmapp.utility.CustomList;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageActivity extends Activity {

    private String value;
    ListView list;
    ArrayList<String> items_list = new ArrayList<>();
    public static final String SEPARATE_MESSAGE = "DBV12261967";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString("Message");
            //Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

            items_list = convertToArray(value);
        }

        CustomList adapter = new
                CustomList(MessageActivity.this, items_list);
        list=(ListView)findViewById(R.id.listas);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MessageActivity.this, "You Clicked at " + items_list.get(position).toString(), Toast.LENGTH_SHORT).show();
                if(position != 0){
                    //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), SingleMessage.class);
                    intent.putExtra("SingleMessage", items_list.get(position).toString());
                    startActivity(intent);
                }

            }
        });



    }

    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);


        if(position != 0){
            //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), SingleMessage.class);
            intent.putExtra("SingleMessage", item);
            startActivity(intent);
        }
    }*/

    private ArrayList<String> convertToArray(String string) {

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(SEPARATE_MESSAGE)));
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    //GPSTPYEYBSTN
}

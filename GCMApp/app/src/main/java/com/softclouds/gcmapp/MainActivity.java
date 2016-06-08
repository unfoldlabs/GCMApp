package com.softclouds.gcmapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener{

    boolean gps = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private TextView messageWaitingTextView;
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    private EditText editTextEmail;
    private EditText editTextVIN;
    public static final String GCM_TOKEN = "gcmToken";
    public static final String NO_GCM_TOKEN = "No token found";
    private CheckBox registerCheckbox;
    private CheckBox tokenRetrieved;
    private CheckBox tokenSent;
    private CheckBox resgistration_sieble_complete;
    private boolean booleanVINnumber = true;
    static String POSTURL = "http://httpbin.org/post";
    private String vin_str = "";
    private DrawerLayout drawerLayout;
    private ListView listview;
    private String[] menu;
    private ActionBarDrawerToggle drawerlistener;
    private boolean error_connecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////////////////////////////////////////////////////////////////
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        messageWaitingTextView = (TextView) findViewById(R.id.txtview4);

        editTextVIN = (EditText) findViewById(R.id.vinText);

        registerCheckbox = (CheckBox) findViewById(R.id.register);
        tokenRetrieved = (CheckBox) findViewById(R.id.token);
        tokenSent = (CheckBox) findViewById(R.id.siebel);
        resgistration_sieble_complete = (CheckBox) findViewById(R.id.siebelcomplete);

        View header = getLayoutInflater().inflate(R.layout.header, null);
        menu = getResources().getStringArray(R.array.menu_items);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        listview = (ListView)findViewById(R.id.left_drawer);
        listview.addHeaderView(header);
        listview.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu));

        //addHeaderView is to add custom content into first row

        listview.setOnItemClickListener(this);

      /*  drawerlistener = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                Toast.makeText(MainActivity.this, "Close", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                Toast.makeText(MainActivity.this, "Open", Toast.LENGTH_SHORT).show();
            }
        };

        drawerLayout.setDrawerListener(drawerlistener);*/


///////////////////////////////////////////////////////////////////////////////////////////////
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                boolean sentToken = sharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false);

                if (sentToken) {
                   // mInformationTextView.setText(getString(R.string.gcm_send_message));
                    registerCheckbox.setChecked(true);
                    tokenRetrieved.setChecked(true);

                    try {
                        postMethod();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    mInformationTextView.setText(getString(R.string.token_error_message));

                }
            }
        };
        //sendToken();

    }

    private boolean checkPlayServices() {
       try {
           GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
           int result = googleAPI.isGooglePlayServicesAvailable(this);

           if (result != ConnectionResult.SUCCESS) {

               if (googleAPI.isUserResolvableError(result)) {
                   googleAPI.getErrorDialog(this, result,
                           9000).show(); //9000 int
               }
               Toast.makeText(this, "Please install Google Play Services", Toast.LENGTH_LONG).show();

               return false;
           }
       }
           catch (Exception e){
               e.printStackTrace();
           }
           return true;
    }
//Fire registration service
    public void register(View v){
        gps = checkPlayServices();

        vin_str = "" + editTextVIN.getText().toString();
        System.out.println(vin_str);


        if(vin_str == "") {
            booleanVINnumber = false;
            System.out.println("Getting here");
            Toast.makeText(MainActivity.this, "Please enter a VIN# to continue", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (gps) {
            mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);
            // Start IntentService to register this application with GCM.
            try {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
                System.out.println("result ---- : " + gps);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPreferences.edit().putString("messages", "Messages").apply();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    public void seeMessages(View v){

        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tmp = "";
        //sharedPreferences.edit().putString("messages", "No Messages").apply();

        tmp = sharedPreferences.getString("messages", "Messages");

            intent.putExtra("Message", tmp);

        startActivity(intent);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void postMethod() {

        //String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (isNetworkAvailable()) {
            //new AsyncXMLSender().execute();
            WebView webView = (WebView) findViewById(R.id.webView1);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.setWebViewClient(new WebViewClient() {


                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(MainActivity.this, "Connectivity Error. Failed to connect to Siebel" + description, Toast.LENGTH_SHORT).show();
                    error_connecting = true;
                }

                @Override
                public void onReceivedHttpError(
                        WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    Toast.makeText(MainActivity.this, "HTTP Connectivity Error. Failed to connect to Siebel" + errorResponse, Toast.LENGTH_SHORT).show();
                    error_connecting = true;
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Toast.makeText(MainActivity.this, "SSL Connectivity Error. Failed to connect to Siebel" + error, Toast.LENGTH_SHORT).show();
                    error_connecting = true;
                }

            });


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String token_str = sharedPreferences.getString(GCM_TOKEN, NO_GCM_TOKEN);


            String req = "";

            req += "http://70.164.113.90/eai_enu/start.swe?SWEExtSource=apple&SWEExtCmd=Execute&UserName=sadmin&Password=softclouds15&SWEExtData=<?xml version=\"1.0\" encoding=\"UTF-8\"?><?Siebel-Property-Set EscapeNames=\"false\"?><SiebelMessage MessageId=\"1-B9L\" MessageType=\"Integration Object\" IntObjectName=\"IOT Auto Vehicle\" IntObjectFormat=\"Siebel Hierarchical\" ><ListOfIotAutoVehicle ><AutoVehicle ><AndroidAppKey >";

            req += token_str;

            req += "</AndroidAppKey ><AndroidServerKey >";

            req += getResources().getString(R.string.server_key);

            req += "</AndroidServerKey ><ProductId >0V-1CFCZL</ProductId ><SerialNumber >";

            req += vin_str;

            req += "</SerialNumber ></AutoVehicle ></ListOfIotAutoVehicle ></SiebelMessage >";

            webView.loadUrl(req);

            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    if(!error_connecting){
                        tokenSent.setChecked(true);
                        resgistration_sieble_complete.setChecked(true);
                        messageWaitingTextView.setVisibility(View.VISIBLE);
                    }
                }
            }.start();

        } else {
            Toast.makeText(this, "No Network connectivity!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //Toast.makeText(this, menu[position], Toast.LENGTH_SHORT).show();
        if (position == 2) {
            Intent intent = new Intent(this, AboutUS.class);
            startActivity(intent);
        }
        else if(position == 1){
            Intent intent = new Intent(this, Instructions.class);
            startActivity(intent);
        }
    }

    public void menu(View v){
        drawerLayout.openDrawer(Gravity.LEFT); //Edit Gravity.End need API 14
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }

    //GPSTPYEYBSTN

}

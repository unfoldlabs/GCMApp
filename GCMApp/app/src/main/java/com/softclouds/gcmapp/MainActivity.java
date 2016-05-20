package com.softclouds.gcmapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class MainActivity extends Activity {

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


                } else {
                    mInformationTextView.setText(getString(R.string.token_error_message));

                }
            }
        };


    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);

        if(result != ConnectionResult.SUCCESS) {

            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        9000).show(); //9000 int
            }
            Toast.makeText(this,"Please install Google Play Services", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }
//Fire registration service
    public void register(View v){
        gps = checkPlayServices();

        String vin_str = "" + editTextVIN.getText().toString();
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
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
            System.out.println("result ---- : " + gps);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            sharedPreferences.edit().putString("messages", "Messages").apply();

            //sendToken();
            postMethod();

        }
    }

    private class AsyncJsonSender extends AsyncTask<Void, Void, Void>
    {
        private String vin_str = "";
        private String token_str = "";
        private boolean booleanVINnumber = true;
        private int responseCode;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            token_str = sharedPreferences.getString(GCM_TOKEN, NO_GCM_TOKEN);

            //get vin number
            vin_str = "" + editTextVIN.getText().toString();


            if(vin_str == "") {
                booleanVINnumber = false;
                System.out.println("Getting here");
                Toast.makeText(MainActivity.this, "Please enter a valid VIN#", Toast.LENGTH_SHORT).show();
                vin_str = "No VIN# provided";
                return;
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
          //send token and vin to naresh
            System.out.println("Token: " + token_str);
            System.out.println("VIN#: " + vin_str);

            try {
                URL requestUrl = new URL(POSTURL);
                HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

                connection.setDoOutput(true);
                connection.setDoInput(true);

                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");

                JSONObject data = new JSONObject();
                data.put("token", token_str);
                data.put("vin", vin_str);

                // sending data & specifying the encoding utf-8
                OutputStream os = connection.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.close();

                // display what returns the POST request
                StringBuilder sb = new StringBuilder();
                responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println("Server Response: " + sb);

                } else {

                }

            } catch (MalformedURLException e) {
                System.out.println("Error processing URL 4" + e);
            } catch (IOException e) {
                System.out.println("Error connecting to Host 5" + e);
            } catch (JSONException e) {
                System.out.println("Error handling JSON Object 6" + e);
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (responseCode == 200) {
                //success
                System.out.println("Staus: " + responseCode +" OK");
            }

            if(booleanVINnumber) {
               // Toast.makeText(MainActivity.this, "Token Sent to Email", Toast.LENGTH_LONG).show();
                tokenSent.setChecked(true);
                resgistration_sieble_complete.setChecked(true);
                messageWaitingTextView.setVisibility(View.VISIBLE);
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

    public void sendToken(){
        new AsyncJsonSender().execute();
        //ex: public void sendEmail(View v)
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void postMethod() {

        //String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (isNetworkAvailable()) {
            new AsyncJsonSender().execute();

        } else {
            Toast.makeText(this, "No Network connectivity!", Toast.LENGTH_LONG).show();
        }
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

}

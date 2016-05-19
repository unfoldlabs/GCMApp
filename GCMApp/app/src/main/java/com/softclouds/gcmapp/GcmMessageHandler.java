package com.softclouds.gcmapp;

/**
 * Created by SoftClouds on 1/21/2016.
 */
import com.google.android.gms.gcm.GcmListenerService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    ArrayList <String> array_of_messages ;
    String sharedPreferencesString = "";
    public static final String SEPARATE_MESSAGE = "DBV12261967";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Get string
        sharedPreferencesString = sharedPreferences.getString("messages", "Messages:");
        //Convert to Array
        array_of_messages = convertToArray(sharedPreferencesString);
        //Add new message to Array
        array_of_messages.add(message);

        //convert Array back to String
        sharedPreferencesString = convertToString(array_of_messages);
        //Keep the new String
        sharedPreferences.edit().putString("messages", sharedPreferencesString).apply();



       // intent.putStringArrayListExtra("stock_list", stock_list);

        //System.out.println("Message" + message);

        createNotification("SoftClouds Telematics", message);

        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);

        intent.putExtra("Message", sharedPreferencesString);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    // Creates notification based on title and body received
    private void createNotification(String title, String body) {

        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.applogo).setContentTitle(title)
                .setContentText(body);

        ///////////////////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferencesN = PreferenceManager.getDefaultSharedPreferences(this);
        String tmp = "";
        //sharedPreferences.edit().putString("messages", "No Messages").apply();

        tmp = sharedPreferencesN.getString("messages", "Messages");


        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(this, MessageActivity.class).putExtra("Message", tmp), PendingIntent.FLAG_UPDATE_CURRENT);

        //contentIntent.putExtra("Message", sharedPreferencesString);

        mBuilder.setContentIntent(contentIntent);

        ///////////////////////////////////////////////////////////////////////////////////////

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }

    private String convertToString(ArrayList<String> list) {

        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (String s : list)
        {
            sb.append(delim);
            sb.append(s);;
            delim = SEPARATE_MESSAGE;
        }
        return sb.toString();
    }

    private ArrayList<String> convertToArray(String string) {

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(SEPARATE_MESSAGE)));
        return list;
    }}

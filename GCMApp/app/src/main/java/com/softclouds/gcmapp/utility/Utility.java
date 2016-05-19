package com.softclouds.gcmapp.utility;

/**
 * Created by SoftClouds on 5/5/2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utility {

    static String POSTURL = "http://52.26.73.211:8080/unfoldlabs-api/rest/user/applock";

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void postMethod(String userEmailID, String password, Context context) {
        String android_id = Settings.Secure.getString(context
                .getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Utility.isNetworkAvailable(context)) {
            new AppLockInfoAsynTask(android_id, userEmailID, password).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {

        }
    }

    public static class AppLockInfoAsynTask extends AsyncTask<String, Void, String> {
        private String password;
        private String userEmailID;
        private String deviceID;
        private int responseCode;

        public AppLockInfoAsynTask(String android_id, String userEmailID,
                                   String password) {
            this.password = password;
            this.userEmailID = userEmailID;
            deviceID = android_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // process the Search parameter string
            // try to fetch the data
            try {
                URL requestUrl = new URL(POSTURL);
                HttpURLConnection connection = (HttpURLConnection) requestUrl
                        .openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type",
                        "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");

                JSONObject data = new JSONObject();
                System.out.println("App LockuserEmailID HTTP POST Response : "
                        + userEmailID);
                System.out.println("App Lock userEmailID HTTP POST Response : "
                        + password);
                System.out.println("App Lock userEmailID HTTP POST Response : "
                        + deviceID);

                data.put("email", userEmailID);
                data.put("password", password);
                data.put("deviceId", deviceID);
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
                    System.out.println("App Lock HTTP POST Response 1 : " + sb.toString());
                } else {
                    System.out.println("App Lock HTTP POST Response Message 2 : "
                            + connection.getResponseMessage());
                }
                System.out.println("App Lock HTTP Response Code: 3 " + responseCode);
            } catch (MalformedURLException e) {
                System.out.println("App Lock Error processing URL 4" + e);
            } catch (IOException e) {
                System.out.println("App Lock Error connecting to Host 5" + e);
            } catch (JSONException e) {
                System.out.println("App Lock Error handling JSON Object 6" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (responseCode == 200) {
            }
        }
    }
    /** copy file */
    public static void copyFile(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int length;

        while ((length = input.read(buffer)) > 0)
            output.write(buffer, 0, length);
    }





}

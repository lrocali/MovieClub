package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rocali on 9/26/15.
 */
public class CheckInternetConnection extends AsyncTask<String, String, String> {
   Context context;

    public CheckInternetConnection(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... f_url) {   //search via title or by id
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return "true";
                } else {
                    return "false";
                }
            } catch (IOException e) {
                Log.i("warning", "Error checking internet connection", e);
                return "false";
            }
        }
        return "false";
    }


    protected void onPostExecute(String result) {
        Model model = Model.getInstance(context);
        if (result.equals("true")){
            model.setConnection(true);
           Log.v("TAG","CONNECTED");
        } else {
            model.setConnection(false);
            Log.v("TAG","NOT CONNECTED");
        }
    }
}
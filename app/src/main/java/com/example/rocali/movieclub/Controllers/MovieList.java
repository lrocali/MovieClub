package com.example.rocali.movieclub.Controllers;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rocali.movieclub.Models.JsonClass;
import com.example.rocali.movieclub.Models.Model;
import com.example.rocali.movieclub.R;

import org.json.JSONException;
import org.json.JSONObject;


public class MovieList extends ListActivity {

    public Model model = Model.getInstance();
    private static final String TAG = "MyActivity";
    public JSONObject movie = null;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Progress Dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (model.isNetworkConnectionAvailable(this)) {
            Log.v(TAG, "CONECTED");
            model.searchMovie("http://www.omdbapi.com/?t=Fight+Club&y=&plot=short&r=json");
            //Toast.makeText(getApplicationContext(), "File already exist under SD card, playing Music", Toast.LENGTH_LONG).show();
        }
            Log.v(TAG,"NOT CONECTED");

    }
    @Override
    public void onResume() {
        super.onResume();
        //populate list view
        ListView lv = getListView();
        lv.setAdapter(new CustomAdapter(this));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                // sending movie id to new activity
                i.putExtra("movieId", position + "");
                startActivity(i);

            }
        });
    }

    // Async Task Class
    class searchMovieThread extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);
            Log.v(TAG,"PRE EXECUTE");
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                JsonClass json = new JsonClass();
                movie = json.getJSONFromUrl(f_url[0]);
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            //prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            // playMusic();
            Log.v(TAG,"POST EXECUTE");
            String title = null;
            try {
                title = movie.getString("Title");
                Log.v(TAG,title);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(TAG,"ERROR NO TITLE");
            }

        }
    }

}

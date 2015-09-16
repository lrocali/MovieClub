package com.example.rocali.movieclub.Controllers;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.ActionBarActivity;
import android.widget.SearchView;
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

        //ActionBar actionBar = getActionBar();
        //Log.v(TAG,actionBar.getTitle().toString());
        // Enabling Back navigation on Action Bar icon
        //actionBar.setDisplayHomeAsUpEnabled(true);

        //txtQuery = (TextView) findViewById(R.id.txtQuery);

        //handleIntent(getIntent());


        if (model.isNetworkConnectionAvailable(this)) {
            Log.v(TAG, "CONECTED");
            model.searchMovie("http://www.omdbapi.com/?t=Fight+Club&y=&plot=short&r=json");
            //Toast.makeText(getApplicationContext(), "File already exist under SD card, playing Music", Toast.LENGTH_LONG).show();
        }
            Log.v(TAG, "NOT CONECTED");

    }
    @Override
    public void onResume() {
        super.onResume();
        populateListView(false);
    }

    public void populateListView(boolean searching) {
        //populate list view
        ListView lv = getListView();
        if (searching) {
            Toast.makeText(this, "SEARCHING", Toast.LENGTH_LONG).show();
            String[] values = new String[] { "Android List View",
                    "Adapter implementation",
                    "Simple List View In Android",
                    "Create List View Android",
                    "Android Example",
                    "List View Source Code",
                    "List View Array Adapter",
                    "Android Example List View"
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            // Assign adapter to ListView
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                   /* Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                    // sending movie id to new activity
                    i.putExtra("movieId", position + "");
                    startActivity(i);*/

                }
            });
        } else {
            Toast.makeText(this, "NOT SEARCHING", Toast.LENGTH_LONG).show();

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
            Log.v(TAG, "POST EXECUTE");
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
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //txtQuery.setText("Search Query: " + query);

        } else {
            populateListView(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_list, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                //myAdapter.getFilter().filter(newText);

                Log.v(TAG, "on text chnge text: " + newText);
                //Toast.makeText(getApplicationContext(), "CHANGE", Toast.LENGTH_SHORT).show()

                populateListView(true);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is your adapter that will be filtered
                //myAdapter.getFilter().filter(query);
                //populateListView(false);
                Log.v(TAG, "on query submit: " + query);
                return true;
            }
        };
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                populateListView(false);
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        searchView.setOnQueryTextListener(textChangeListener);

        return super.onCreateOptionsMenu(menu);
    }


}

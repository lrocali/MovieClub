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
import com.example.rocali.movieclub.Models.Movie;
import com.example.rocali.movieclub.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MovieList extends ListActivity {

    public Model model;
    private static final String TAG = "MyActivity";
    public JSONObject msJsonObj = null;
    public JSONArray msJsonArray = null;
    ArrayList<String>  msArrayList =new ArrayList<String>();



    //Fetch frmo OMDB
    boolean fetchFromId = false;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Progress Dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);


        model  = Model.getInstance(this);
        fetchSearchedMovies();

        if (model.isNetworkConnectionAvailable(this)) {
            Toast.makeText(this, "CONECTED", Toast.LENGTH_LONG).show();
            //Log.v(TAG, "CONECTED");
            //model.searchMovie("http://www.omdbapi.com/?t=Fight+Club&y=&plot=short&r=json");
            //Toast.makeText(getApplicationContext(), "File already exist under SD card, playing Music", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NOT CONECTED", Toast.LENGTH_LONG).show();
        }

    }

    public void fetchSearchedMovies(){

        Firebase searchedRef = model.firebaseRef.child("searchedMovies");
        searchedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                model.movies.clear();
                if (snapshot.getChildrenCount() != model.movies.size()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                       // Toast.makeText(getApplicationContext(),"BY ID :" +  "Fetching", Toast.LENGTH_SHORT).show();

                        model.addKey(postSnapshot.getKey());
                        Movie movie = postSnapshot.getValue(Movie.class);

                        model.movies.add(movie);
                        populateListView(false);

                    }
                } else {
                    //Toast.makeText(getApplicationContext(),"BY ID :" +  "NOT Fetching", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        populateListView(false);
    }

    public void populateListView(boolean searching) {
        ListView lv = getListView();
        if (searching) {
            //Toast.makeText(this, "SEARCHING", Toast.LENGTH_LONG).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, msArrayList);

            // Assign adapter to ListView
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    //Get movie id on imdb which have been fetched from search
                    try {
                        JSONObject movieSelected = msJsonArray.getJSONObject(position);
                        String imdbID = movieSelected.getString("imdbID");
                        //Toast.makeText(getApplicationContext(), imdbID, Toast.LENGTH_LONG).show();

                        fetchFromId = true;
                        String url = "http://www.omdbapi.com/?i="+imdbID+"&plot=short&r=json";
                        new searchMovieThread().execute(url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v(TAG,"Get imdbId Error");
                    }


                   /* Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                    // sending movie id to new activity
                    i.putExtra("movieId", position + "");
                    startActivity(i);*/

                }
            });
        } else {
            //Toast.makeText(this, "NOT SEARCHING", Toast.LENGTH_LONG).show();

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
        @Override
        protected String doInBackground(String... f_url) {   //search via title or by id
            try {
                JsonClass json = new JsonClass();
                msJsonObj = json.getJSONFromUrl(f_url[0]);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            //Toast.makeText(getApplicationContext(),"POST EXECUTE", Toast.LENGTH_SHORT).show();
            try {

                if (fetchFromId) {
                    Toast.makeText(getApplicationContext(),"BY ID :" +  msJsonObj.getString("imdbID"), Toast.LENGTH_SHORT).show();


                    model.setSearchedMovie("x",
                            msJsonObj.getString("imdbID"),
                            msJsonObj.getString("Title"),
                            msJsonObj.getString("Year"),
                            msJsonObj.getString("Plot"),
                            msJsonObj.getString("Runtime"),
                            msJsonObj.getString("Genre"),
                            msJsonObj.getString("Country"),
                            msJsonObj.getString("imdbVotes"),
                            msJsonObj.getString("imdbRating"),
                            msJsonObj.getString("Poster")
                    );

                    Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                    // sending movie id to new activity
                    i.putExtra("movieId", "-1");
                    startActivity(i);
                    addSearchedMovie();

                } else {
                    msArrayList.clear();
                    msJsonArray = msJsonObj.getJSONArray("Search");
                    Log.v(TAG, "LENGHT" + msJsonArray.length());
                    for (int i = 0; i < msJsonArray.length(); i++) {
                        JSONObject json_data = msJsonArray.getJSONObject(i);

                        msArrayList.add(json_data.getString("Title"));
                    }
                    populateListView(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(TAG,"ERROR NO JSONARRAY");
            }

        }

    }
    public void addSearchedMovie() {
        Firebase searchedRef = model.firebaseRef.child("searchedMovies");
        String key1 = searchedRef.push().getKey();
        model.searchedMovie.setPushId(key1);
        searchedRef.push().setValue(model.searchedMovie);
        String key2 = searchedRef.getKey();

    }



    public void handleSearch(String searchText,boolean submit) {
        if (searchText.length() != 0 && ( Character.isWhitespace(searchText.charAt(searchText.length() - 1)) || submit )) {

            if (model.isNetworkConnectionAvailable(this)) {
                Toast.makeText(getApplicationContext(), model.getSearchableTitle(searchText), Toast.LENGTH_SHORT).show();

                fetchFromId = false;
                String url = "http://www.omdbapi.com/?s=" + model.getSearchableTitle(searchText) + "&y=&plot=short&r=json";
                new searchMovieThread().execute(url);
                populateListView(true);
            } else {
                Toast.makeText(getApplicationContext(),"NO INTERNET CONECTION", Toast.LENGTH_SHORT).show();
            }
        } else {
            msArrayList = new ArrayList<String>();
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
               handleSearch(newText,false);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query,true);
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

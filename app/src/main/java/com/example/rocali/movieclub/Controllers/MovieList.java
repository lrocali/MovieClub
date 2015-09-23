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
import com.example.rocali.movieclub.Models.Party;
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

    //Model
    public Model model;

    //Just for debug
    private static final String TAG = "MyActivity";

    //Variables to fetch JSON from OMDB
    public JSONObject msJsonObj = null;
    public JSONArray msJsonArray = null;
    ArrayList<String>  msArrayList = new ArrayList<String>();
    boolean fetchFromId = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        model  = Model.getInstance(this);

        model.getMoviesMainInfoFromDatabase();

        fetchParties();
    }


    public void populateListView(boolean searching) {
        //get list view id
        ListView lv = getListView();

        //if is in searching mode
        if (searching) {
            //if search update array from movie search array list
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

                        fetchFromId = true;
                        model.getMovie(imdbID);
                        //String url = "http://www.omdbapi.com/?i="+imdbID+"&plot=short&r=json";
                        //new searchMovieThread().execute(url);
                        //populateListView(false);

                        //Call Movie selected activity to show movie details
                        //Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                        //i.putExtra("imdbID", imdbID);
                        //startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v(TAG,"Get imdbId Error");
                    }

                }
            });
        } else {
            lv.setAdapter(new CustomAdapter(this));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                    // sending movie id to new activity
                    //i.putExtra("imdbID", model.movies.get(position).getId());
                    //startActivity(i);

                }
            });
        }

    }

    // OMDB PART
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
            try {
                //If is requesting the movie details
                if (fetchFromId) {
                    //Set requested info into the model
                    /*model.setSearchedMovie(
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
                    );*/
                    //Refresh the list view
                    //populateListView(false);

                    //Call Movie selected activity to show movie details
                    //Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                    //i.putExtra("movieId", "-1");
                    //startActivity(i);
                }
                //If is searching all the movies with an specific name
                else {
                    msArrayList.clear();
                    msJsonArray = msJsonObj.getJSONArray("Search");
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

    public void handleSearch(String searchText,boolean submit) {
        if (searchText.length() != 0 && ( Character.isWhitespace(searchText.charAt(searchText.length() - 1)) || submit )) {

            if (model.isNetworkConnectionAvailable(this)) {
                fetchFromId = false;
                String url = "http://www.omdbapi.com/?s=" + model.getSearchableTitle(searchText) + "&y=&plot=short&r=json";
                new searchMovieThread().execute(url);
                populateListView(true);
            } else {

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
                return false;
            }
        });

        searchView.setOnQueryTextListener(textChangeListener);

        return super.onCreateOptionsMenu(menu);
    }


    //FIREBASE PART
    public void fetchParties(){
        Firebase searchedRef = model.firebaseRef.child("Parties");
        searchedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Party party = postSnapshot.getValue(Party.class);
                    model.parties.add(party);
                    model.insertPartyIntoDatabase(party);
                }
                populateListView(false);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


}

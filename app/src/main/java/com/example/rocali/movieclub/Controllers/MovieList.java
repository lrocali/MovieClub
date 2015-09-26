package com.example.rocali.movieclub.Controllers;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.example.rocali.movieclub.Models.CheckInternetConnection;
import com.example.rocali.movieclub.Models.Model;

import com.example.rocali.movieclub.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MovieList extends ListActivity {

    //Model
    public Model model;

    ArrayList<String>  searchedTitles = new ArrayList<String>();


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String searching = intent.getStringExtra("searching");
            if (searching.equals("true")){
                populateListView(true);
            } else {
                populateListView(false);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        model  = Model.getInstance(this);

        new CheckInternetConnection(this).execute("x");

        model.getMovies();

        model.getParties();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("refreshListView"));

        new CheckInternetConnection(this).execute("x");
    }

    @Override
    protected  void onResume(){
        super.onResume();

        populateListView(false);
    }


    public void populateListView(boolean searching) {
        //get list view id
        ListView lv = getListView();

        //if is in searching mode
        if (searching) {
            searchedTitles = model.getSearchedTitles();

            //if search update array from movie search array list
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, searchedTitles);

            // Assign adapter to ListView
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String imdbID = model.searchedMovies.get(position).getId();

                    //Call Movie selected activity to show movie details
                    Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                    i.putExtra("imdbID", imdbID);
                    i.putExtra("state", "searching");
                    startActivity(i);

                }
            });
        } else {
            lv.setAdapter(new CustomAdapter(this));
        }

    }

    public void handleSearch(String searchText,boolean submit) {
        Log.v("TAG", "HANDLE SEARCH");
        if (searchText.length() != 0 && ( Character.isWhitespace(searchText.charAt(searchText.length() - 1)) || submit )) {
            Log.v("TAG", "SEARCH");
            new CheckInternetConnection(this).execute("x");
            model.getSearchedMovies(searchText);
            if (model.internetConnection) {
                //Toast.makeText(getApplicationContext(),"Internet connection", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(),"NO Internet connection", Toast.LENGTH_SHORT).show();

            }
        }
        if(searchText.length() == 0){
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





}

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
import com.example.rocali.movieclub.Models.JsonClass;
import com.example.rocali.movieclub.Models.Model;
import com.example.rocali.movieclub.Models.Movie;
import com.example.rocali.movieclub.Models.MovieMainInfo;
import com.example.rocali.movieclub.Models.Party;
import com.example.rocali.movieclub.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;


public class MovieList extends ListActivity {

    //Model
    public Model model;

    //Just for debug
    private static final String TAG = "MyActivity";

    //Variables to fetch JSON from OMDB
    public JSONObject msJsonObj = null;
    public JSONArray msJsonArray = null;
    ArrayList<String>  searchedTitles = new ArrayList<String>();
    boolean fetchFromId = false;

    final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

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

        //IntentFilter filter = new IntentFilter(CONNECTIVITY_CHANGE_ACTION);
        //this.registerReceiver(changeWifiReceiver, filter);
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

                        //JSONObject movieSelected = msJsonArray.getJSONObject(position);
                        //String imdbID = movieSelected.getString("imdbID");

                        String imdbID = model.searchedMovies.get(position).getId();
                        //fetchFromId = true;
                        //model.getMovie(imdbID);
                        //String url = "http://www.omdbapi.com/?i="+imdbID+"&plot=short&r=json";
                        //new searchMovieThread().execute(url);
                        //populateListView(false);

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


    /*public boolean issNetworkConnectionAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        try{
            // ping to googe to check internet connectivity
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 80);
            socket.connect(socketAddress, 3000);
            socket.close();
            Log.v(TAG, "CONECTED");
            return true;

        } catch (Exception e) {
            Log.v(TAG,"NOT CONECTED");
            return false;
        }/
    }*/

    public void handleSearch(String searchText,boolean submit) {
        Log.v(model.TAG,"HANDLE SEARCH");
        new CheckInternetConnection(this).execute("x");
        if (searchText.length() != 0 && ( Character.isWhitespace(searchText.charAt(searchText.length() - 1)) || submit )) {
            Log.v(model.TAG, "SEARCH");
            model.getSearchedMovies(searchText);
            if (model.internetConnection) {
                Toast.makeText(getApplicationContext(),"Internet connection", Toast.LENGTH_SHORT).show();

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

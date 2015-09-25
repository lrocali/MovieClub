package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.rocali.movieclub.Controllers.MovieList;
import com.example.rocali.movieclub.Controllers.MovieSelected;
import com.firebase.client.Firebase;

import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * Created by rocali on 8/24/15.
 */
public class Model {

    public static final String TAG = "TAG";

    //Singleton Model
    public ArrayList<MovieMainInfo> movies;
    public ArrayList<MovieMainInfo> searchedMovies;
    public ArrayList<Party> parties;

    private Movie movie;
    public static Model instance = null;

    private Context context;

    //CHAIN OF RESPONSABILITY
    public MovieInfoChain memoryModelChain;
    public MovieInfoChain databaseChain;
    public MovieInfoChain OMDBChain;

    public Database database;
    public Fbase firebase;

    public boolean internetConnection;

    public static Model getInstance(Context _context){
        if (instance == null) {
            instance = new Model(_context);
        }
        return instance;
    }

    public void setConnection(boolean status){
        this.internetConnection = status;
    }

    public Model (Context _context) {
        context = _context;

        internetConnection = true;
        movie = new Movie();
        movies = new ArrayList<MovieMainInfo>();
        searchedMovies = new ArrayList<MovieMainInfo>();
        parties = new ArrayList<Party>();

        database = new Database(context);
        firebase = new Fbase(context);

        setChains(context);

        Log.v(TAG, "CREATING MODEL");
    }

    //PARTY
    public void getParties(){
        parties = database.getPartiesFromDatabase();
        firebase.fetchParties();


    }
        //This function is called just on thread of firebase
    public void setParties(ArrayList<Party> parties){
        this.parties = new ArrayList<Party>(){};
        this.parties = parties;
        Intent intent = new Intent("refreshListView");
        intent.putExtra("searching", "false");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }

    public void addParty(Party party){
        this.parties.add(party);
        database.addPartyToDB(party);
        firebase.addPartyToCloud(party);
    }

    public Party checkForParty(String imdbID) {
        for (Party party : parties){
            if (new String(party.getId()).equals(imdbID)) {
                return party;
            }
        }
        return null;
    }

    //CHAIN OF RESPOSABILITY
    public void setChains(Context context){
        memoryModelChain = new MemoryModel(context);
        databaseChain = new Database(context);
        OMDBChain = new OMDB(context);

        memoryModelChain.setNextChain(databaseChain);
        databaseChain.setNextChain(OMDBChain);
    }

    //MOVIE
    public void getMovie(String id){
        movie = memoryModelChain.getMovieInfo(id);
    }

    public void getSearchedMovies(String title){
        Log.v(TAG,"GET SEARCHED MOVIES");
        searchedMovies = memoryModelChain.searchMovie(title);
        if(searchedMovies!=null){
            sendBroadCastToListView("true");
        } else {
            sendBroadCastToListView("false");
        }
    }
        //only called frmo the thread of OMDB fetching
    public void setSearchedMovies(ArrayList<MovieMainInfo> searchedMovies){
        Log.v(TAG, "SET SEARCHMOVIES ANDR SENT INTENTION TO REFRESH LIST VIEW");
        this.searchedMovies = searchedMovies;
        sendBroadCastToListView("true");
    }

    public void sendBroadCastToListView(String searching){
        Intent intent = new Intent("refreshListView");
        intent.putExtra("searching", searching);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    //

    /*public boolean isNetworkConnectionAvailable(Context cntxt) {
        ConnectivityManager cm =(ConnectivityManager) cntxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if (isConnected) {
           // Log.v(TAG, "CONECTED");
            return true;
        } else {
            //Log.v(TAG,"NOT CONECTED");
            return  false;
        }
        /*try{
            // ping to googlevto check internet connectivity
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 80);
            socket.connect(socketAddress, 3000);
            socket.close();
            Log.v(TAG, "CONECTED");
            return true;

        } catch (Exception e) {
            Log.v(TAG,"NOT CONECTED");
            return false;
        }
        /*
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        Log.v(TAG,network.toString());
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);

    }*/

    //SEARCH
    public String getSearchableTitle(String enteredText) {

        String searchableTitle = new String();
        boolean deleteLastChar = false;     //Delete white space in case of last char is space or not
                //enteredText.substring(0, enteredText.length()-1);
        char[] titleChars = enteredText.toCharArray();
        for(int i = 0; i < titleChars.length; i++){
            if(Character.isWhitespace(titleChars[i])){
                if (i == titleChars.length-1)
                    deleteLastChar = true;
                titleChars[i] = '+';
            }
        }

        searchableTitle = String.valueOf(titleChars);
        if (deleteLastChar)
            searchableTitle = searchableTitle.substring(0, searchableTitle.length()-1);
        return searchableTitle;
    }



    public void getMovies(){
        this.movies = database.getMoviesMainInfoFromDatabase();
    }
    public void updateMovie(){
        database.updateMovie(movie);
        getMovies();
        movie = memoryModelChain.getMovieInfo(movie.getId());
    }
    public void updateMovie(int index){
        database.updateMovie(movies.get(index));
        getMovies();
        movie = memoryModelChain.getMovieInfo(movies.get(index).getId());
    }

    public void insertMovie(){
        database.insertSearchedMovieIntoDatabase(movie);
        getMovies();

    }

   /* public void setMovies(ArrayList<MovieMainInfo> movies){
        this.movies = movies;

    }*/

    public ArrayList<String> getSearchedTitles(){
        ArrayList<String> searchedTitles = new ArrayList<String>(){};
        for(MovieMainInfo movie : searchedMovies){
            searchedTitles.add(movie.getTitle());
        }
        return searchedTitles;
    }


    public Movie getMovie(){
        return movie;
    }
    public void setMovie(Movie movie){
        Log.v(TAG,"SET MOVIE AND SENT INTENTION START MOVIESELECTED ACTIVITY");
        this.movie = movie;
        Intent intent = new Intent("refreshMovieSelected");
        intent.putExtra("source", "omdb");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}

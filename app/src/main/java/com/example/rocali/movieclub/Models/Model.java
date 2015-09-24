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

    //public Movie [] movies;
    //public Movie searchedMovie;
    public Movie selectedMovie;
    private Movie movie;
    public static Model instance = null;

    private Context context;

    //CHAIN OF RESPONSABILITY
    public MovieInfoChain memoryModelChain;
    public MovieInfoChain databaseChain;
    public MovieInfoChain OMDBChain;

    public Database database;

    //Firebase
    public Firebase firebaseRef;

    //SQLite DB
    //SQLData DB;
    public static Model getInstance(Context _context){
        if (instance == null) {
            instance = new Model(_context);
        }
        return instance;
    }

    //PARTY
    public boolean hasParty(String id) {
        for (Party party : parties){
            if (new String(party.getId()).equals(id)) {
                return true;
            }
        }
        return false;

    }

    //CHAIN OF RESPOSABILITY
    public void setChains(Context context){
        memoryModelChain = new MemoryModel(context);
        databaseChain = new Database(context);
        OMDBChain = new OMDB(context);

        memoryModelChain.setNextChain(databaseChain);
        databaseChain.setNextChain(OMDBChain);
    }

    public void getMovie(String id){
        movie = memoryModelChain.getMovieInfo(id);
        //return movie;
    }

    public void getSearchedMovies(String title){
        searchedMovies = memoryModelChain.searchMovie(title);
    }
    public void setSearchedMovies(ArrayList<MovieMainInfo> searchedMovies){
        Log.v(TAG,"SET SEARCHMOVIES ANDR SENT INTENTION TO REFRESH LIST VIEW");
        this.searchedMovies = searchedMovies;
        Intent intent = new Intent("refreshListView");
        intent.putExtra("searching", "true");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public Party checkForParty(String imdbID) {
        for (Party party : parties){
            if (new String(party.getId()).equals(imdbID)) {
                return party;
            }
        }
        return null;
    }
    //

    public void startActivity(){
        Intent intent = new Intent(context, MovieSelected.class);
        intent.putExtra("imdbID", "0");
        intent.putExtra("state", "searching");
        context.startActivity(intent);
    }

    public boolean isNetworkConnectionAvailable(Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

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
        //Log.v(TAG,searchableTitle);
        return searchableTitle;
    }

    public Model (Context _context) {
        //List of movie and parties
        movies = new ArrayList<MovieMainInfo>();
        parties = new ArrayList<Party>();

        searchedMovies = new ArrayList<MovieMainInfo>();
        context = _context;

        //DB = new SQLData(context);
        //Set context to firebase
        Firebase.setAndroidContext(context);
        firebaseRef = new Firebase("https://torrid-heat-8747.firebaseio.com/");

        setChains(context);
        movie = new Movie();

        Log.v(TAG,"CREATING MODEL");

        database = new Database(context);

    }

    public void getMoviesFromDB(){
        String x = "x";
        this.movies = database.getMoviesMainInfoFromDatabase();
    }
    public void updateMovie(){
        database.updateMovie(movie);
        getMoviesFromDB();
        movie = memoryModelChain.getMovieInfo(movie.getId());
    }
    public void updateMovie(int index){
        database.updateMovie(movies.get(index));
        getMoviesFromDB();
        movie = memoryModelChain.getMovieInfo(movies.get(index).getId());
    }

    public void insertMovie(){
        database.insertSearchedMovieIntoDatabase(movie);
        getMoviesFromDB();

    }
    public void getPartiesFromDB(){

    }
    public void savePartyIntoDB(Party party){

    }



    //DATABASE FUNCITONS
/*
    public void insertSearchedMovieIntoDatabase(){
        boolean result = DB.insertMovie(movie.getId(), movie.getTitle(), movie.getYear(), movie.getPlot(), movie.getRuntime(), movie.getGenre(), movie.getCountry(), movie.getImdbVotes(), movie.getImdbRating(), movie.getImgURL(), "0");
        if (result) {
            System.out.print("SENDED");
            getMoviesMainInfoFromDatabase();
        }
        else
            System.out.print("NOT SENDED");
    }

    public void updateMovie(int index){
        boolean result = DB.updatetMovie(movies.get(index).getId(), Float.toString(movies.get(index).getRating()));
        if (result) {
            System.out.print("UPDATED");
            getMoviesMainInfoFromDatabase();
        }
        else
            System.out.print("NOT UPDATED");
    }
    public void updateMovie(){
        boolean result = DB.updatetMovie(movie.getId(), Float.toString(movie.getRating()));
        if (result) {
            System.out.print("UPDATED");
            getMoviesMainInfoFromDatabase();
        }
        else
            System.out.print("NOT UPDATED");
    }



    public void getMoviesMainInfoFromDatabase() {
        Cursor res = DB.getAllMovies();
        if (res.getCount() == 0)
            return;
        movies.clear();
        while (res.moveToNext()) {
            MovieMainInfo movie = new MovieMainInfo(res.getString(0),res.getString(1),res.getString(2),res.getString(9),res.getString(10));
            movies.add(movie);
        }
    }*/
/*
    public Movie getMovieFromDatabase(int id) { //id of the list
        /*Cursor res = DB.getMovie(movies.get(id).getId());
        if (res.getCount() == 0)
            return null;

        if (res.moveToFirst()){
            String c = res.getString(0);
            String cc = res.getString(1);
        }
        selectedMovie = new Movie(res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9),res.getString(10));

        return chain1.getMovieInfo(movies.get(id).getId());
    }

    public void insertPartyIntoDatabase(Party party){
        //AD INVITEs SHOULD IMPRMEEN XXXXXXXXXX
        boolean result = DB.insertParty(party.getId(),party.getDate(),party.getTime(),party.getVenue(),party.getLocation(),"X invites");
        if (result) {
            System.out.print("SENDED");
            //getMoviesMainInfoFromDatabase();
        }
        else
            System.out.print("NOT SENDED");
    }

    public void getPartiesFromDatabase() {
        Cursor res = DB.getAllParties();
        if (res.getCount() == 0)
            return;
        movies.clear();
        parties.clear();
        while (res.moveToNext()) {
            Party party = new Party(res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),new ArrayList<String>(){});
            parties.add(party);
        }
    }*/

    public void setMovies(ArrayList<MovieMainInfo> movies){
        this.movies = movies;

    }
    public void setParties(ArrayList<Party> parties){
        this.parties = parties;
    }

    /*
    public void setSearchedMovie(String _id,String _title, String _year, String _plot,String _runtime,String _genre,String _country,String _imdbVotes,String _imdbRating,String _imgURL){
        searchedMovie = new Movie(_id,_title,_year,_plot,_runtime,_genre,_country,_imdbVotes,_imdbRating,_imgURL,"0");

    }*/


    public void addPartyToCloud(Party party) {
        Firebase searchedRef = firebaseRef.child("Parties");
        searchedRef.push().setValue(party);
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

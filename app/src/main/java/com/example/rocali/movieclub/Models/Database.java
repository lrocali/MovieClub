package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rocali on 9/23/15.
 */
public class Database implements MovieInfoChain{

    private MovieInfoChain nextChain;
    private  Context context;
    SQLData DB;

    public Database(Context context){
        this.context = context;
        DB = new SQLData(context);
    }

    @Override
    public void setNextChain(MovieInfoChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public Movie getMovieInfo(String id) {
        Cursor res = DB.getMovie(id);
        if (res.getCount() != 0 && res.moveToFirst()) {
            Log.v("TAG", "GET MOVIE FROM DATABASE");
            return new Movie(res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9),res.getString(10));
        }
        else {
            return nextChain.getMovieInfo(id);
        }
    }

    @Override
    public ArrayList<MovieMainInfo> searchMovie(String title) {
        Cursor res = DB.getAllMovies();
        if (res.getCount() == 0) {
            Log.v("TAG", "NOT IN DB LETS SEE OMDB (RES.COUNT = 0)");
            return nextChain.searchMovie(title);
        }
        ArrayList<MovieMainInfo> movies = new ArrayList<MovieMainInfo>(){};
        while (res.moveToNext()) {
            //Log.v(model.TAG, res.getString(1));
                if (res.getString(1).contains(title)) {
                    MovieMainInfo movie = new MovieMainInfo(res.getString(0), res.getString(1), res.getString(2), res.getString(9), res.getString(10));
                    movies.add(movie);
                }

        }
        if (movies.size() == 0){
            Log.v("TAG", "NOT IN DB LETS SEE OMDB (MOVIES.SIZE = 0)");
            return nextChain.searchMovie(title);
        } else {
            Log.v("TAG", "GET MOVIE LIST FROM DATABASE");
            return movies;
        }
    }

    public void insertSearchedMovieIntoDatabase(Movie movie){

        boolean result = DB.insertMovie(movie.getId(), movie.getTitle(), movie.getYear(), movie.getPlot(), movie.getRuntime(), movie.getGenre(), movie.getCountry(), movie.getImdbVotes(), movie.getImdbRating(), movie.getImgURL(), "0");
        if (result) {
            getMoviesMainInfoFromDatabase();
            Log.v("TAG", "MOVIE SENT TO DB");
        }
        else
            Log.v("TAG", "MOVIE NOT SENT TO DB");
    }

    public void updateMovie(MovieMainInfo movie){
        boolean result = DB.updatetMovie(movie.getId(), Float.toString(movie.getRating()));
        if (result) {
            Log.v("TAG", "UPDATED MOVIE TO DB");
            getMoviesMainInfoFromDatabase();
        }
        else
            Log.v("TAG", "NOT UPDATED TO DB");
    }

    public void updateParty(Party party){
        boolean result = DB.updateParty(party.getId(), party.getDate(), party.getTime(), party.getVenue(), party.getLocation(), " ");
        if (result) {
            Log.v("TAG", "UPDATED PARTY TO DB");
            getMoviesMainInfoFromDatabase();
        }
        else
            Log.v("TAG", "NOT UPDATED TO DB");
    }

    public ArrayList<MovieMainInfo> getMoviesMainInfoFromDatabase() {
        Cursor res = DB.getAllMovies();
        ArrayList<MovieMainInfo> movies = new ArrayList<MovieMainInfo>(){};
        if (res.getCount() == 0) {
            movies.add(new MovieMainInfo());
            return movies; //model.setMovies(null);
        }

        while (res.moveToNext()) {
            MovieMainInfo movie = new MovieMainInfo(res.getString(0),res.getString(1),res.getString(2),res.getString(9),res.getString(10));
            movies.add(movie);
        }
        return movies;
    }

    public void addPartyToDB(Party party){
        //AD INVITEs SHOULD IMPRMEEN XXXXXXXXXX
        boolean result = DB.insertParty(party.getId(), party.getDate(), party.getTime(), party.getVenue(), party.getLocation(), "X invites");
        if (result) {
            Log.v("TAG", "PARTY SENT TO DB");
            //getMoviesMainInfoFromDatabase();
        }
        else
            Log.v("TAG", "PARTY NOT SENT TO DB");
    }

    public ArrayList<Party>  getPartiesFromDatabase() {
        Cursor res = DB.getAllParties();
        if (res.getCount() == 0)
            return null;
        ArrayList<Party> parties = new ArrayList<Party>(){};
        while (res.moveToNext()) {
            Party party = new Party(res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),new ArrayList<String>(){});
            parties.add(party);
        }
        return parties;
    }
}
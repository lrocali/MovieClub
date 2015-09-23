package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.database.Cursor;

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
            return new Movie(res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9),res.getString(10));
        }
        else {
            return nextChain.getMovieInfo(id);
        }
    }

    @Override
    public Movie searchMovie(String title) {
        return null;
    }
}
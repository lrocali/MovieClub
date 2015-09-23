package com.example.rocali.movieclub.Models;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by rocali on 9/23/15.
 */
public class MemoryModel implements MovieInfoChain{

    private MovieInfoChain nextChain;
    private Context context;

    public MemoryModel(Context context){
        this.context = context;
    }

    @Override
    public void setNextChain(MovieInfoChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public Movie getMovieInfo(String id) {
        Model model = Model.getInstance(context);
        String idModel = model.getMovie().getId();
        if (idModel.equals(id)) {
            return model.getMovie();
        } else {
            return nextChain.getMovieInfo(id);
        }
    }

    @Override
    public ArrayList<MovieMainInfo> searchMovie(String title) {
        Model model = Model.getInstance(context);
        String titleModel = model.getMovie().getTitle();
        if (titleModel.equals(title)){
            ArrayList<MovieMainInfo> movies = new ArrayList<MovieMainInfo>(){};
            movies.add(model.getMovie());
            return movies;
        } else {
            return nextChain.searchMovie(title);
        }
    }
}

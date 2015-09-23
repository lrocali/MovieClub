package com.example.rocali.movieclub.Models;

import android.content.Context;

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
        String id1 = model.getMovie().getId();
        String id2 = id;
        if (id1.equals(id2)) {
            return model.getMovie();
        } else {
            return nextChain.getMovieInfo(id);
        }
    }

    @Override
    public Movie searchMovie(String title) {
        return null;
    }
}

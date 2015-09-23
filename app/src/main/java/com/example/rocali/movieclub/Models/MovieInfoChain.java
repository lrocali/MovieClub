package com.example.rocali.movieclub.Models;

import android.content.Context;

/**
 * Created by rocali on 9/23/15.
 */
public interface MovieInfoChain {

    void setNextChain(MovieInfoChain nextChain);

    Movie getMovieInfo(String id);

    Movie searchMovie(String title);

}

package com.example.rocali.movieclub.Models;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by rocali on 9/23/15.
 */
public interface MovieInfoChain {

    void setNextChain(MovieInfoChain nextChain);

    Movie getMovieInfo(String id);

    ArrayList<MovieMainInfo> searchMovie(String title);

}

package com.example.rocali.movieclub.Models;

import android.provider.Telephony;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocali on 8/24/15.
 */
public class Movie extends MovieMainInfo{
    //no editable atributes
    //private String id;
    //private String title;
    //private String year;
    private String plot;
    private String runtime;
    private String genre;
    private String country;
    private String imdbVotes;
    private String imdbRating;
    //private String imgURL;

    //editable atributes
    //private float rating;
    public Movie() {
        super(" "," "," "," ","0");
        //id = " ";
        //title =  " ";
        //year = " ";
        plot = " ";
        runtime =  " ";
        genre = " ";
        country = " ";
        imdbVotes = " ";
        imdbRating = " ";
        //imgURL = " ";

        //Editable
        //rating = Float.parseFloat("0");
    }

    //initialize movie with pre-defined and no editable atributes
    public Movie(String _id, String _title, String _year, String _plot, String _runtime, String _genre, String _country, String _imdbVotes, String _imdbRating, String _imgURL, String _rating) {
        super(_id,_title,_year,_imgURL,_rating);

        plot = _plot;
        runtime = _runtime;
        genre = _genre;
        country = _country;
        imdbVotes = _imdbVotes;
        imdbRating = _imdbRating;
    }

    //Non editables (just get, initialized just on creation)

    public String getPlot() {
        return plot;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getCountry() {
        return country;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getImdbRating() {
        return imdbRating;
    }


}


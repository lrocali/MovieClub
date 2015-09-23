package com.example.rocali.movieclub.Models;

import android.provider.Telephony;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocali on 8/24/15.
 */
public class Movie {
    //no editable atributes
    private String id;
    private String title;
    private String year;
    private String plot;
    private String runtime;
    private String genre;
    private String country;
    private String imdbVotes;
    private String imdbRating;
    private String imgURL;

    //editable atributes
    private float rating;
    public Movie() {
        id = " ";
        title =  " ";
        year = " ";
        plot = " ";
        runtime =  " ";
        genre = " ";
        country = " ";
        imdbVotes = " ";
        imdbRating = " ";
        imgURL = " ";

        //Editable
        rating = Float.parseFloat("0");
    }

    //initialize movie with pre-defined and no editable atributes
    public Movie(String _id, String _title, String _year, String _plot, String _runtime, String _genre, String _country, String _imdbVotes, String _imdbRating, String _imgURL, String _rating) {
        //Non editable
        id = _id;
        title = _title;
        year = _year;
        plot = _plot;
        runtime = _runtime;
        genre = _genre;
        country = _country;
        imdbVotes = _imdbVotes;
        imdbRating = _imdbRating;
        imgURL = _imgURL;

        //Editable
        rating = Float.parseFloat(_rating);

    }

    //Non editables (just get, initialized just on creation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

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

    public String getImgURL() {
        return imgURL;
    }

    //Editables
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}


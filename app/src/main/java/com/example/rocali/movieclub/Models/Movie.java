package com.example.rocali.movieclub.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocali on 8/24/15.
 */
public class Movie {
    //no editable atributes
    private String id;
    public String title;
    public String year;
    public String plot;
    public String runtime;
    public String genre;
    public String country;
    public String imdbVotes;
    public String imdbRating;
    public String imgURL;
    //editable atributes
    public float rating;
    public String date;
    public String time;
    public String venue;
    public String location;
    public List<String> invitees = new ArrayList<String>();
    public boolean scheduled = false;

    //initialize movie with pre-defined and no editable atributes
    public Movie (String _id,String _title, String _year, String _plot,String _runtime,String _genre,String _country,String _imdbVotes,String _imdbRating,String _imgURL) {
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
        rating = 0;
    }

    /*
    //initialize movie with pre-defined and no editable atributes
    public Movie () {
        imgSrc = getImgName("fightclub");
        rating = 0;
    }*/

    /*
    //generate id of 10 letters based on the number of letters on title and number of the year
    private String generateId(String title,String year) {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVXWYZ";
        int n = alphabet.length();
        int baseTitle = title.length();
        int baseYear = Integer.parseInt(year);
        int base = baseTitle % n;
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + alphabet.charAt(base);
            base = (base  * i +  baseYear)  % n;
        }

        return id;
    }
*/
    /*
    //"Generate" the name of the img file, which is the title in lowercase and no spaces
    private String getImgName(String title) {
        String imgsrc = title.toLowerCase();
        imgsrc = imgsrc.replaceAll(" ", "");
        imgsrc = "cityofgod";
        return imgsrc;
    }*/

}

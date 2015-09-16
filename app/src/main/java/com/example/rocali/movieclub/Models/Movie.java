package com.example.rocali.movieclub.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocali on 8/24/15.
 */
public class Movie {
    //no editable atributes
    public String title;
    public String year;
    public String shortPlot;
    public String fullPlot;
    private String id;
    public String imgSrc;
    //editable atributes
    public float rating;
    public String date;
    public String time;
    public String venue;
    public String location;
    public List<String> invitees = new ArrayList<String>();
    public boolean scheduled = false;

    //initialize movie with pre-defined and no editable atributes
    public Movie (String _title, String _year, String _shortPlot, String _fullPlot) {
        title = _title;
        year = _year;
        shortPlot = _shortPlot;
        fullPlot = _fullPlot;
        id = generateId(_title,_year);
        imgSrc = getImgName(_title);
        rating = 0;

    }

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

    //"Generate" the name of the img file, which is the title in lowercase and no spaces
    private String getImgName(String title) {
        String imgsrc = title.toLowerCase();
        imgsrc = imgsrc.replaceAll(" ", "");
        return imgsrc;
    }

}

package com.example.rocali.movieclub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocali on 8/24/15.
 */
public class Movie {
    public String title;
    public String year;
    public String shortPlot;
    public String fullPlot;
    private String id;
    public String imgSrc;
    public float rating;
    public String date;
    public String time;
    public String venue;
    public String location;
    public List<String> invitees = new ArrayList<String>();

    public Movie (String _title, String _year, String _shortPlot, String _fullPlot) {
        title = _title;
        year = _year;
        shortPlot = _shortPlot;
        fullPlot = _fullPlot;
        id = generateId(_title);
        imgSrc = getImgName(_title);
        rating = 0;

        date = "No date yet";
        time = "No time yet";
        venue = "No venue yet";
        location = "No location yet";
        invitees.add("user1");
        invitees.add("user2");
        invitees.add("user3");
        invitees.add("user4");
        invitees.add("user5");

    }

    private String generateId(String title) {
        return title.toUpperCase();
    }

    private String getImgName(String title) {
        String imgsrc = title.toLowerCase();
        imgsrc = imgsrc.replaceAll(" ", "");
        return imgsrc;
    }

}

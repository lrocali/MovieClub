package com.example.rocali.movieclub;

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

    public Movie (String _title, String _year, String _shortPlot, String _fullPlot) {
        title = _title;
        year = _year;
        shortPlot = _shortPlot;
        fullPlot = _fullPlot;
        id = generateId(_title);
        imgSrc = getImgName(_title);
        rating = 0;

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

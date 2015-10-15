package com.example.rocali.movieclub.Models;

/**
 * Created by rocali on 9/23/15.
 */
public class MovieMainInfo {
    private String id;
    private String title;
    private String year;
    private String imgURL;
    private float rating;

    public MovieMainInfo (){
        this.id = "x";
        this.title = "x";
        this.year = "x";
        this.imgURL = "x";
        this.rating = 2;
    }

    public MovieMainInfo(String _id,String _title,String _year,String _imgURL,String _rating){
        id = _id;
        title = _title;
        year = _year;
        imgURL = _imgURL;
        rating = Float.parseFloat(_rating);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}


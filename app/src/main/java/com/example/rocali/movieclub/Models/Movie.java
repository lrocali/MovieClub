package com.example.rocali.movieclub.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocali on 8/24/15.
 */
public class Movie {
    //no editable atributes
    private String pushId;
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
    private boolean scheduled;
    private float rating;
    private String date;
    private String time;
    private String venue;
    private String location;
    private ArrayList<String> invitees;


    public Movie () {
        //Firebase
    }
    //initialize movie with pre-defined and no editable atributes
    public Movie (String _pushId,String _id,String _title, String _year, String _plot,String _runtime,String _genre,String _country,String _imdbVotes,String _imdbRating,String _imgURL) {
        //Non editable
        pushId = _pushId;
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

        //Editable
        date = " ";
        time = " ";
        venue = " ";
        location = " ";
        scheduled = false;
        invitees = new ArrayList<String>();
        invitees.add("user0");
    }

    //Non editables (just get, initialized just on creation)
    public String getTitle(){
        return title;
    }
    public String getYear(){
        return year;
    }
    public String getPlot(){
        return plot;
    }
    public String getRuntime(){
        return runtime;
    }
    public String getGenre(){
        return genre;
    }
    public String getCountry(){
        return country;
    }
    public String getImdbVotes(){
        return imdbVotes;
    }
    public String getImdbRating(){
        return imdbRating;
    }
    public String getImgURL(){
        return imgURL;
    }

    //Editables
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getInvitees() {
        return invitees;
    }

    public void setInvitees(ArrayList<String> invitees) {
        this.invitees = invitees;
    }

    public void addInvitees(String invited){
        this.invitees.add(invited);
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
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

package com.example.rocali.movieclub.Models;

import java.util.ArrayList;

/**
 * Created by rocali on 9/20/15.
 */
public class Party {

    private String date;
    private String time;
    private String venue;
    private String location;
    private ArrayList<String> invitees;

    public  Party() {
        date = " ";
        time = " ";
        venue = " ";
        location = " ";

        invitees = new ArrayList<String>();
        invitees.add("user0");
    }

    public Party(String _date,String _time, String _venue, String _location, ArrayList<String> _invitees){
        this.date = _date;
        this.time = _time;
        this.venue = _venue;
        this.location = _location;
        this.invitees = _invitees;
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
}

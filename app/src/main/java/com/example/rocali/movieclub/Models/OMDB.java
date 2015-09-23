package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rocali.movieclub.Controllers.MovieSelected;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rocali on 9/23/15.
 */
public class OMDB implements MovieInfoChain{

    private MovieInfoChain nextChain;
    private Context context;

    public JSONObject msJsonObj = null;
    public JSONArray msJsonArray = null;

    public OMDB(Context context){
        this.context = context;
    }
    @Override
    public void setNextChain(MovieInfoChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public Movie getMovieInfo(String id) {
        String url = "http://www.omdbapi.com/?i="+id+"&plot=short&r=json";
        new searchMovieByIDThread().execute(url);
        return new Movie();
    }

    @Override
    public ArrayList<MovieMainInfo> searchMovie(String title) {
        String url = "http://www.omdbapi.com/?s=" + title + "&y=&plot=short&r=json";
        new searchMovieByTitleThread().execute(url);
        return new ArrayList<MovieMainInfo>(){};
    }
    class searchMovieByIDThread extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... f_url) {   //search via title or by id
            try {
                JsonClass json = new JsonClass();
                msJsonObj = json.getJSONFromUrl(f_url[0]);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try {
                Model model = Model.getInstance(context);
                model.setMovie( new Movie(
                        msJsonObj.getString("imdbID"),
                        msJsonObj.getString("Title"),
                        msJsonObj.getString("Year"),
                        msJsonObj.getString("Plot"),
                        msJsonObj.getString("Runtime"),
                        msJsonObj.getString("Genre"),
                        msJsonObj.getString("Country"),
                        msJsonObj.getString("imdbVotes"),
                        msJsonObj.getString("imdbRating"),
                        msJsonObj.getString("Poster"),
                        "0"));
                model.startActivity();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    class searchMovieByTitleThread extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... f_url) {   //search via title or by id
            try {
                JsonClass json = new JsonClass();
                msJsonObj = json.getJSONFromUrl(f_url[0]);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try {
                Model model = Model.getInstance(context);
                    msJsonArray = msJsonObj.getJSONArray("Search");
                    ArrayList<MovieMainInfo> tempMovies = new ArrayList<MovieMainInfo>(){};
                    for (int i = 0; i < msJsonArray.length(); i++) {
                        JSONObject json_data = msJsonArray.getJSONObject(i);
                        tempMovies.add(new MovieMainInfo(json_data.getString("imdbID"), json_data.getString("Title"), json_data.getString("Year"), "img", "0"));
                       // msArrayList.add(json_data.getString("Title"));
                    }
                    model.setSearchedMovies(tempMovies);
                    //populateListView(true);

            } catch (JSONException e) {
                e.printStackTrace();
               // Log.v(TAG, "ERROR NO JSONARRAY");
            }

        }

    }
}
package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rocali.movieclub.Controllers.MovieSelected;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        new searchMovieThread().execute(url);
        return new Movie();
    }

    @Override
    public Movie searchMovie(String title) {

        return null;
    }
    class searchMovieThread extends AsyncTask<String, String, String> {
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
                   /* model.setSearchedMovie(
                            msJsonObj.getString("imdbID"),
                            msJsonObj.getString("Title"),
                            msJsonObj.getString("Year"),
                            msJsonObj.getString("Plot"),
                            msJsonObj.getString("Runtime"),
                            msJsonObj.getString("Genre"),
                            msJsonObj.getString("Country"),
                            msJsonObj.getString("imdbVotes"),
                            msJsonObj.getString("imdbRating"),
                            msJsonObj.getString("Poster")
                    );

                    //Refresh the list view
                    populateListView(false);

                    //Call Movie selected activity to show movie details
                    Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                    i.putExtra("movieId", "-1");
                    startActivity(i);*/

            } catch (JSONException e) {
                e.printStackTrace();
                //Log.v(TAG,"ERROR NO JSONARRAY");
            }

        }

    }
}
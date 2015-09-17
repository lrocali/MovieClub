package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rocali on 8/24/15.
 */
public class Model {

    private static final String TAG = "TAG";

    //Singleton Model
    public Movie [] movies;
    public Movie searchedMovie;
    public static Model instance = null;
    public JSONObject movie = null;

    public static Model getInstance(){
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public boolean isNetworkConnectionAvailable(Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    public String getSearchableTitle(String enteredText) {

        String searchableTitle = new String();
        boolean deleteLastChar = false;     //Delete white space in case of last char is space or not
                //enteredText.substring(0, enteredText.length()-1);
        char[] titleChars = enteredText.toCharArray();
        for(int i = 0; i < titleChars.length; i++){
            if(Character.isWhitespace(titleChars[i])){
                if (i == titleChars.length-1)
                    deleteLastChar = true;
                titleChars[i] = '+';
            }
        }

        searchableTitle = String.valueOf(titleChars);
        if (deleteLastChar)
            searchableTitle = searchableTitle.substring(0, searchableTitle.length()-1);
        //Log.v(TAG,searchableTitle);
        return searchableTitle;
    }

    /*
    public void searchMovie(String url) {
        new searchMovieThread().execute(url);
    }

    // Async Task Class
    class searchMovieThread extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);
            Log.v(TAG,"PRE EXECUTE");
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                JsonClass json = new JsonClass();
                movie = json.getJSONFromUrl(f_url[0]);
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            //prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
           // playMusic();
            Log.v(TAG,"POST EXECUTE");
            String title = null;
            try {
                title = movie.getString("Title");
                Log.v(TAG,title);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(TAG,"ERROR NO TITLE");
            }

        }
    }
*/
    public Model () {
        //List of movie

//        JSONObject movie = null;
  //      JsonClass json = new JsonClass();
    //    movie = json.getJSONFromUrl("http://www.omdbapi.com/?t=Fight+Club&y=&plot=short&r=json");


        /*
        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(this)) {
            Log.i(TAG, "Connected.");
            new ProductAsyncTask().execute(this);
        } else {
            Log.v(TAG, "Network not Available!");
        }*/
        //Information based of imdb.com

        movies = new Movie [] {
                new Movie("00",
                        "The Shawshank Redemption",
                        "1994",
                        "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                        "runtime",
                        "genre",
                        "country",
                        "imdb votes",
                        "imdvrating",
                        "0"
                ),
                new Movie("000",
                        "The Godfather",
                        "1972",
                        "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
                        "runtime",
                        "genre",
                        "country",
                        "imdb votes",
                        "imdvrating",
                        "0"
                ),
                new Movie("000",
                        "Schindlers List",
                        "1993",
                        "In Poland during World War II, Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.",
                        "runtime",
                        "genre",
                        "country",
                        "imdb votes",
                        "imdvrating",
                        "0"
                ),
                new Movie("000",
                        "Fight Club",
                        "1999",
                        "An insomniac office worker, looking for a way to change his life, crosses paths with a devil-may-care soap maker, forming an underground fight club that evolves into something much, much more.",
                        "runtime",
                        "genre",
                        "country",
                        "imdb votes",
                        "imdvrating",
                        "0"
                ),
                new Movie("000",
                        "Inception",
                        "2010",
                        "A thief who steals corporate secrets through use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
                        "runtime",
                        "genre",
                        "country",
                        "imdb votes",
                        "imdvrating",
                        "0"
                ),
                new Movie("000",
                        "City Of God",
                        "2002",
                        "Two boys growing up in a violent neighborhood of Rio de Janeiro take different paths: one becomes a photographer, the other a drug dealer.",
                        "runtime",
                        "genre",
                        "country",
                        "imdb votes",
                        "imdvrating",
                        "0"
                ),
                new Movie("000",
                        "Interstellar",
                        "2014",
                        "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                        "runtime",
                        "genre",
                        "country",
                        "imdb votes",
                        "imdvrating",
                        "0"
                )
        };

//*
        //Simulating pre-schedulled parties
        movies[2].invitees.add("john@hotmail.com");
        movies[2].invitees.add("katie@hotmail.com");
        movies[2].invitees.add("charlie@hotmail.com");
        movies[2].invitees.add("jack@hotmail.com");
        movies[2].invitees.add("lucke@hotmail.com");
        movies[2].date = "11/10/15";
        movies[2].time = "08:00PM";
        movies[2].venue = "Restaurant";
        movies[2].location = "13 Queen St, Melbourne";
        movies[2].rating = 4;
        movies[2].scheduled = true;

        movies[4].invitees.add("jackson@hotmail.com");
        movies[4].invitees.add("hurley@hotmail.com");
        movies[4].invitees.add("claire@hotmail.com");
        movies[4].date = "10/15/15";
        movies[4].time = "10:00PM";
        movies[4].venue = "Cinema";
        movies[4].location = "321 King St, Melbourne";
        movies[4].rating = 2.5f;
        movies[4].scheduled = true;


    }

    public void setSearchedMovie(String _id,String _title, String _year, String _plot,String _runtime,String _genre,String _country,String _imdbVotes,String _imdbRating,String _imgURL){
        searchedMovie = new Movie(_id,_title,_year,_plot,_runtime,_genre,_country,_imdbVotes,_imdbRating,_imgURL);

    }

    /*
    //Get array of int of image resources
    public int [] getImgResources(Context context) {
        int numOfMovies = movies.length;
        int [] imgs = new int[numOfMovies];
        int i;
        for(i = 0; i<numOfMovies; i++) {
            Resources res = context.getResources();
            String mDrawableName = movies[i].imgSrc;
            imgs[i]  = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
        }
        return  imgs;
    }
    */
}

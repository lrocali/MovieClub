package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rocali on 8/24/15.
 */
public class Model {

    private static final String TAG = "TAG";

    //Singleton Model
    public ArrayList<Movie> movies;
    //public Movie [] movies;
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

    /*
     lblTitle.setText(model.searchedMovie.getTitle());
            lblYear.setText("Year: "+model.searchedMovie.getYear());
            lblGenre.setText("Genre: "+model.searchedMovie.getGenre());
            lblRuntime.setText("Runtime: "+model.searchedMovie.getRuntime());
            lblCountry.setText("Country: "+model.searchedMovie.getCountry());
            lblVotes.setText("IMDB Votes: "+model.searchedMovie.getImdbVotes());
            lblRating.setText("IMDB Rating: "+model.searchedMovie.getImdbRating());
            lblPlot.setText(model.searchedMovie.getPlot());

            new DownloadImageTask(imgPoster)
                    .execute(model.searchedMovie.getImgURL());
     */
    /*
    public String getMovieTitle(int index){
        return movies.get(index).getTitle();
    }
    public String getMovieYear(int index){
        return movies.get(index).getYear();
    }
    public String getMoviePlot(int index){
        return movies.get(index).getPlot();
    }
    public String getMovieRuntime(int index){
        return movies.get(index).getRuntime();
    }
    public String getMovieGenre(int index){
        return movies.get(index).getGenre();
    }
    public String getMovieCountry(int index){
        return movies.get(index).getCountry();
    }
    public String getMovieVotes(int index){
        return movies.get(index).getImdbVotes();
    }
    public String getMovieRating(int index){
        return movies.get(index).getImdbRating();
    }
    public String getMovieImgURL(int index){
        return movies.get(index).getImgURL();
    }
    */
    public Model () {
        //List of movie

        movies = new ArrayList<Movie>();


       // Movie m = new Movie("a","a","a","a","a","a","a","a","a","a");
        //movies.add(m);

/*
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

*/
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

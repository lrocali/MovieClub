package com.example.rocali.movieclub;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by rocali on 8/24/15.
 */
public class Model {

    private static final String TAG = "MyActivity";
    public Movie [] movies;

    public static Model instance = null;

    public static Model getInstance(){
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }


    public Model () {
        movies = new Movie [] {
                new Movie("The Shawshank Redemption",
                        "1994",
                        "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                        "full full plot1"),
                new Movie("The Godfather",
                        "1972",
                        "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
                        "full full plot2"),
                new Movie("Schindlers List",
                        "1993",
                        "In Poland during World War II, Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.",
                        "full full plot2"),
                new Movie("Fight Club",
                        "1999",
                        "An insomniac office worker, looking for a way to change his life, crosses paths with a devil-may-care soap maker, forming an underground fight club that evolves into something much, much more.",
                        "full full plot2"),
                new Movie("Inception",
                        "2010",
                        "A thief who steals corporate secrets through use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
                        "full full plot2"),
                new Movie("City Of God",
                        "2002",
                        "Two boys growing up in a violent neighborhood of Rio de Janeiro take different paths: one becomes a photographer, the other a drug dealer.",
                        "full full plot2"),
                new Movie("Interstellar",
                        "2014",
                        "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                        "full full plot2")
        };
        movies[0].invitees.add("a");
        movies[0].invitees.add("ab");
        movies[0].invitees.add("ac");
        movies[0].invitees.add("acd");
        movies[0].invitees.add("acc");
        movies[0].date = "10/10/10";
        movies[0].time = "08:15PM";
        movies[0].venue = "Restaurant";
        movies[0].location = "12 Bol St";
        movies[0].rating = 4;
        movies[0].scheduled = true;

    }

    public String [] getMovieTitles() {
        int numOfMovies = movies.length;
        String [] titles = new String[numOfMovies];
        int i;
        for(i = 0; i<numOfMovies; i++) {
            titles[i] = movies[i].title;
            Log.v(TAG, movies[i].imgSrc);
        }
        return  titles;
    }

    public String [] getMovieYears() {
        int numOfMovies = movies.length;
        String [] years = new String[numOfMovies];
        int i;
        for(i = 0; i<numOfMovies; i++) {
            years[i] = movies[i].year;
            //Log.v(TAG, movies[i].imgSrc);
        }
        return  years;
    }

    public String [] getMovieShortPlots() {
        int numOfMovies = movies.length;
        String [] shortPlots = new String[numOfMovies];
        int i;
        for(i = 0; i<numOfMovies; i++) {
            shortPlots[i] = movies[i].shortPlot;
            //Log.v(TAG, movies[i].imgSrc);
        }
        return  shortPlots;
    }

    public int [] getImgResources(Context context) {
        int numOfMovies = movies.length;
        int [] imgs = new int[numOfMovies];
        int i;
        for(i = 0; i<numOfMovies; i++) {
            Resources res = context.getResources();
            String mDrawableName = movies[i].imgSrc;
            imgs[i]  = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
            //imgs[i] = getResources().getIdentifier(<resource name>, movies[i].title, getPackageName());
            Log.v(TAG, movies[i].imgSrc);
        }
        return  imgs;
    }
}

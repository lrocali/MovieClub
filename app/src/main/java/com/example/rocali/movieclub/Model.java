package com.example.rocali.movieclub;

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
                new Movie("The Shawshank Redemption","1994","Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.","full full plot1"),
                new Movie("The Godfather","1972","The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.","full full plot2"),
                new Movie("Schindlers List","1993","In Poland during World War II, Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.","full full plot2"),
                new Movie("Fight Club","1999","An insomniac office worker, looking for a way to change his life, crosses paths with a devil-may-care soap maker, forming an underground fight club that evolves into something much, much more.","full full plot2"),
                new Movie("Inception","2010","A thief who steals corporate secrets through use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.","full full plot2"),
                new Movie("City Of God","2002","Two boys growing up in a violent neighborhood of Rio de Janeiro take different paths: one becomes a photographer, the other a drug dealer.","full full plot2"),
                new Movie("Interstellar","2014","A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.","full full plot2")
        };
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
}

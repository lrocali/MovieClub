package com.example.rocali.movieclub;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MovieList extends ListActivity {

    private static final String TAG = "MyActivity";
    public Model model = Model.getInstance();
    String[] movieTitles = model.getMovieTitles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);


        // use your custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movieTitles);
        setListAdapter(adapter);

        ListView lv = getListView();

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                // sending data to new activity
                i.putExtra("movieId", position + "");
                startActivity(i);

            }
        });

    }
}

package com.example.rocali.movieclub;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MovieList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
    }
    @Override
    public void onResume() {
        super.onResume();
        //populate list view
        ListView lv = getListView();
        lv.setAdapter(new CustomAdapter(this));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), MovieSelected.class);
                // sending movie id to new activity
                i.putExtra("movieId", position + "");
                startActivity(i);

            }
        });
    }


}

package com.example.rocali.movieclub.Controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rocali.movieclub.Models.Model;
import com.example.rocali.movieclub.R;
import com.firebase.client.Firebase;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by rocali on 8/24/15.
 */
public class MovieSelected extends Activity {

    //movie attributes
    private int movieId;
    public Model model;
    private static final String TAG = "MyActivity";

    //to date and time field
    final Calendar c = Calendar.getInstance();

    //non editable attributes
    private TextView lblTitle;
    private TextView lblYear;
    private TextView lblGenre;
    private TextView lblRuntime;
    private TextView lblCountry;
    private TextView lblVotes;
    private TextView lblRating;
    private TextView lblPlot;


    private ImageView imgPoster;

    //editable atributtes
    private RatingBar rtbRating;
    private EditText edtDate;
    private EditText edtTime;
    private EditText edtVenue;
    private EditText edtLocation;
    private EditText edtInvited;

    //buttons
    //private Button btnEditMovie;
    private Button btnAddInvited;
    private Button btnParty;

    //iniviteeslist atributtes
    private List<String> inviteesList;
    private ListView listInvitees;
    private ArrayAdapter<String> adapter;

    private boolean editState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        model = Model.getInstance(this);
        //initial
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.selected_movie);

        //get intent to know movieid
        Intent i = getIntent();
        String movieIdStr = i.getStringExtra("movieId");
        movieId = Integer.parseInt(movieIdStr);

        //find noneditables
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblYear = (TextView) findViewById(R.id.lblYear);
        lblGenre = (TextView) findViewById(R.id.lblGenre);
        lblRuntime = (TextView) findViewById(R.id.lblRuntime);
        lblCountry = (TextView) findViewById(R.id.lblCountry);
        lblVotes = (TextView) findViewById(R.id.lblVotes);
        lblRating = (TextView) findViewById(R.id.lblRating);

        lblPlot = (TextView) findViewById(R.id.lblPlot);
        imgPoster = (ImageView) findViewById(R.id.imgPoster);
        rtbRating = (RatingBar) findViewById(R.id.movieRatingBar);

        //find editables
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtTime = (EditText) findViewById(R.id.edtTime);
        edtVenue = (EditText) findViewById(R.id.edtVenue);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        edtInvited = (EditText) findViewById(R.id.edtInvited);
        ///btnEditMovie = (Button) findViewById(R.id.btnEditMovie);
        btnAddInvited = (Button) findViewById(R.id.btnAddInvited);
        btnParty = (Button) findViewById((R.id.btnParty));


        if (movieId >= 0) {
            //hide party button if they will create the event
            if (!model.movies.get(movieId).isScheduled())
                btnParty.setVisibility(View.INVISIBLE);

            //disableEditables
            enableEditables(false);

            //set noneditable information
            lblTitle.setText(model.movies.get(movieId).getTitle());
            lblYear.setText("Year: "+model.movies.get(movieId).getYear());
            lblGenre.setText("Genre: "+model.movies.get(movieId).getGenre());
            lblRuntime.setText("Runtime: "+model.movies.get(movieId).getRuntime());
            lblCountry.setText("Country: " + model.movies.get(movieId).getCountry());
            lblVotes.setText("IMDB Votes: "+model.movies.get(movieId).getImdbVotes());
            lblRating.setText("IMDB Rating: "+model.movies.get(movieId).getImdbRating());
            lblPlot.setText(model.movies.get(movieId).getPlot());

            rtbRating.setRating(model.movies.get(movieId).getRating());

            // -------------
            new DownloadImageTask(imgPoster).execute(model.movies.get(movieId).getImgURL());

            //set editable/party information if it has created
            if (model.movies.get(movieId).isScheduled()) {
                edtDate.setText(model.movies.get(movieId).getParty().getDate());
                edtTime.setText(model.movies.get(movieId).getParty().getTime());
                edtVenue.setText(model.movies.get(movieId).getParty().getVenue());
                edtLocation.setText(model.movies.get(movieId).getParty().getLocation());
            }

            //Set image
           // Resources res = getResources();
            //String mDrawableName = model.movies[movieId].imgSrc;
            //int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
           // Drawable drawable = res.getDrawable(resID);
            //imgPoster.setImageDrawable(drawable);

            //set invitees list
            getListElements();

/*
            //if the movie has no scheduled party
            if (!model.movies.get(movieId).isScheduled()) {
                Toast.makeText(getApplicationContext(), "Create an event", Toast.LENGTH_SHORT).show();
            }
*/
            //add invite button
            btnAddInvited.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), edtInvited.getText().toString(), Toast.LENGTH_SHORT).show();

                    model.movies.get(movieId).getParty().addInvitees(edtInvited.getText().toString());
                    //get/refresh list of invitees
                    getListElements();
                    //if all the field had been entered the party button is visible
                    if (edtDate.getText().toString() != null &&
                            edtTime.getText().toString() != null &&
                            edtVenue.getText().toString() != null &&
                            edtLocation.getText().toString() != null) {

                        btnParty.setVisibility(View.VISIBLE);
                    }
                    edtInvited.setText("");
                }
            });

            //party/create event button
            btnParty.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //set information on the model
                    model.movies.get(movieId).getParty().setDate(edtDate.getText().toString());
                    model.movies.get(movieId).getParty().setTime(edtTime.getText().toString());
                    model.movies.get(movieId).getParty().setVenue(edtVenue.getText().toString());
                    model.movies.get(movieId).getParty().setLocation(edtLocation.getText().toString());

                    model.updateMovieParty(movieId);
                    //disable editables
                    enableEditables(false);

                    //change name of the button and it state
                    //etlt.makeText(getApplicationContext(), "Edit event", Toast.LENGTH_SHORT).show();
                    editState = false;
                }
            });

            //set action to rating bar
            rtbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    model.movies.get(movieId).setRating(rating);
                    model.updateRatingMovie(movieId);

                }
            });
        } else {
            //disableEditables
            enableEditables(false);

            lblTitle.setText(model.searchedMovie.getTitle());
            lblYear.setText("Year: "+model.searchedMovie.getYear());
            lblGenre.setText("Genre: "+model.searchedMovie.getGenre());
            lblRuntime.setText("Runtime: "+model.searchedMovie.getRuntime());
            lblCountry.setText("Country: "+model.searchedMovie.getCountry());
            lblVotes.setText("IMDB Votes: "+model.searchedMovie.getImdbVotes());
            lblRating.setText("IMDB Rating: "+model.searchedMovie.getImdbRating());
            lblPlot.setText(model.searchedMovie.getPlot());

           // -------------
            new DownloadImageTask(imgPoster).execute(model.searchedMovie.getImgURL());
        }
    }


/*
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/
    //Function to enable or disable the editable atributtes
    public void enableEditables(boolean trueOrFalse){
        edtDate.setEnabled(trueOrFalse);
        edtTime.setEnabled(trueOrFalse);
        edtVenue.setEnabled(trueOrFalse);
        edtLocation.setEnabled(trueOrFalse);
        edtInvited.setEnabled(trueOrFalse);
        btnAddInvited.setEnabled(trueOrFalse);
    }

    //Function to get/refresh the list of invitess when a new invited is added
    public void getListElements() {
        inviteesList = model.movies.get(movieId).getParty().getInvitees();
        if (inviteesList != null) {
            listInvitees = (ListView) findViewById(R.id.listInvitees);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inviteesList);

            listInvitees.setAdapter(adapter);

            //Set list view height
            ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams) listInvitees.getLayoutParams();
            listViewParams.height = inviteesList.size() * 150;
            listInvitees.requestLayout();
        }
    }



    DatePickerDialog.OnDateSetListener date
        = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
            c.set( Calendar.YEAR, year );
            c.set( Calendar.MONTH, monthOfYear );
            c.set( Calendar.DAY_OF_MONTH, dayOfMonth );
            setCurrentDateOnView();
        }
    };
    public void dateOnClicked(View view) {
        new DatePickerDialog(MovieSelected.this,date,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
    }

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet( TimePicker view, int hourOfDay, int minute ) {
            c.set( Calendar.HOUR_OF_DAY, hourOfDay );
            c.set( Calendar.MINUTE, minute );
            setCurrentTimeOnView();
        }
    };
    public void timeOnClicked( View view ) {
        new TimePickerDialog( MovieSelected.this, time,
                c.get( Calendar.HOUR ), c.get( Calendar.MINUTE ), false ).show();
    }

    //Set current information on date filed, just a default value
    public void setCurrentDateOnView() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, Locale.US);
        edtDate.setText(sdf.format(c.getTime()));
    }

    //Set current information on time filed, just a default value
    public void setCurrentTimeOnView() {
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat( timeFormat, Locale.US );
        edtTime.setText(stf.format(c.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_selected, menu);

        if (movieId < 0) {
            MenuItem item = menu.findItem(R.id.action_edit_event);
            item.setIcon(R.drawable.ic_addmovie);
            item.setTitle("Add Movie");
        }


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        //Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
        //if on edit state
        if (item.getItemId()== R.id.action_edit_event) {
            if (movieId >= 0) {
                if (!editState) {
                    enableEditables(true);
                    Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    editState = true;
                    model.movies.get(movieId).setScheduled(true);
                } else {

                    enableEditables(false);
                    if (!model.movies.get(movieId).isScheduled()) {
                        Toast.makeText(getApplicationContext(), "Create event", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Edit event", Toast.LENGTH_SHORT).show();
                    }
                    editState = false;
                }
            } else {
                Toast.makeText(getApplicationContext(), "Add Movie", Toast.LENGTH_SHORT).show();
                //model.addSearchedMovie();
                model.insertSearchedMovieIntoDatabase();
                //this.finish();
                //Intent i = new Intent(getApplicationContext(), MovieList.class);
                //startActivity(i);
                //Intent i = new Intent(getApplicationContext(), MovieList.class);
                //i.putExtra("movieId", position + "");
                //startActivity(i);
                finish();
            }

        }
        return true;

    }
}

package com.example.rocali.movieclub.Controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.example.rocali.movieclub.Models.Movie;
import com.example.rocali.movieclub.Models.Party;
import com.example.rocali.movieclub.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by rocali on 8/24/15.
 */
public class MovieSelected extends Activity {

    //movie attributes
    private String imdbID;
    private String state = "x"; //Selecting or Searching
    //private int movieId = 0;
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
    private ArrayList<String> inviteesList;
    private ListView listViewInvitees;
    private ArrayAdapter<String> adapter;

    //To recognize if the user clicked once on edit move (to edit) or two (to cancel)
    private boolean editState = false;

    //Movie model.movie;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String source = intent.getStringExtra("source");
            // Log.d("receiver", "Got message: " + message);
            Log.v(model.TAG, "MOVIE GETTED FROM " + source + "NO IS JUST REFRESH INFO");
            setMovieInfo();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        model = Model.getInstance(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("refreshMovieSelected"));

        inviteesList = new ArrayList<String>(){};

        //initial
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.selected_movie);

        //get intent to know movieid
        Intent i = getIntent();
        imdbID = i.getStringExtra("imdbID");
        state = i.getStringExtra("state");


        model.getMovie(imdbID);
        //Get Movie from DB
        //0 means the movie has alreaday been fetched and is on the model
       // if (!imdbID.equals("0")) {
        //    model.getMovie(imdbID);
        //} else {
         //   imdbID = model.getMovie().getId();
       // }

        //String title = model.getMovie().getTitle();
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

        btnAddInvited = (Button) findViewById(R.id.btnAddInvited);
        btnParty = (Button) findViewById((R.id.btnParty));


        if (!state.equals("searching")) {
            setPartyInfo();

            enableEditables(false);

            setMovieInfo();
            /*
            //set noneditable information
            lblTitle.setText(model.getMovie().getTitle());
            lblYear.setText("Year: "+ model.getMovie().getYear());
            lblGenre.setText("Genre: "+ model.getMovie().getGenre());
            lblRuntime.setText("Runtime: "+ model.getMovie().getRuntime());
            lblCountry.setText("Country: " + model.getMovie().getCountry());
            lblVotes.setText("IMDB Votes: "+ model.getMovie().getImdbVotes());
            lblRating.setText("IMDB Rating: "+ model.getMovie().getImdbRating());
            lblPlot.setText(model.getMovie().getPlot());

            rtbRating.setRating(model.getMovie().getRating());

            new DownloadImageTask(imgPoster).execute(model.getMovie().getImgURL());
*/
            getListElements();

            btnAddInvited.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), edtInvited.getText().toString(), Toast.LENGTH_SHORT).show();

                    inviteesList.add(edtInvited.getText().toString());

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

                    Party newparty = new Party(model.getMovie().getId(),edtDate.getText().toString(),edtTime.getText().toString(),edtVenue.getText().toString(),edtLocation.getText().toString(),inviteesList);
                    model.addPartyToCloud(newparty);

                    enableEditables(false);

                    editState = false;
                }
            });

            //set action to rating bar
            rtbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    model.getMovie().setRating(rating);
                    model.updateMovie();

                }
            });
        } else {
            //disableEditables
            btnParty.setVisibility(View.INVISIBLE);
            enableEditables(false);

            lblTitle.setText(model.getMovie().getTitle());
            lblYear.setText("Year: "+model.getMovie().getYear());
            lblGenre.setText("Genre: "+model.getMovie().getGenre());
            lblRuntime.setText("Runtime: "+model.getMovie().getRuntime());
            lblCountry.setText("Country: "+model.getMovie().getCountry());
            lblVotes.setText("IMDB Votes: "+model.getMovie().getImdbVotes());
            lblRating.setText("IMDB Rating: "+model.getMovie().getImdbRating());
            lblPlot.setText(model.getMovie().getPlot());

            new DownloadImageTask(imgPoster).execute(model.getMovie().getImgURL());
        }
    }

    public void setMovieInfo(){
        //set noneditable information
        lblTitle.setText(model.getMovie().getTitle());
        lblYear.setText("Year: "+ model.getMovie().getYear());
        lblGenre.setText("Genre: "+ model.getMovie().getGenre());
        lblRuntime.setText("Runtime: "+ model.getMovie().getRuntime());
        lblCountry.setText("Country: " + model.getMovie().getCountry());
        lblVotes.setText("IMDB Votes: "+ model.getMovie().getImdbVotes());
        lblRating.setText("IMDB Rating: "+ model.getMovie().getImdbRating());
        lblPlot.setText(model.getMovie().getPlot());

        rtbRating.setRating(model.getMovie().getRating());
        new DownloadImageTask(imgPoster).execute(model.getMovie().getImgURL());
    }

    public void setPartyInfo(){
        Party party = model.checkForParty(imdbID);
        if(party != null) {
            edtDate.setText(party.getDate());
            edtTime.setText(party.getTime());
            edtVenue.setText(party.getVenue());
            edtLocation.setText(party.getLocation());

            inviteesList = party.getInvitees();
            getListElements();

            Toast.makeText(getApplicationContext(), "THERE IS A PARTY FOR THIS MOVIE", Toast.LENGTH_SHORT).show();
        }
    }

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
        //inviteesList = null; // model.getMovie().getParty().getInvitees();
        if (inviteesList != null) {
            listViewInvitees = (ListView) findViewById(R.id.listInvitees);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inviteesList);

            listViewInvitees.setAdapter(adapter);

            //Set list view height
            ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams) listViewInvitees.getLayoutParams();
            listViewParams.height = inviteesList.size() * 150;
            listViewInvitees.requestLayout();
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

        if (state.equals("searching")) {
            MenuItem item = menu.findItem(R.id.action_edit_event);
            item.setIcon(R.drawable.ic_addmovie);
            item.setTitle("Add Movie");
        }


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Clicked Edit/Add event button
        if (item.getItemId()== R.id.action_edit_event) {
            //If selected a movie from DB
            if (!state.equals("searching")) {
                //First click (to edit)
                if (!editState) {
                    enableEditables(true);
                    if (!model.hasParty(imdbID)) {
                        Toast.makeText(getApplicationContext(), "Create event", Toast.LENGTH_SHORT).show();
                        inviteesList.add("User 0");
                        getListElements();
                    } else {
                        Toast.makeText(getApplicationContext(), "Edit event", Toast.LENGTH_SHORT).show();
                    }
                    editState = true;
                }
                //Second click (to cancel)
                else {
                    Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    enableEditables(false);

                    editState = false;
                }
            }
            //If selected movie from Search
            else {
                Toast.makeText(getApplicationContext(), "Add Movie", Toast.LENGTH_SHORT).show();
                model.insertMovie();
                finish();
            }

        }
        return true;

    }
}

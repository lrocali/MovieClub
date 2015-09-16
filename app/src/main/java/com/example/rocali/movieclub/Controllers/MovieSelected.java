package com.example.rocali.movieclub.Controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.rocali.movieclub.Models.Model;
import com.example.rocali.movieclub.R;

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
    public Model model = Model.getInstance();
    private static final String TAG = "MyActivity";

    //to date and time field
    final Calendar c = Calendar.getInstance();

    //non editable attributes
    private TextView lblTitle;
    private TextView lblYear;
    private TextView lblFullPlot;
    private ImageView imgPoster;

    //editable atributtes
    private RatingBar rtbRating;
    private EditText edtDate;
    private EditText edtTime;
    private EditText edtVenue;
    private EditText edtLocation;
    private EditText edtInvited;

    //buttons
    private Button btnEditMovie;
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
        lblFullPlot = (TextView) findViewById(R.id.lblPlot);
        imgPoster = (ImageView) findViewById(R.id.imgPoster);
        rtbRating = (RatingBar) findViewById(R.id.movieRatingBar);

        //find editables
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtTime = (EditText) findViewById(R.id.edtTime);
        edtVenue = (EditText) findViewById(R.id.edtVenue);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        edtInvited = (EditText) findViewById(R.id.edtInvited);
        btnEditMovie = (Button) findViewById(R.id.btnEditMovie);
        btnAddInvited = (Button) findViewById(R.id.btnAddInvited);
        btnParty = (Button) findViewById((R.id.btnParty));

        //hide party button if they will create the event
        if (!model.movies[movieId].scheduled)
            btnParty.setVisibility(View.INVISIBLE);

        //disableEditables
        enableEditables(false);

        //set noneditable information
        lblTitle.setText(model.movies[movieId].title);
        lblYear.setText(model.movies[movieId].year);
        lblFullPlot.setText(model.movies[movieId].fullPlot);
        rtbRating.setRating(model.movies[movieId].rating);

        //set editable/party information if it has created
        if (model.movies[movieId].scheduled) {
            edtDate.setText(model.movies[movieId].date);
            edtTime.setText(model.movies[movieId].time);
            edtVenue.setText(model.movies[movieId].venue);
            edtLocation.setText(model.movies[movieId].location);
        }

        //Set image
        Resources res = getResources();
        String mDrawableName = model.movies[movieId].imgSrc;
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID);
        imgPoster.setImageDrawable(drawable);

        //set invitees list
        getListElements();

        //if the movie has no scheduled party
        if (!model.movies[movieId].scheduled) {
            btnEditMovie.setText("Create an event");
        }

        //edit movie button
        btnEditMovie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if on edit state
                if (!editState) {
                    enableEditables(true);
                    btnEditMovie.setText("Cancel");
                    editState = true;
                    model.movies[movieId].scheduled = true;
                } else {
                    enableEditables(false);
                    btnEditMovie.setText("Edit");
                    editState = false;
                }
            }
        });

        //add invite button
        btnAddInvited.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.movies[movieId].invitees.add(edtInvited.getText().toString());
                //get/refresh list of invitees
                getListElements();
                //if all the field had been entered the party button is visible
                if (edtDate.getText().toString() != null &&
                        edtTime.getText().toString() != null &&
                        edtVenue.getText().toString() != null &&
                        edtLocation.getText().toString() != null) {

                    btnParty.setVisibility(View.VISIBLE);
                }
            }
        });

        //party/create event button
        btnParty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //set information on the model
                model.movies[movieId].date = edtDate.getText().toString();
                model.movies[movieId].time = edtTime.getText().toString();
                model.movies[movieId].venue = edtVenue.getText().toString();
                model.movies[movieId].location = edtLocation.getText().toString();

                //disable editables
                enableEditables(false);

                //change name of the button and it state
                btnEditMovie.setText("Edit");
                editState = false;
            }
        });

        //set action to rating bar
        rtbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.movies[movieId].rating = rating;

            }
        });
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
        inviteesList = model.movies[movieId].invitees;
        listInvitees = (ListView) findViewById(R.id.listInvitees);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inviteesList);
        listInvitees.setAdapter(adapter);

        //Set list view height
        ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)listInvitees.getLayoutParams();
        listViewParams.height = inviteesList.size()*150;
        listInvitees.requestLayout();
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
        edtDate.setText(sdf.format( c.getTime()));
    }

    //Set current information on time filed, just a default value
    public void setCurrentTimeOnView() {
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat( timeFormat, Locale.US );
        edtTime.setText(stf.format(c.getTime()));
    }

}

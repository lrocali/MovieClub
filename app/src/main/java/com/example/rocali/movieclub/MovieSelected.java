package com.example.rocali.movieclub;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by rocali on 8/24/15.
 */
public class MovieSelected extends Activity implements AppCompatCallback{

    private static final String TAG = "MyActivity";
    private int movieId;
    public Model model = Model.getInstance();
    final Calendar c = Calendar.getInstance();

    private TextView lblTitle;
    private TextView lblYear;
    private TextView lblshortPlot;
    private ImageView imgPoster;
    private RatingBar rtbRating;

    private EditText edtDate;
    private EditText edtTime;
    private EditText edtVenue;
    private EditText edtLocation;
    private EditText edtInvited;
    private Button btnEditMovie;
    private Button btnAddInvited;
    private Button btnParty;


    private List<String> list;
    private ListView listInvitees;
    private ArrayAdapter<String> adapter;

    private boolean editState = false;

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
    }

    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
    private AppCompatDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.selected_movie);

        Intent i = getIntent();
        // getting attached intent data
        String movieIdStr = i.getStringExtra("movieId");
        movieId = Integer.parseInt(movieIdStr);

        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblYear = (TextView) findViewById(R.id.lblYear);
        lblshortPlot = (TextView) findViewById(R.id.lblPlot);
        imgPoster = (ImageView) findViewById(R.id.imgPoster);
        rtbRating = (RatingBar) findViewById(R.id.movieRatingBar);

        edtDate = (EditText) findViewById(R.id.edtDate);
        edtTime = (EditText) findViewById(R.id.edtTime);
        edtVenue = (EditText) findViewById(R.id.edtVenue);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        edtInvited = (EditText) findViewById(R.id.edtInvited);

        btnEditMovie = (Button) findViewById(R.id.btnEditMovie);
        btnAddInvited = (Button) findViewById(R.id.btnAddInvited);
        btnParty = (Button) findViewById((R.id.btnParty));

        btnParty.setVisibility(View.INVISIBLE);


        enableEditables(false);
        /*
        edtDate.setEnabled(false);
        edtTime.setEnabled(false);
        edtVenue.setEnabled(false);
        edtLocation.setEnabled(false);
        //rtbRating.setEnabled(false);
        edtInvited.setEnabled(false);
        btnAddInvited.setEnabled(false);
*/

        edtDate.setText(model.movies[movieId].date);
        edtTime.setText(model.movies[movieId].time);
        edtVenue.setText(model.movies[movieId].venue);
        edtLocation.setText(model.movies[movieId].location);




       //
       // setListViewHeightBasedOnItems(listInvitees);


        lblTitle.setText(model.movies[movieId].title);
        lblYear.setText(model.movies[movieId].year);
        lblshortPlot.setText(model.movies[movieId].shortPlot);
        rtbRating.setRating(model.movies[movieId].rating);

        Resources res = getResources();
        Log.v(TAG,model.movies[movieId].imgSrc);
        String mDrawableName = model.movies[movieId].imgSrc;
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID);
        imgPoster.setImageDrawable(drawable);
        Log.v(TAG, "0");
        //setCurrentDateOnView();
        getListElements();
/*
        list = model.movies[movieId].invitees;
        listInvitees = (ListView) findViewById(R.id.listInvitees);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listInvitees.setAdapter(adapter);

        //Set list view height
        ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)listInvitees.getLayoutParams();
        listViewParams.height = list.size()*150;
        listInvitees.requestLayout();
*/


        if (!model.movies[movieId].scheduled) {
            btnEditMovie.setText("Create an event");
        }
        btnEditMovie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "EDIT");

                if (!editState) {
                    enableEditables(true);
                    /*
                    edtDate.setEnabled(true);
                    edtTime.setEnabled(true);
                    edtVenue.setEnabled(true);
                    edtLocation.setEnabled(true);
                    edtInvited.setEnabled(true);
                    btnAddInvited.setEnabled(true);
*/
                    btnEditMovie.setText("Cancel");
                    editState = true;
                    model.movies[movieId].scheduled = true;

                } else {
                    enableEditables(false);
                    /*
                    edtDate.setEnabled(false);
                    edtTime.setEnabled(false);
                    edtVenue.setEnabled(false);
                    edtLocation.setEnabled(false);
                    edtInvited.setEnabled(false);
                    btnAddInvited.setEnabled(false);
*/
                    btnEditMovie.setText("Edit");
                    editState = false;
                    /*
                    model.movies[movieId].date = edtDate.getText().toString();
                    model.movies[movieId].time = edtTime.getText().toString();
                    model.movies[movieId].venue = edtVenue.getText().toString();
                    model.movies[movieId].location = edtLocation.getText().toString();
                    Log.v(TAG, model.movies[movieId].date);
                    edtDate.setEnabled(false);
                    edtTime.setEnabled(false);
                    edtVenue.setEnabled(false);
                    edtLocation.setEnabled(false);
                    rtbRating.setEnabled(false);

                    btnEditMovie.setText("Edit");
                    editState = false;
                    */
                }
            }
        });




        btnAddInvited.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.movies[movieId].invitees.add(edtInvited.getText().toString());
                //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
                getListElements();
                if (edtDate.getText().toString() != null &&
                        edtTime.getText().toString() != null &&
                        edtVenue.getText().toString() != null &&
                        edtLocation.getText().toString() != null) {

                    btnParty.setVisibility(View.VISIBLE);
                }
                Log.v(TAG, "ADD" + edtInvited.getText().toString());
            }
        });



        btnParty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.movies[movieId].date = edtDate.getText().toString();
                model.movies[movieId].time = edtTime.getText().toString();
                model.movies[movieId].venue = edtVenue.getText().toString();
                model.movies[movieId].location = edtLocation.getText().toString();
                Log.v(TAG, model.movies[movieId].date);
                enableEditables(false);
                /*
                edtDate.setEnabled(false);
                edtTime.setEnabled(false);
                edtVenue.setEnabled(false);
                edtLocation.setEnabled(false);
                edtInvited.setEnabled(false);
                btnAddInvited.setEnabled(false);
                //rtbRating.setEnabled(false);*/


                btnEditMovie.setText("Edit");
                editState = false;
                Log.v(TAG, "PARTY");
            }
        });

        rtbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.movies[movieId].rating = rating;
                Log.v(TAG, String.valueOf(rating));

            }
        });
    }
    public void enableEditables(boolean trueOrFalse){
        edtDate.setEnabled(trueOrFalse);
        edtTime.setEnabled(trueOrFalse);
        edtVenue.setEnabled(trueOrFalse);
        edtLocation.setEnabled(trueOrFalse);
        edtInvited.setEnabled(trueOrFalse);
        btnAddInvited.setEnabled(trueOrFalse);
    }


    public void getListElements() {
        list = model.movies[movieId].invitees;
        listInvitees = (ListView) findViewById(R.id.listInvitees);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listInvitees.setAdapter(adapter);

        //Set list view height
        ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)listInvitees.getLayoutParams();
        listViewParams.height = list.size()*150;
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
            setCurrentDateOnView();
        }
    };
    public void timeOnClicked( View view ) {
        new TimePickerDialog( MovieSelected.this, time,
                c.get( Calendar.HOUR ), c.get( Calendar.MINUTE ), false ).show();
    }

    public void setCurrentDateOnView() {
        Log.v(TAG," 1");
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, Locale.US);
        edtDate.setText(sdf.format( c.getTime() ) );
        Log.v(TAG, "2");
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat( timeFormat, Locale.US );
        edtTime.setText(stf.format(c.getTime()));
    }

}

package com.example.rocali.movieclub;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by rocali on 8/24/15.
 */
public class MovieSelected extends Activity {

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


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.selected_movie);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Toolbar toolbar = (Toolbar) findViewById(Resoure.id.toolbar);

        //setSupportActionBar(toolbar);

        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblYear = (TextView) findViewById(R.id.lblYear);
        lblshortPlot = (TextView) findViewById(R.id.lblPlot);
        imgPoster = (ImageView) findViewById(R.id.imgPoster);
        rtbRating = (RatingBar) findViewById(R.id.movieRatingBar);
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtTime = (EditText) findViewById(R.id.edtTime);

        Intent i = getIntent();
        // getting attached intent data
        String movieIdStr = i.getStringExtra("movieId");
        // displaying selected product name
        //Log.v(TAG,movieIdStr);

        movieId = Integer.parseInt(movieIdStr);

        lblTitle.setText(model.movies[movieId].title);
        lblYear.setText(model.movies[movieId].year);
        lblshortPlot.setText(model.movies[movieId].shortPlot);
        rtbRating.setRating(model.movies[movieId].rating);

        Resources res = getResources();
        Log.v(TAG,model.movies[movieId].imgSrc);
        String mDrawableName = model.movies[movieId].imgSrc;
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID );
        imgPoster.setImageDrawable(drawable);
        Log.v(TAG, "0");
        //setCurrentDateOnView();

        rtbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.movies[movieId].rating = rating;
                Log.v(TAG, String.valueOf(rating));

            }
        });

/*
        String[] list = new String[] {"a","b","c"};
        ListView lv = (ListView) findViewById(R.id.listInvitees);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
*/



    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
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
        SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, Locale.US );
        edtDate.setText(sdf.format( c.getTime() ) );
        Log.v(TAG, "2");
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat( timeFormat, Locale.US );
        edtTime.setText(stf.format(c.getTime()));
    }

}

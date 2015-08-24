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


        edtDate.setEnabled(false);
        edtTime.setEnabled(false);
        edtVenue.setEnabled(false);
        edtLocation.setEnabled(false);
        rtbRating.setEnabled(false);

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
        final Button btnEditMovie = (Button) findViewById(R.id.btnEditMovie);
        btnEditMovie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "EDIT");

                if (!editState) {
                    edtDate.setEnabled(true);
                    edtTime.setEnabled(true);
                    edtVenue.setEnabled(true);
                    edtLocation.setEnabled(true);
                    rtbRating.setEnabled(true);

                    btnEditMovie.setText("Done");
                    editState = true;

                } else {
                    model.movies[movieId].date = edtDate.getText().toString();
                    model.movies[movieId].time = edtTime.getText().toString() + "TTTT";
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
                }
            }
        });

        final Button btnAddInvited = (Button) findViewById(R.id.btnAddInvited);
        btnAddInvited.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.movies[movieId].invitees.add(edtInvited.getText().toString());
                //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
                getListElements();
                Log.v(TAG, "ADD"+edtInvited.getText().toString());
            }
        });

        rtbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.movies[movieId].rating = rating;
                Log.v(TAG, String.valueOf(rating));

            }
        });
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

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            int itemPos;
            for (itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            Log.v(TAG, "1DEU");
            return true;

        } else {
            Log.v(TAG, "2FALHOU");
            return false;
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

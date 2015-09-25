package com.example.rocali.movieclub.Controllers;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.rocali.movieclub.Models.Model;
import com.example.rocali.movieclub.Models.Party;
import com.example.rocali.movieclub.R;

public class CustomAdapter extends BaseAdapter{

    //model
    public Model model ;

    Context context;    //To be sent to get the img resource


    private static LayoutInflater inflater=null;
    public CustomAdapter(MovieList movieList) {
        // TODO Auto-generated constructor stub

        //Get context to be sent to model to get img resources
        context = movieList;
        model = Model.getInstance(context);
        //movieImgSrcs = model.getImgResources(context);

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return model.movies.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    //Class to hold the elements of the row
    public class RowInfo
    {
        ImageView poster;
        TextView title;
        TextView year;
        RatingBar rating;
        TextView party;
        TextView dateAndTime;
        TextView location;
        TextView nInvitees;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //Creating row info
        RowInfo rowInfo = new RowInfo();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_movie_row, null);

        //Finding elements of the row and keeping on the rowInfo class
        rowInfo.poster = (ImageView) rowView.findViewById(R.id.imgPosterList);
        rowInfo.title = (TextView) rowView.findViewById(R.id.txtTitle);
        rowInfo.year = (TextView) rowView.findViewById(R.id.txtYear);
        rowInfo.party = (TextView) rowView.findViewById(R.id.txtParty);
        rowInfo.dateAndTime = (TextView) rowView.findViewById(R.id.txtDateTime);
        rowInfo.location = (TextView) rowView.findViewById(R.id.txtLocation);
        rowInfo.nInvitees = (TextView) rowView.findViewById(R.id.txtNInvitees);
        rowInfo.rating = (RatingBar) rowView.findViewById(R.id.rtbListMovie);

        //Seting information of the movie (never change)
       // rowInfo.poster.setImageResource(movieImgSrcs[position]);
        new DownloadImageTask(rowInfo.poster).execute(model.movies.get(position).getImgURL());
        rowInfo.title.setText(model.movies.get(position).getTitle());
        rowInfo.year.setText(model.movies.get(position).getYear());
        //rowInfo.shortPlot.setText(model.movies.get(position).getPlot());
        //rowInfo.shortPlot.setText(" ");

        //Set ratting in case of the user had ratted the movie before
        if (model.movies.get(position).getRating() != 0) {
            rowInfo.rating.setRating(model.movies.get(position).getRating());
        }

        //Check Party
        Party party = model.checkForParty(model.movies.get(position).getId());

        //If the function return a party
        if (party != null) {
            rowInfo.dateAndTime.setVisibility(View.VISIBLE);
            rowInfo.location.setVisibility(View.VISIBLE);
            rowInfo.nInvitees.setVisibility(View.VISIBLE);
            rowInfo.dateAndTime.setText(party.getDate() + " - " + party.getTime());
            rowInfo.location.setText("At " + party.getLocation());
            int nInv = party.getInvitees().size();
            rowInfo.nInvitees.setText("Invitees: " + String.valueOf(nInv));

        }
        //If there is no party
        else {
            //In case of there is no schedulled party for the movie
            rowInfo.party.setText("No party yet");
            rowInfo.dateAndTime.setVisibility(View.GONE);
            rowInfo.location.setVisibility(View.GONE);
            rowInfo.nInvitees.setVisibility(View.GONE);

        }

        /*
        if (model.movies.get(position).isScheduled()) {
            rowInfo.dateAndTime.setText(model.movies.get(position).getParty().getDate() + " - " + model.movies.get(position).getParty().getTime());
            rowInfo.location.setText("At " + model.movies.get(position).getParty().getLocation());
            int nInv = model.movies.get(position).getParty().getInvitees().size();
            rowInfo.nInvitees.setText("Invitees: " + String.valueOf(nInv));

        } else {*/
            //In case of there is no schedulled party for the movie


        //}

        //If the user click on the row, its called the new view with the movie details
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(context.getApplicationContext(), MovieSelected.class);
                i.putExtra("imdbID", model.movies.get(position).getId());
                i.putExtra("state","not searching");
                context.startActivity(i);
            }
        });

        //set action to rating bar
        rowInfo.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.movies.get(position).setRating(rating);
                model.updateMovie(position);
                //model.updateRatingMovie(position);


            }
        });
        return rowView;
    }

}
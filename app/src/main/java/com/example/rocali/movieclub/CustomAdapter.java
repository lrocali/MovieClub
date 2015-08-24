package com.example.rocali.movieclub;
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

public class CustomAdapter extends BaseAdapter{
    public Model model = Model.getInstance();

    int [] movieImgSrcs;

    Context context;

    private static LayoutInflater inflater=null;
    public CustomAdapter(MovieList movieList) {
        // TODO Auto-generated constructor stub
        context=movieList;

        movieImgSrcs =model.getImgResources(context);
        //movieTitles=model.getMovieTitles();
        //movieYears = model.getMovieYears();
        //moviewShortPlots = model.getMovieShortPlots();

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return model.movies.length;
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

    public class Holder
    {
        ImageView poster;
        TextView title;
        TextView year;
        TextView shortPlot;
        TextView dateAndTime;
        TextView location;
        TextView nInvitees;
        RatingBar rating;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_movie_row, null);

        holder.poster = (ImageView) rowView.findViewById(R.id.imgPosterList);
        holder.title = (TextView) rowView.findViewById(R.id.txtTitle);
        holder.year = (TextView) rowView.findViewById(R.id.txtYear);
        holder.shortPlot = (TextView) rowView.findViewById(R.id.txtShortPlot);
        holder.dateAndTime = (TextView) rowView.findViewById(R.id.txtDateTime);
        holder.location = (TextView) rowView.findViewById(R.id.txtLocation);
        holder.nInvitees = (TextView) rowView.findViewById(R.id.txtNInvitees);
        holder.rating = (RatingBar) rowView.findViewById(R.id.rtbListMovie);

        holder.poster.setImageResource(movieImgSrcs[position]);
        holder.title.setText(model.movies[position].title);
        holder.year.setText(model.movies[position].year);
        holder.shortPlot.setText(model.movies[position].shortPlot);
        if (model.movies[position].rating != 0) {
            holder.rating.setRating(model.movies[position].rating);
        }
        if (model.movies[position].scheduled == true) {
            holder.dateAndTime.setText(model.movies[position].date + " - " + model.movies[position].time);
            holder.location.setText("At " + model.movies[position].location);
            int nInv = model.movies[position].invitees.size();
            holder.nInvitees.setText("Invitees: " + String.valueOf(nInv));

        } else {
            holder.dateAndTime.setText("Movie not scheduled yet, be the first!");
            holder.location.setText(" ");
            holder.nInvitees.setText(" ");

        }

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context.getApplicationContext(), MovieSelected.class);
                // sending data to new activity
                i.putExtra("movieId", position + "");
                context.startActivity(i);
            }
        });
        return rowView;
    }

}
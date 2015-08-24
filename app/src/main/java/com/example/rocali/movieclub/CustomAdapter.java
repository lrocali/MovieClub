package com.example.rocali.movieclub;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{
    public Model model = Model.getInstance();

    int [] movieImgSrcs;
    String [] movieTitles;
    String [] movieYears;

    Context context;

    private static LayoutInflater inflater=null;
    public CustomAdapter(MovieList movieList) {
        // TODO Auto-generated constructor stub
        context=movieList;
        movieImgSrcs =model.getImgResources(context);
        movieTitles=model.getMovieTitles();
        movieYears = model.getMovieYears();

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return movieTitles.length;
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

        holder.poster.setImageResource(movieImgSrcs[position]);
        holder.title.setText(movieTitles[position]);
        holder.year.setText(movieYears[position]);

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
package com.example.rocali.movieclub.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLInput;

/**
 * Created by rocali on 9/20/15.
 */
public class SQLData extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "movieclub.db";
    public static final String MOVIE_TABLE_NAME = "movie_table";
    public static final String PARTY_TABLE_NAME = "party_table";
    public static final String COL_ID = "ID";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_YEAR = "YEAR";
    public static final String COL_PLOT = "PLOT";
    public static final String COL_RUNTIME = "RUNTIME";
    public static final String COL_GENRE = "GENRE";
    public static final String COL_COUNTRY = "COUNTRY";
    public static final String COL_VOTES = "VOTES";
    public static final String COL_IMDBRATING = "IMDBRATING";
    public static final String COL_IMGURL = "IMGURL";
    public static final String COL_RATING = "RATING";

    public static final String COL_DATE = "DATE";
    public static final String COL_TIME = "TIME";
    public static final String COL_VENUE = "VENUE";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_INVITEES = "INVITEES";


    public SQLData(Context context) {
        super(context,DATABASE_NAME,null,1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MOVIE_TABLE_NAME + " (ID TEXT PRIMARY KEY,TITLE TEXT,YEAR TEXT,PLOT TEXT,RUNTIME TEXT,GENRE TEXT,COUNTRY TEXT,VOTES TEXT,IMDBRATING TEXT,IMGURL TEXT,RATING TEXT)");
        db.execSQL("create table " + PARTY_TABLE_NAME + " (ID TEXT PRIMARY KEY,DATE TEXT,TIME TEXT,VENUE TEXT,LOCATION TEXT,INVITEES TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PARTY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMovie(String _id,String _title, String _year, String _plot,String _runtime,String _genre,String _country,String _imdbVotes,String _imdbRating,String _imgURL,String _rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        //onUpgrade(db,0,0);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,_id);
        contentValues.put(COL_TITLE,_title);
        contentValues.put(COL_YEAR,_year);
        contentValues.put(COL_PLOT,_plot);
        contentValues.put(COL_RUNTIME,_runtime);
        contentValues.put(COL_GENRE,_genre);
        contentValues.put(COL_COUNTRY,_country);
        contentValues.put(COL_VOTES,_imdbVotes);
        contentValues.put(COL_IMDBRATING,_imdbRating);
        contentValues.put(COL_IMGURL,_imgURL);
        contentValues.put(COL_RATING,_rating);
        long result = db.insert(MOVIE_TABLE_NAME,null,contentValues);
        if (result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor getMovie(String _id){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.query(COL_ID,
               // allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                //null, null, null);
        String query = "SELECT * FROM " + MOVIE_TABLE_NAME + " WHERE " + COL_ID + " = '" + _id+"'";
        Cursor res = db.rawQuery(query,null);
        return res;


    }

    public Cursor getAllMovies(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+MOVIE_TABLE_NAME,null);
        return res;
    }


    public boolean updatetMovie(String _id,String _rating) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,_id);

        contentValues.put(COL_RATING, _rating);
        String query = "UPDATE " + MOVIE_TABLE_NAME + " SET " + COL_RATING + "='"+_rating+"' WHERE " + COL_ID + " = '" + _id+"';";
        db.execSQL(query);

            return true;

    }

    public boolean insertParty(String _id,String _date, String _time, String _venue,String _location,String _invitees) {
        SQLiteDatabase db = this.getWritableDatabase();
        ///onUpgrade(db,0,0);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,_id);
        contentValues.put(COL_DATE,_date);
        contentValues.put(COL_TIME,_time);
        contentValues.put(COL_VENUE,_venue);
        contentValues.put(COL_LOCATION,_location);
        contentValues.put(COL_INVITEES,_invitees);

        long result = db.insert(PARTY_TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        } else {
            return true;
        }
    }

    //GET ALL PARTY FRMO DB
    public Cursor getAllParties(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + PARTY_TABLE_NAME, null);
        return res;
    }

    //UPDATE PARTY


}

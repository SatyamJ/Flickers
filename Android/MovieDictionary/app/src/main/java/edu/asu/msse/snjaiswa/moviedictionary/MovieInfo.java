package edu.asu.msse.snjaiswa.moviedictionary;

/*
 * Copyright 2016 Satyam Jaiswal,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * My Instuctors and the University have the right to build and evaluate the
 * software package for the purpose of determining my grade and program assessment
 *
 * Purpose: Fulfilling Lab assignment 9 ( Android Movie Library and Player) submission of
 * SER598 - Mobile Systems course
 * This assignment demonstrates the use of media streaming server for playing a movie.
 *
 * @author Satyam Jaiswal Satyam.Jaiswal@asu.edu
 *         Software Engineering, ASU Poly
 * @version March 2016
 */

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MovieInfo extends AppCompatActivity {

    MovieDescription movie;
    public static MovieInfo instance;
    //int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieInfo.instance = this;
        setContentView(R.layout.movie_info_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            movie = (MovieDescription)extras.getSerializable("movie");
            updateUI(movie);
        }

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        Button playMovieButton = (Button) findViewById(R.id.playMovieButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    MoviesSQLiteOpenHelper db = new MoviesSQLiteOpenHelper(MovieInfo.instance);
                    SQLiteDatabase mvsDB = db.openDB();
                    String deleteQuery = "delete from movies where _title = '"+movie.getTitle()+"';";
                    mvsDB.execSQL(deleteQuery);
                    mvsDB.close();
                    db.close();
                    MainActivity.ladapter.remove(movie.getTitle());
                    MainActivity.ladapter.notifyDataSetChanged();
                    Toaster.showToast(MovieInfo.instance, "Movie deleted");
                }catch (Exception e){
                    e.getStackTrace();
                }
                finish();
            }
        });

    }

    public void updateUI(final MovieDescription movie){

        this.movie = movie;
        TextView titleTextView = (TextView)findViewById(R.id.titleValue);
        titleTextView.setText(movie.getTitle());

        TextView yearTextView = (TextView)findViewById(R.id.yearValue);
        yearTextView.setText(movie.getYear());

        TextView ratedTextView = (TextView)findViewById(R.id.ratedValue);
        ratedTextView.setText(movie.getRated());

        TextView releasedTextView = (TextView)findViewById(R.id.releasedValue);
        releasedTextView.setText(movie.getReleased());

        TextView genreTextVew = (TextView)findViewById(R.id.genreValue);
        genreTextVew.setText(movie.getGenre());

        TextView runtimeTextView = (TextView)findViewById(R.id.runtimeValue);
        runtimeTextView.setText(movie.getRuntime());

        TextView actorsTextView = (TextView)findViewById(R.id.actorsValue);
        actorsTextView.setText(movie.getActors());

        TextView plotTextView = (TextView)findViewById(R.id.plotValue);
        plotTextView.setText(movie.getPlot());

        System.out.println(movie.getFilename());
        if(! (movie.getFilename().startsWith("unknown"))){
            System.out.println("Movie name is not unknown");
            Button playMovieButton = (Button) findViewById(R.id.playMovieButton);
            playMovieButton.setVisibility(View.VISIBLE);

            playMovieButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent sendingIntent = new Intent(getApplicationContext(), MediaActivity.class);
                    sendingIntent.putExtra("mediaFilename", movie.getFilename());

                    startActivityForResult(sendingIntent, 0);
                }
            });
        }else{
            Button playMovieButton = (Button) findViewById(R.id.playMovieButton);
            playMovieButton.setVisibility(View.GONE);
            System.out.println("Movie name is unknown");
        }
    }

}

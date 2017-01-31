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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static CustomAdapter ladapter;
    public static MainActivity instance;
    MovieDescription selectedMovie;
    public static int selectedMovieIndex;
    public static Cursor cur;
    private SQLiteDatabase mvsDB;
    private ArrayList<String> movienames;
    public static String serverUrlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        MainActivity.serverUrlString = getResources().getString(R.string.serverUrl);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupListView();
    }

    private void setupListView(){
        try{
            MoviesSQLiteOpenHelper db = new MoviesSQLiteOpenHelper((Context)this);
            SQLiteDatabase mvsDB = db.openDB();
            Cursor cur = mvsDB.rawQuery("select _title from movies;", new String[]{});

            movienames = new ArrayList<String>();
            while(cur.moveToNext()){
                try{
                    movienames.add(cur.getString(0));
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            cur.close();
            mvsDB.close();
            db.close();

            ladapter = new CustomAdapter(this, android.R.layout.simple_list_item_single_choice, movienames);

            ListView movieListView = (ListView) findViewById(R.id.movieListView);
            movieListView.setAdapter(ladapter);

            movieListView.setOnItemClickListener(new
                 AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         String str = ladapter.getItem(position);//.toString();
                         selectedMovieIndex = position;
                         String clickedMovie = "You clicked " + str + ". Loading page...";
                         Toaster.showToast(MainActivity.instance, clickedMovie);

                         try {
                             MoviesSQLiteOpenHelper db = new MoviesSQLiteOpenHelper(MainActivity.instance);
                             SQLiteDatabase mvsDB = db.openDB();
                             Cursor cur = mvsDB.rawQuery("select * from movies where _title=?", new String[]{str});
                             //cur = mvsDB.rawQuery("select * from movies where _title=?", new String[]{str});
                             String title, year, rated, released, runtime, genre, actors, plot, poster, filepath;
                             title = year = rated = released = runtime = genre = actors = plot = poster = filepath = "unknown";
                             while (cur.moveToNext()) {
                                 title = cur.getString(0);
                                 year = cur.getString(1);
                                 rated = cur.getString(2);
                                 released = cur.getString(3);
                                 runtime = cur.getString(4);
                                 genre = cur.getString(5);
                                 actors = cur.getString(6);
                                 plot = cur.getString(7);
                                 poster = cur.getString(8);
                                 filepath = cur.getString(9);
                             }
                             cur.close();
                             mvsDB.close();
                             db.close();

                             MovieDescription movie = new MovieDescription(title, year, rated, released,
                                     runtime, genre, actors, plot, poster, filepath);
                             Intent sendingIntent = new Intent(getApplicationContext(), MovieInfo.class);
                             sendingIntent.putExtra("movie", movie);

                             startActivityForResult(sendingIntent, 0);
                         }catch (Exception e){
                             e.getStackTrace();
                         }
                     }
                 });

        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup list view");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("MainActivity onRestart called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MainActivity onStart called");
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_add_movie) {
            Intent i = new Intent(this, AddMovieActivity.class);
            //startActivity(i);
            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

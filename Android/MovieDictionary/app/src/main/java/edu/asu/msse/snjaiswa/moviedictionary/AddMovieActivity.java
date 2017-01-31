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

import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

public class AddMovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {

    Spinner gSpinner;
    String genre;
    SearchView searchView;
    public static AddMovieActivity sharedInstance;
    public static MovieDescription loadedMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedInstance = this;
        setContentView(R.layout.activity_add_movie);

        gSpinner = (Spinner) findViewById(R.id.genreSpinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.movieGenres, android.R.layout.simple_spinner_item);
        gSpinner.setAdapter(adapter);
        gSpinner.setOnItemSelectedListener(this);


        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String title, year, released, rated, runtime, actors, plot;
                EditText titleET = (EditText) findViewById(R.id.TitleEditText);
                title = titleET.getText().toString();

                EditText yearET = (EditText) findViewById(R.id.yearEditText);
                year = yearET.getText().toString();

                EditText releasedET = (EditText) findViewById(R.id.releasedEditText);
                released = releasedET.getText().toString();

                EditText ratedET = (EditText) findViewById(R.id.ratedEditText);
                rated = ratedET.getText().toString();

                EditText runtimeET = (EditText) findViewById(R.id.runtimeEditText);
                runtime = runtimeET.getText().toString();

                EditText actorsET = (EditText) findViewById(R.id.actorsEditText);
                actors = actorsET.getText().toString();

                EditText plotET = (EditText) findViewById(R.id.plotEditText);
                plot = plotET.getText().toString();

                MovieDescription m = new MovieDescription(title, year, released, genre, rated, runtime,
                        actors, plot, loadedMovie.getPoster(), loadedMovie.getFilename());

                addNewMovieToDatabase(m);
                finish();
            }
        });
    }

    public void addNewMovieToDatabase(MovieDescription newMovie){

        String insert = "insert into movies values( '"+newMovie.getTitle().replace("'","''")
                +"', '"+newMovie.getYear().replaceAll("'", "''")
                +"', '"+newMovie.getRated().replaceAll("'", "''")
                +"', '" +newMovie.getReleased().replaceAll("'", "''")
                +"', '"+newMovie.getRuntime().replaceAll("'", "''")
                +"', '"+newMovie.getGenre().replaceAll("'", "''")
                +"', '"+newMovie.getActors().replaceAll("'", "''")
                +"', '"+newMovie.getPlot().replaceAll("'", "''")
                +"', '"+newMovie.getPoster().replaceAll("'", "''")
                +"', '"+newMovie.getFilename().replaceAll("'", "''")+"')";
        try {
            MoviesSQLiteOpenHelper db = new MoviesSQLiteOpenHelper(MainActivity.instance);
            SQLiteDatabase mvsDB = db.openDB();
            mvsDB.execSQL(insert);
            mvsDB.close();
            MainActivity.ladapter.add(newMovie.getTitle());
            MainActivity.ladapter.notifyDataSetChanged();
            Toaster.showToast(MainActivity.instance, "New movie added: " + newMovie.getTitle());
        }catch (Exception e){
            System.out.println("Exception on new movie insert statement: " + e);
            Toaster.showToast(MainActivity.instance, e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_movie_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
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
            /*
            Intent i = new Intent(this, AddMovieActivity.class);

            startActivity(i); */
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView text = (TextView) view;
        this.genre = text.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("onQueryTextSubmit called. Query submitted"+ query);
        String[] params = {query};

        try{
            MethodInformation mi = new MethodInformation(MainActivity.instance, MainActivity.serverUrlString, "get", params);
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception getting selected movie: "+
                    ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("onQueryTextChange called. Query: "+ newText);
        return false;
    }

    public void updateUI(MovieDescription movie){
        loadedMovie = movie;
        EditText titleET = (EditText)findViewById(R.id.TitleEditText);
        titleET.setText(movie.getTitle());

        EditText yearET = (EditText)findViewById(R.id.yearEditText);
        yearET.setText(movie.getYear());

        EditText releasedET = (EditText)findViewById(R.id.releasedEditText);
        releasedET.setText(movie.getReleased());

        EditText ratedET = (EditText)findViewById(R.id.ratedEditText);
        ratedET.setText(movie.getRated());

        EditText runtimeET = (EditText)findViewById(R.id.runtimeEditText);
        runtimeET.setText(movie.getRuntime());

        EditText actorsET = (EditText)findViewById(R.id.actorsEditText);
        actorsET.setText(movie.getActors());

        EditText plotET = (EditText)findViewById(R.id.plotEditText);
        plotET.setText(movie.getPlot());

        EditText posterET = (EditText)findViewById(R.id.posterEditText);
        posterET.setText(movie.getPoster());

        EditText movieFileET = (EditText)findViewById(R.id.movieFile);
        movieFileET.setText(movie.getFilename());

        String fetchedMovieGenre = movie.getGenre();
        String[] genres = fetchedMovieGenre.split(","); //substring(0, fetchedMovieGenre.indexOf(","));
        gSpinner.setSelection(((ArrayAdapter<String>) gSpinner.getAdapter()).getPosition(genres[0]));
    }
}

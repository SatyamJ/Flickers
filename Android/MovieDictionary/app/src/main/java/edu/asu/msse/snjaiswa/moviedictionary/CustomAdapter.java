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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, int resource, ArrayList<String> lib) {
        super(context, resource, lib);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        //View customView = li.inflate(R.layout.cell_layout, parent, false);
        View customView = li.inflate(R.layout.cell_layout, parent, false);
        String movieTitle = getItem(position);
        TextView cellTextView = (TextView) customView.findViewById(R.id.cellTextView);
        cellTextView.setText(movieTitle);

        return customView;
    }
}

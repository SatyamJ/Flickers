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

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

public class MediaActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener{

    private VideoView mVideoView;
    private MediaController mController;
    private MediaMetadataRetriever mMetadataRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        /*
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
        */

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String mediaFilename = (String)extras.getSerializable("mediaFilename");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mVideoView = (VideoView) findViewById(R.id.avideoview);

            mVideoView.setVideoPath(getString(R.string.urlprefix) + mediaFilename);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(mVideoView);
            mVideoView.setMediaController(mediaController);
            mVideoView.setOnPreparedListener(this);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp){
        android.util.Log.d(this.getClass().getSimpleName(), "onPrepared called. Video Duration: "
                + mVideoView.getDuration());
        mVideoView.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        android.util.Log.d(this.getClass().getSimpleName(), "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}

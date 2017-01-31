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
import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {

    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){

        MovieDescription movie = new MovieDescription("unknown", "unknown", "unknown", "unknown",
                "unknown", "unknown", "unknown", "unknown", "unknown");
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            android.util.Log.d(this.getClass().getSimpleName(),"requestData: "+requestData+" url: "+aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)), aRequest[0].parent);
            String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = resultStr;
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        try {
            if (res.method.equals("get")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                MovieDescription movie = new MovieDescription(jo.getJSONObject("result"));

                if(movie.getTitle().equalsIgnoreCase("Unknown")){
                    AsyncAPIClient a = (AsyncAPIClient) new AsyncAPIClient().execute(res.params[0]);
                }else{
                    AddMovieActivity.sharedInstance.updateUI(movie);
                }

            }
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }

}
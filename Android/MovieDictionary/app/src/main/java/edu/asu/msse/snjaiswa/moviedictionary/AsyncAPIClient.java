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
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;


public class AsyncAPIClient extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {

        String result = "";
        try {
            String movieNameQueried = params[0].replace(" ", "+");
            URL url = new URL("http://www.omdbapi.com/?t="+movieNameQueried+"&y=&plot=short&r=json");
            result = makeHTTPRequest(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected String makeHTTPRequest(URL url){
        String result = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            OutputStream out = null;
            try {
                out = connection.getOutputStream();
                out.flush();
                out.close();
                int statusCode = connection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    throw new Exception(
                            "Unexpected status from post: " + statusCode);
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            String responseEncoding = connection.getHeaderField("Content-Encoding");
            responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            try {
                in = connection.getInputStream();
                if ("gzip".equalsIgnoreCase(responseEncoding)) {
                    in = new GZIPInputStream(in);
                }
                in = new BufferedInputStream(in);
                byte[] buff = new byte[1024];
                int n;
                while ((n = in.read(buff)) > 0) {
                    bos.write(buff, 0, n);
                }
                bos.flush();
                bos.close();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            if (bos != null) {
                result = bos.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {

        super.onPostExecute(response);
        System.out.println("Movie received from network" + response);

        try {
            JSONObject j = new JSONObject(response);
            if (j.getString("Response").equals("True")){
                Toaster.showToast(AddMovieActivity.sharedInstance, "Match found!");
                MovieDescription movie = new MovieDescription(j);

                AddMovieActivity.sharedInstance.updateUI(movie);
            }else{
                Toaster.showToast(AddMovieActivity.sharedInstance, j.getString("Error"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

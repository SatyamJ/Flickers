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

import org.json.JSONObject;
import java.io.Serializable;

public class MovieDescription implements Serializable{
        private String title;
        private String year;
        private String rated;
        private String released;
        private String runtime;
        private String genre;
        private String actors;
        private String plot;
        private String poster;
        private String filename;

        public MovieDescription(String title, String year, String rated, String released, String runtime,
                                String genre, String actors, String plot, String poster){
            this.title = title;
            this.year = year;
            this.rated = rated;
            this.released = released;
            this.runtime = runtime;
            this.genre = genre;
            this.actors = actors;
            this.plot = plot;
            this.poster = poster;
            this.filename = "unknown.mp4";
        }

        public MovieDescription(String title, String year, String rated, String released, String runtime,
                                String genre, String actors, String plot, String poster, String filename){
            this.title = title;
            this.year = year;
            this.rated = rated;
            this.released = released;
            this.runtime = runtime;
            this.genre = genre;
            this.actors = actors;
            this.plot = plot;
            this.poster = poster;
            this.filename = filename;
        }

        public MovieDescription(JSONObject movieJson){
            try {
                this.title = movieJson.getString("Title");
                this.year = movieJson.getString("Year");
                this.rated = movieJson.getString("Rated");
                this.released = movieJson.getString("Released");
                this.runtime = movieJson.getString("Runtime");
                this.genre = movieJson.getString("Genre");
                this.actors = movieJson.getString("Actors");
                this.plot = movieJson.getString("Plot");
                this.poster = movieJson.getString("Poster");

                if(movieJson.has("Filename")){
                    System.out.println("Got movie from json");
                    this.filename = movieJson.getString("Filename");
                }else{
                    System.out.println("No movie in json");
                    this.filename = "unknown.mp4";
                }
            }catch(Exception e){
                android.util.Log.w(this.getClass().getSimpleName(), "error setting the value");
            }
        }

        public JSONObject toJson(MovieDescription m){
            JSONObject movieJO = new JSONObject();
            try{

                movieJO.put("Title", title);
                movieJO.put("Year", year);
                movieJO.put("Rated", rated);
                movieJO.put("Released", released);
                movieJO.put("Runtime", runtime);
                movieJO.put("Genre", genre);
                movieJO.put("Actors", actors);
                movieJO.put("Plot", plot);
            }catch (Exception ex){
                System.out.println(this.getClass().getSimpleName() +
                        ": error converting to json");
            }
            return movieJO;
        }

        public String toJsonString(JSONObject j){
            String str = "";
            try{
                str = j.toString();
            }catch (Exception ex){
                System.out.println(this.getClass().getSimpleName() +
                        ": error converting to json string");
            }
            return str;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getRated() {
            return rated;
        }

        public void setRated(String rated) {
            this.rated = rated;
        }

        public String getReleased() {
            return released;
        }

        public void setReleased(String released) {
            this.released = released;
        }

        public String getRuntime() {
            return runtime;
        }

        public void setRuntime(String runtime) {
            this.runtime = runtime;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getActors() {
            return actors;
        }

        public void setActors(String actors) {
            this.actors = actors;
        }

        public String getPlot() {
            return plot;
        }

        public void setPlot(String plot) {
            this.plot = plot;
        }

        public String getPoster() {
        return poster;
    }

        public void setPoster(String poster) {
        this.poster = poster;
    }

        public  String getFilename(){
            return filename;
        }

        public void setFilename(String filepath){
            this.filename = filepath;
        }
}

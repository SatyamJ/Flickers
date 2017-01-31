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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;


public class MoviesSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "moviesdb";
    private final static int DATABASE_VERSION = 1;
    private static String dbPath;
    private SQLiteDatabase mvsDB;
    private final Context context;
    private static final boolean debugon = false;

    public MoviesSQLiteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        dbPath = this.context.getFilesDir().getPath() + "/";
        android.util.Log.d(this.getClass().getSimpleName(), "dbpath: "+dbPath);
    }

    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }

    private boolean checkDB(){    //does the database exist and is it initialized?
        SQLiteDatabase checkDB = null;
        boolean ret = false;
        try{
            String path = dbPath + DATABASE_NAME + ".db";
            debug("MoviesSQLiteOpenHelper --> checkDB: path to db is", path);
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    debug("MoviesSQLiteOpenHelper --> checkDB","opened db at: "+checkDB.getPath());
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='movies';", null);
                    boolean mvsTabExists = false;
                    if(tabChk == null){
                        debug("MoviesSQLiteOpenHelper --> checkDB","check for course table result set is null");
                    }else{
                        tabChk.moveToNext();
                        debug("MoviesSQLiteOpenHelper --> checkDB","check for course table result set is: " +
                                ((tabChk.isAfterLast() ? "empty" : (String) tabChk.getString(0))));
                        mvsTabExists = !tabChk.isAfterLast();
                    }
                    if(mvsTabExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM movies", null);
                        c.moveToFirst();
                        while(! c.isAfterLast()) {
                            String mvsName = c.getString(0);
                            //int crsid = c.getInt(1);
                            debug("MoviesSQLiteOpenHelper --> checkDB", "Movies table has titles: "+
                                    mvsName);
                            c.moveToNext();
                        }
                        ret = true;
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return ret;
    }

    public void copyDB() throws IOException{
        try {
            if(!checkDB()){
                // only copy the database if it doesn't already exist in my database directory
                debug("copyDB", "checkDB returned false, starting copy");
                InputStream ip =  context.getResources().openRawResource(R.raw.moviesdb);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath + DATABASE_NAME +".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("copyDB", "IOException: "+e.getMessage());
        }
    }
    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + DATABASE_NAME + ".db";
        if(checkDB()) {
            mvsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            debug("openDB", "opened db at path: " + mvsDB.getPath());
        }else{
            try {
                this.copyDB();
                mvsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),"unable to copy and open db: "+ex.getMessage());
            }
        }
        return mvsDB;
    }

    @Override
    public synchronized void close() {
        if(mvsDB != null)
            mvsDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void debug(String hdr, String msg){
        if(debugon){
            android.util.Log.d(hdr, msg);
        }
    }
}

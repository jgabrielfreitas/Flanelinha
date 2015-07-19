package br.com.manta.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 16/01/2015.
 */
public class LocalRepository extends SQLiteOpenHelper {

    public static LocalRepository mLocalRepository ;

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "Local.db";

    // Base Tables
    public static final String TABLE_ID        = "ID";
    public static final String TABLE_NAME      = "flanelinha_locations";
    public static final String TABLE_LATITUDE  = "LATITUDE";
    public static final String TABLE_LONGITUDE = "LONGITUDE";
    public static final String TABLE_DATE_HOUR = "DATE_HOUR";
    public static final String TABLE_LOCATION_ADDRESS = "ADDRESS";

    /**
     * CREATE TABLE "flanelinha_locations_db"
     * ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,
     * "latitude" TEXT,
     * "longitude" TEXT,
     * "DATE_HOUR" TEXT);
     * */

    public static final String CREATE_LOCATION_BASE = "CREATE TABLE IF NOT EXISTS '" + TABLE_NAME + "' " +
            " ('"  + TABLE_ID        + "' INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ," +
            " '"   + TABLE_LATITUDE  + "' TEXT, " +
            " '"   + TABLE_LONGITUDE + "' TEXT, " +
            " '"   + TABLE_DATE_HOUR + "' TEXT, " +
            " '"   + TABLE_LOCATION_ADDRESS + "' TEXT ); ";


    public LocalRepository (Context ctx){
        super(ctx, TABLE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATION_BASE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }









}

package br.com.manta.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JGabrielFreitas on 08/06/15.
 */
public class LocationDAO extends SQLiteOpenHelper {

    private static final String TABLE_ID        = "ID";
    private static final String TABLE_NAME      = "flanelinha_locations";
    private static final String TABLE_LATITUDE  = "LATITUDE";
    private static final String TABLE_LONGITUDE = "LONGITUDE";
    private static final String TABLE_DATE_HOUR = "DATE_HOUR";
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_LOCATION_ADDRESS = "ADDRESS";
    private SQLiteDatabase sqLiteDatabase;

    /**
     * CREATE TABLE "flanelinha_locations_db"
     * ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,
     * "latitude" TEXT,
     * "longitude" TEXT,
     * "DATE_HOUR" TEXT);
     * */

    public static final String CREATE_LOCATION_BASE = "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME + "\"" +
            " ( \"" + TABLE_ID        + "\" INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ," +
            " \""   + TABLE_LATITUDE  + "\" TEXT, " +
            " \""   + TABLE_LONGITUDE + "\" TEXT, " +
            " \""   + TABLE_DATE_HOUR + "\" TEXT ); " +
            " \""   + TABLE_LOCATION_ADDRESS + "\" TEXT ); ";


    public LocationDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TABLE_NAME, factory, DATABASE_VERSION);

    }



    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATION_BASE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(LocationObjectBase locationObject){



    }

}
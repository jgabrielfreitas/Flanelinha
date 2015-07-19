package br.com.manta.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static br.com.manta.database.LocalRepository.TABLE_LONGITUDE;
import static br.com.manta.database.LocalRepository.TABLE_LOCATION_ADDRESS;
import static br.com.manta.database.LocalRepository.TABLE_LATITUDE;
import static br.com.manta.database.LocalRepository.TABLE_DATE_HOUR;
import static br.com.manta.database.LocalRepository.TABLE_NAME;
import static br.com.manta.database.LocalRepository.TABLE_ID;

/**
 * Created by JGabrielFreitas on 08/06/15.
 */
public class LocationDAO {

    private SQLiteDatabase sqLiteDatabase;
    private LocalRepository localRepository;
    private Context context;

    public LocationDAO(Context context) {
        this.context = context;
        this.localRepository = new LocalRepository(context);
        openSql();
    }

    private void openSql() {
        sqLiteDatabase = localRepository.getWritableDatabase();
    }

    // this method return if the location was inserted successfully
    public boolean insert(LocationObjectBase locationObject) {

        checkIfBaseIfOpen();

        boolean success = false;

        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_LOCATION_ADDRESS, locationObject.getAddress());
            values.put(TABLE_LONGITUDE, locationObject.getLongitude());
            values.put(TABLE_DATE_HOUR, locationObject.getDateHour());
            values.put(TABLE_LATITUDE, locationObject.getLatitude());

            sqLiteDatabase.insert(TABLE_NAME, null, values);
            sqLiteDatabase.close();

            Log.d("INSERT", "Location saved:\n " + locationObject.toString());
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("INSERT", "ERROR");
        }

        return success;
    }

    public int getLocalCount() {


        String countQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        cursor.moveToFirst();
        cursor.close();

        return cursor.getCount();
    }

    public int updateLocal(Local local) {


        ContentValues values = new ContentValues();
        values.put(TABLE_LATITUDE, local.getLatitude());
        values.put(TABLE_LONGITUDE, local.getLongitude());
        values.put(TABLE_LOCATION_ADDRESS, local.getLocalName());
        values.put(TABLE_DATE_HOUR, local.getDateTime());

        int i = sqLiteDatabase.update(TABLE_NAME, values, TABLE_ID + " = ?",
                new String[]{String.valueOf(local.getId())});

        sqLiteDatabase.close();
        return i;
    }

    public List<Local> getAllLocal() {

        // instance list to return
        List<Local> localList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + TABLE_ID + " ASC";

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst() == true) {
            do {

                localList.add(getFromBase(cursor));
            } while (cursor.moveToNext() == true);
        }
        cursor.close();


        return localList;
    }

    private Local getFromBase(Cursor cursor) {

        Local local = new Local();

        local.setId(cursor.getInt(0));
        local.setLatitude(Double.parseDouble(cursor.getString(1)));
        local.setLongitude(Double.parseDouble(cursor.getString(2)));
        local.setDateTime(cursor.getString(3));
        local.setLocalName(cursor.getString(4));

        return local;
    }

    public Long getLastInsertId() {
        Long index = 0l;
        Cursor cursor = sqLiteDatabase.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{TABLE_NAME},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            index = cursor.getLong(cursor.getColumnIndex("seq"));
        }
        cursor.close();
        return index;
    }

    private void checkIfBaseIfOpen() {
        if (sqLiteDatabase.isOpen() == false)
            openSql();
    }

    public void showAllColumnsInLog(){

        checkIfBaseIfOpen();

        Cursor dbCursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

        for (String columnName : columnNames)
            Log.i("COLUMN_NAME", columnName);
    }

}
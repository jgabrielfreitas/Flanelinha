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
    public static String TABLE_LOCAL   = "LocalRepository";

    public static String KEY_ID        = "id";
    public static String KEY_LATITUDE  = "latitude";
    public static String KEY_LONGITUDE = "longitude";
    public static String KEY_LOCALNAME = "local_name";
    public static String KEY_DATETIME  = "datetime";

    private Context mCxt;

    public static LocalRepository getInstance(Context ctx) {
        if (mLocalRepository == null){
            mLocalRepository = new LocalRepository(ctx.getApplicationContext());
        }
        return mLocalRepository;
    }

    public LocalRepository (Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCxt = ctx;
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCAL_TABLE = "CREATE TABLE "
                + TABLE_LOCAL
                + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_LATITUDE  + " REAL," +
                KEY_LONGITUDE + " REAL," +
                KEY_LOCALNAME + " TEXT," +
                KEY_DATETIME  + " TEXT"  + ");";
        db.execSQL(CREATE_LOCAL_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL);
        onCreate(db);
    }

    public void addLocal(Local local) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE,  local.getLatitude());
        values.put(KEY_LONGITUDE, local.getLongitude());
        values.put(KEY_LOCALNAME, local.getLocalName());
        values.put(KEY_DATETIME,  local.getDateTime());

        db.insert(TABLE_LOCAL, null, values);
        db.close();
    }

    public Local getLocal(int id)  {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCAL, new String[] { KEY_ID,
                        KEY_LATITUDE,
                        KEY_LONGITUDE,
                        KEY_LOCALNAME,
                        KEY_DATETIME
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Local local = new Local(
                cursor.getInt(0),
                cursor.getDouble(1),
                cursor.getDouble(1),
                cursor.getString(3),
                cursor.getString(4) );
        return  local;
    }

    public List<Local> getAllLocal(){
        List<Local> localList = new ArrayList<>();

       // String selectQuery = "SELECT  * FROM " + TABLE_LOCAL + " ORDER BY " + KEY_ID + " DESC" ;
        String selectQuery = "SELECT  * FROM " + TABLE_LOCAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Local local = new Local();

                local.setId(cursor.getInt(0));
                local.setLatitude(cursor.getDouble(1));
                local.setLongitude(cursor.getDouble(2));
                local.setLocalName(cursor.getString(3));
                local.setDateTime(cursor.getString(4));

                localList.add(local);

            } while (cursor.moveToNext());
        }
        return localList;
    }

    public int getLocalCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        cursor.close();

        return cursor.getCount();
    }

    public int updateLocal(Local local) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE,  local.getLatitude());
        values.put(KEY_LONGITUDE, local.getLongitude());
        values.put(KEY_LOCALNAME, local.getLocalName());
        values.put(KEY_DATETIME,  local.getDateTime());

        int i = db.update(TABLE_LOCAL, values, KEY_ID + " = ?",
                new String[] { String.valueOf(local.getId()) });

        db.close();
        return i;
    }


    public Long getLastInsertId() {
        Long index = 0l;
        SQLiteDatabase sdb = getReadableDatabase();
        Cursor cursor = sdb.query(
                "sqlite_sequence",
                new String[] { "seq" },
                "name = ?",
                new String[] { TABLE_LOCAL },
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







}

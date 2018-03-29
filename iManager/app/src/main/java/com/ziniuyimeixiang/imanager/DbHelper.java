package com.ziniuyimeixiang.imanager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j_mei on 2018-03-26.
 */

public class DbHelper extends SQLiteOpenHelper {

     /*Logcat tag*/
    private static final String LOG = "DatabaseHelper";

    /* Database Version*/
    private static final int DATABASE_VERSION = 1;

    /* Database Name*/
    private static final String DATABASE_NAME = "image.db";

    /* Table Names */
    private static final String TABLE_winterCoat = "winter_coat";
    private static final String TABLE_sweater = "sweater";
    private static final String TABLE_legging = "legging";
    private static final String TABLE_tShirt = "t_shirt";
    private static final String TABLE_windProofJacket = "wind_proof_jacket";
    private static final String TABLE_springCoat = "spring_coat";
    private static final String TABLE_hoodie = "hoodie";
    private static final String TABLE_dress = "dress";
    private static final String TABLE_skirt = "skirt";
    private static final String TABLE_jeans = "jeans";
    private static final String TABLE_shorts = "shorts";

    /* Common column names*/
    private static final String KEY_imageName = "image_name";
    private static final String KEY_ID = "id";

    /* create table statement */
    private static final String CREATE_TABLE_winterCoat = "CREATE TABLE " + TABLE_winterCoat + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_sweater = "CREATE TABLE " + TABLE_sweater + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_legging = "CREATE TABLE " + TABLE_legging + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_tShirt = "CREATE TABLE " + TABLE_tShirt + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_windProofJacket = "CREATE TABLE " + TABLE_windProofJacket + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_springCoat = "CREATE TABLE " + TABLE_springCoat + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_hoodie = "CREATE TABLE " + TABLE_hoodie + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_dress = "CREATE TABLE " + TABLE_dress + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_skirt = "CREATE TABLE " + TABLE_skirt + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_jeans = "CREATE TABLE " + TABLE_jeans + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";
    private static final String CREATE_TABLE_shorts = "CREATE TABLE " + TABLE_shorts + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_imageName + " BLOB NOT NULL " + " );";

//    Context context;
//    SQLiteDatabase db;
//    ContentResolver contentResolver;


    /* constructor */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        contentResolver = context.getContentResolver();
//        db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_winterCoat);
        db.execSQL(CREATE_TABLE_sweater);
        db.execSQL(CREATE_TABLE_legging);
        db.execSQL(CREATE_TABLE_tShirt);
        db.execSQL(CREATE_TABLE_windProofJacket);
        db.execSQL(CREATE_TABLE_springCoat);
        db.execSQL(CREATE_TABLE_hoodie);
        db.execSQL(CREATE_TABLE_dress);
        db.execSQL(CREATE_TABLE_skirt);
        db.execSQL(CREATE_TABLE_jeans);
        db.execSQL(CREATE_TABLE_shorts);
    }

    public void addToDb(String tableName, byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_imageName, image);
        db.insert(tableName, null, contentValues);
        db.close();
    }

    public List<byte[]> getAllImage(String tableName){
        List<byte[]> images = new ArrayList<byte[]>();
        String selectQuery = "SELECT * FROM " + tableName;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                byte[] image = cursor.getBlob(cursor.getColumnIndex(KEY_imageName));
                images.add(image);
            } while (cursor.moveToNext());
        }
        db.close();
        return images;
    }

    public void deleteImage(String tableName, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_winterCoat);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_sweater);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_legging);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_tShirt);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_windProofJacket);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_springCoat);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_hoodie);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_dress);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_skirt);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_jeans);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_shorts);
        onCreate(db);

    }
}

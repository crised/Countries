package com.example.countries.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.countries.data.CountryContract.CountryEntry.COLUMN_LOC;
import static com.example.countries.data.CountryContract.CountryEntry.COLUMN_DESCRIPTION;
import static com.example.countries.data.CountryContract.CountryEntry.COLUMN_FLAG_LINK;
import static com.example.countries.data.CountryContract.CountryEntry.COLUMN_GDP;
import static com.example.countries.data.CountryContract.CountryEntry.COLUMN_KEY;
import static com.example.countries.data.CountryContract.CountryEntry.COLUMN_LANGUAGE;
import static com.example.countries.data.CountryContract.CountryEntry.COLUMN_NAME;
import static com.example.countries.data.CountryContract.CountryEntry.COLUMN_POPULATION;
import static com.example.countries.data.CountryContract.CountryEntry.TABLE_NAME;
import static com.example.countries.data.CountryContract.CountryEntry._ID;

/**
 * Created by crised on 07-06-16.
 */
public class CountryDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "country.db";

    public CountryDbHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_KEY + " INTEGER NOT NULL UNIQUE, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_FLAG_LINK + " TEXT, " +
                COLUMN_GDP + " TEXT, " +
                COLUMN_POPULATION + " TEXT, " +
                COLUMN_LOC + " TEXT, " +
                COLUMN_LANGUAGE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT"
                + ")";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

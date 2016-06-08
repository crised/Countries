package com.example.countries.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by crised on 07-06-16.
 */
public class CountryProvider extends ContentProvider {


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sCountryQueryBuilder = new SQLiteQueryBuilder();


    private CountryDbHelper mCountryDbHelper;

    static final int COUNTRIES = 100; //if favorites they will overlap.


    static {
        sCountryQueryBuilder.setTables(CountryContract.CountryEntry.TABLE_NAME);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CountryContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, CountryContract.PATH, COUNTRIES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mCountryDbHelper = new CountryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case COUNTRIES:
                retCursor = getCountries(projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case COUNTRIES:
                return CountryContract.CountryEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mCountryDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case COUNTRIES: {
                long _id = db.insert(CountryContract.CountryEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CountryContract.CountryEntry.CONTENT_URI;
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mCountryDbHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case COUNTRIES:
                rowsDeleted = db.delete(CountryContract.CountryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        /*final SQLiteDatabase db = mCountryDbHelper.getWritableDatabase();
        int rowsUpdated;
        long _id = ContentUris.parseId(uri);
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        sMovieIdSelection,
                        new String[]{String.valueOf(_id)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;*/
        return 0;
    }

    private Cursor getCountries(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return sCountryQueryBuilder.query(mCountryDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }


}

package com.example.countries.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by crised on 07-06-16.
 */
public class CountryContract {

    public static final String CONTENT_AUTHORITY = "com.example.countries";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH = "countries";

    public static final class CountryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static final String TABLE_NAME = "country";

        public static final String COLUMN_KEY = "country_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FLAG_LINK = "flag";
        public static final String COLUMN_GDP = "gdp";
        public static final String COLUMN_POPULATION = "population";
        public static final String COLUMN_AREA = "area";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_DESCRIPTION = "description";


    }
}

package com.example.countries.net;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.countries.R;
import com.example.countries.data.CountryContract;
import com.example.countries.data.CountryProvider;
import com.example.countries.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by crised on 08-06-16.
 */
public class CountriesService extends IntentService {

    private final String LOG_TAG = CountriesService.class.getSimpleName();

    String mCountriesWeb;


    public CountriesService() {
        super("Countries");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) fetchCountries();
        fetchCountries();
        if (mCountriesWeb != null) parseJsonAndCallInsert();
    }

    private void fetchCountries() {

        if (!Util.isNetworkAvailable(this)) return;

        Cursor cursor = getContentResolver().query(CountryContract.CountryEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        if ((cursor != null) && (cursor.getCount() > 0)) return;
        cursor.close();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(getString(R.string.country_service_url));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) return;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            if (buffer.length() == 0) return;
            mCountriesWeb = buffer.toString();

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }

        }
    }

    static final int MIN_LENGTH = 4;

    private void parseJsonAndCallInsert() {
        try {
            JSONArray jsonArray = new JSONArray(mCountriesWeb);
            for (int i = 0; i < jsonArray.length(); i++) {
                int key;
                String name, flag, gdp, population, area, language, description;
                JSONObject country = jsonArray.getJSONObject(i);
                key = country.getInt("id");
                name = country.getString("name");
                flag = country.getString("flag");
                gdp = country.getString("gdp");
                population = country.getString("population");
                area = country.getString("area");
                language = country.getString("language");
                description = country.getString("description");
                if (name.length() < MIN_LENGTH ||
                        flag.length() < MIN_LENGTH ||
                        gdp.length() < MIN_LENGTH ||
                        population.length() < MIN_LENGTH ||
                        area.length() < MIN_LENGTH ||
                        language.length() < MIN_LENGTH ||
                        description.length() < MIN_LENGTH) throw new IOException();
                insertCountry(key, name, flag, gdp, population, area, language, description);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "failed validating input", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "failed validating input", e);
        }

    }

    private void insertCountry(int key, String name, String flag,
                               String gdp, String population,
                               String area, String language,
                               String description) {
        ContentValues values = new ContentValues();
        values.put(CountryContract.CountryEntry.COLUMN_KEY, key);
        values.put(CountryContract.CountryEntry.COLUMN_NAME, name);
        values.put(CountryContract.CountryEntry.COLUMN_FLAG_LINK, flag);
        values.put(CountryContract.CountryEntry.COLUMN_GDP, gdp);
        values.put(CountryContract.CountryEntry.COLUMN_POPULATION, population);
        values.put(CountryContract.CountryEntry.COLUMN_AREA, area);
        values.put(CountryContract.CountryEntry.COLUMN_LANGUAGE, language);
        values.put(CountryContract.CountryEntry.COLUMN_DESCRIPTION, description);
        getContentResolver().insert(CountryContract.CountryEntry.CONTENT_URI, values);

    }


}

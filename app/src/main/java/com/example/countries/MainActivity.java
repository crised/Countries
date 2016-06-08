package com.example.countries;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.countries.data.CountryContract;
import com.example.countries.net.CountriesService;
import com.example.countries.util.Const;
import com.example.countries.util.Country;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int COUNTRY_LOADER = 0;


    private AdView mAdView;
    private Cursor mCursorData;
    private TextView mText1;
    private List<Country> mCountryList;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Admob
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Start IntentService
        Intent countryIntent = new Intent(this, CountriesService.class);
        startService(countryIntent);

        mText1 = (TextView) findViewById(R.id.text1);

        getSupportLoaderManager().initLoader(COUNTRY_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                CountryContract.CountryEntry.CONTENT_URI,
                null, //every column
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) return;
        mCursorData = data;
        setCountries();
        mText1.setText(mCountryList.get(1).getName());
        //mText1.setText("hola");


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;

    }

    private void setCountries() {
        mCountryList = new ArrayList<>();
        for (mCursorData.moveToFirst(); !mCursorData.isAfterLast(); mCursorData.moveToNext()) {
            Country country = new Country();
            country.setKey(mCursorData.getInt(Const.COL_KEY));
            country.setName(mCursorData.getString(Const.COL_NAME));
            country.setFlag(mCursorData.getString(Const.COL_FLAG_LINK));
            country.setGDP(mCursorData.getString(Const.COL_GDP));
            country.setPopulation(mCursorData.getString(Const.COL_POPULATION));
            country.setArea(mCursorData.getString(Const.COL_AREA));
            country.setLanguage(mCursorData.getString(Const.COL_LANGUAGE));
            country.setDescription(mCursorData.getString(Const.COL_DESCRIPTION));
            mCountryList.add(country);
        }


    }
}

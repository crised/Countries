package com.example.countries;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.countries.data.CountryContract;
import com.example.countries.net.CountriesService;
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

    private RecyclerView mRecyclerView;
    private CountryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    static final int COL_ID = 0;
    static final int COL_KEY = 1;
    static final int COL_NAME = 2;
    static final int COL_FLAG_LINK = 3;
    static final int COL_GDP = 4;
    static final int COL_POPULATION = 5;
    static final int COL_AREA = 6;
    static final int COL_LANGUAGE = 7;
    static final int COL_DESCRIPTION = 8;

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

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CountryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


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
        mAdapter.swapCursor(data);
        mCursorData = data;
        setCountries();
        //mText1.setText(mCountryList.get(1).getName());


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }

    private void setCountries() {
        mCountryList = new ArrayList<>();
        for (mCursorData.moveToFirst(); !mCursorData.isAfterLast(); mCursorData.moveToNext()) {
            Country country = new Country();
            country.setKey(mCursorData.getInt(COL_KEY));
            country.setName(mCursorData.getString(COL_NAME));
            country.setFlag(mCursorData.getString(COL_FLAG_LINK));
            country.setGDP(mCursorData.getString(COL_GDP));
            country.setPopulation(mCursorData.getString(COL_POPULATION));
            country.setArea(mCursorData.getString(COL_AREA));
            country.setLanguage(mCursorData.getString(COL_LANGUAGE));
            country.setDescription(mCursorData.getString(COL_DESCRIPTION));
            mCountryList.add(country);
        }


    }
}

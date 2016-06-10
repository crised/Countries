package com.example.countries;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.countries.data.CountryContract;
import com.example.countries.net.CountriesService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int COUNTRY_LOADER = 0;


    private AdView mAdView;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private CountryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    static final int COL_ID = 0;
    static final int COL_KEY = 1;
    static final int COL_NAME = 2;
    static final int COL_FLAG_LINK = 3;
    static final int COL_GDP = 4;
    static final int COL_POPULATION = 5;
    static final int COL_LOCATION = 6;
    static final int COL_LANGUAGE = 7;
    static final int COL_DESCRIPTION = 8;

    static final String COUNTRY_ID = "COUNTRY_ID";

    private boolean isTablet;


    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar_tablet);
            isTablet = true;

        }
        setSupportActionBar(mToolbar);


        //Admob
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Start IntentService
        Intent countryIntent = new Intent(this, CountriesService.class);
        startService(countryIntent);

        //TextView mText1 = (TextView) findViewById(R.id.text1);

        getSupportLoaderManager().initLoader(COUNTRY_LOADER, null, this);

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CountryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // use a linear layout manager
        if (isTablet) {
            mLayoutManager = new GridLayoutManager(this, 2);

        } else {
            mLayoutManager = new LinearLayoutManager(this);

        }
        mRecyclerView.setLayoutManager(mLayoutManager);

    }


    @Override
    protected void onResume() {
        super.onResume();
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
        mAdapter.swapCursor(data);
        // mCursorData = data;
        //setCountries();
        //mText1.setText(mCountryList.get(1).getName());


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }

    public void onItemSelected(int countryId, CountryAdapter.CountryAdapterViewHolder viewHolder) {

        Intent i = new Intent(this, DetailActivity.class);

        i.putExtra(COUNTRY_ID, countryId);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                viewHolder.mFlag, "profile"); //profile->xml

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(i, options.toBundle());
        } else startActivity(i);

    }

}

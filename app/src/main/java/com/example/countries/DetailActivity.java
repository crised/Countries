package com.example.countries;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.countries.data.CountryContract;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import static com.example.countries.MainActivity.COL_DESCRIPTION;
import static com.example.countries.MainActivity.COL_FLAG_LINK;
import static com.example.countries.MainActivity.COL_LOCATION;
import static com.example.countries.MainActivity.COL_NAME;
import static com.example.countries.MainActivity.COUNTRY_ID;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {

    private static final int COUNTRY_LOADER = 0;

    private int mCountryId;
    private Cursor mCursor;

    private Toolbar mToolbar;
    private ImageView mFlag;
    private TextView mDescription;

    private String mDescriptionString;
    private String mCountryName;
    private String mLocation;

    private GoogleMap mGooglemap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCountryId = getIntent().getIntExtra(COUNTRY_ID, 0);
        getSupportLoaderManager().initLoader(COUNTRY_LOADER, null, this);


        setContentView(R.layout.activity_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        mFlag = (ImageView) findViewById(R.id.flag);

        mDescription = (TextView) findViewById(R.id.description);
        mDescription.setTypeface(Typeface.createFromAsset(getAssets(), "Slabo27px-Regular.ttf"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mDescriptionString);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(COUNTRY_LOADER, null, this);
    }

    private void setViews() {
        if (mCursor == null || mCursor.isClosed()) return;
        mCursor.moveToPosition(mCountryId);
        mDescriptionString = mCursor.getString(COL_DESCRIPTION);
        mDescription.setText(mDescriptionString);
        Picasso.with(this).load(mCursor.getString(COL_FLAG_LINK)).into(mFlag);
        mCountryName = mCursor.getString(COL_NAME);
        mToolbar.setTitle(mCountryName);
        mLocation = mCursor.getString(COL_LOCATION);
        Log.d("LOCATAG", mLocation);

        //mCursor.close();


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
        mCursor = data;
        setViews();
        setMapCamera();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;

    }

    @Override
    public void onMapReady(GoogleMap map) {

        mGooglemap = map;
        setMapCamera();


    }

    private void setMapCamera() {
        if (mGooglemap == null || mLocation == null) return;
        String[] latLong = mLocation.split(",");
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1])));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(2);
        mGooglemap.moveCamera(center);
        mGooglemap.animateCamera(zoom);
    }


}

package com.example.countries;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.countries.MainActivity.*;

/**
 * Created by crised on 08-06-16.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryAdapterViewHolder> {

    private static final String LOG_TAG = CountryAdapter.class.getSimpleName();


    private Cursor mCursor;
    final private Context mContext;

    public CountryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public class CountryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mName;
        public final ImageView mFlag;

        public CountryAdapterViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.name);
            mFlag = (ImageView) view.findViewById(R.id.flag);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public CountryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            view.setFocusable(true);
            return new CountryAdapterViewHolder(view);
        } else throw new RuntimeException("Not bound to RecyclerView");

    }

    @Override
    public void onBindViewHolder(CountryAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.mName.setText(mCursor.getString(COL_NAME));
        Log.d(LOG_TAG, mCursor.getString(COL_FLAG_LINK));
        Picasso.with(mContext).load(mCursor.getString(COL_FLAG_LINK)).into(holder.mFlag);


    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }
}

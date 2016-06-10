package com.example.countries.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.countries.MainActivity;
import com.example.countries.R;
import com.example.countries.data.CountryContract;

/**
 * Created by crised on 09-06-16.
 */
public class TopProviderIntentService extends IntentService {


    public TopProviderIntentService() {
        super("TopProviderIntentService");
    }

    String first, second, third;

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this.getPackageName(),
                TopProvider.class.getName()));


        callContentProvider();

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.app_widget);
            views.setTextViewText(R.id.widget_title, getString(R.string.widget_title));
            views.setTextViewText(R.id.widget_first, getString(R.string.widget_first_prefix) + first);
            views.setTextViewText(R.id.widget_second, getString(R.string.widget_second_prefix) + second);
            views.setTextViewText(R.id.widget_third, getString(R.string.widget_third_prefix) + third);

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                views.setContentDescription(R.id.widget_main,
                        getString(R.string.widget_content_description_old));


            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_main, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }

    private void callContentProvider() {

        Cursor cursor = getContentResolver().query(CountryContract.CountryEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor == null) return;
        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        int COL_NAME = 2;
        first = cursor.getString(COL_NAME);
        cursor.moveToNext();
        second = cursor.getString(COL_NAME);
        cursor.moveToNext();
        third = cursor.getString(COL_NAME);
        cursor.close();
        return;


    }
}

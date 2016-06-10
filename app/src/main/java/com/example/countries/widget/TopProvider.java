package com.example.countries.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by crised on 09-06-16.
 */
public class TopProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("TAG", "onUpdate");
        context.startService(new Intent(context, TopProviderIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.d("TAG", "onAppWidgetOptionsChanged");

        context.startService(new Intent(context, TopProviderIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        Log.d("TAG", "onReceive");
        context.startService(new Intent(context, TopProviderIntentService.class));

    }
}

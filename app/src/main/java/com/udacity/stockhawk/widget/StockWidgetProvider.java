package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.ui.DetailsActivity;

/**
 * Created by nopesal on 06/04/2017.
 */

public class StockWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(QuoteSyncJob.ACTION_DATA_UPDATED)) {
            AppWidgetManager am = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, StockWidgetProvider.class));
            am.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stock_widget_list_view);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the app widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stock_appwidget);
            remoteViews.setRemoteAdapter(R.id.stock_widget_list_view, new Intent(context, ListWidgetService.class));

            Intent detailIntent = new Intent(context, DetailsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.stock_widget_list_view, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(i, R.id.stock_widget_list_view);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

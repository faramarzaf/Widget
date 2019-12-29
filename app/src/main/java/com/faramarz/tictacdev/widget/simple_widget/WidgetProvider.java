package com.faramarz.tictacdev.widget.simple_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.faramarz.tictacdev.widget.R;

public class WidgetProvider extends AppWidgetProvider {

    // for toast listview elements
    public static final String KEY_ITEM = "com.faramarz.tictacdev.widget.simple_widget.KEY_ITEM";
    public static final String TOAST_ACTION = "com.faramarz.tictacdev.widget.simple_widget.TOAST_ACTION";

    // for toast listview elements
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TOAST_ACTION)) {
            String listItem = intent.getStringExtra(KEY_ITEM);
            Toast.makeText(context, listItem, Toast.LENGTH_SHORT).show();
            // below for update color of widget
            onUpdate(context, AppWidgetManager.getInstance(context), null);
            /* or below one
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            */
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // to say android every new widget has own functionality. if we use appWidgetIds --> only bg of one widget will be changed :(

        int[] realAppWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WidgetProvider.class));


        for (int id : realAppWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.simple_widget_layout);

            Intent serviceIntent = new Intent(context, WidgetService.class);
            remoteViews.setRemoteAdapter(R.id.listView, serviceIntent);


            int r = (int) (Math.random() * 0xff);
            int g = (int) (Math.random() * 0xff);
            int b = (int) (Math.random() * 0xff);
            int color = (0xff << 24) + (r << 16) + (g << 8) + b;
            remoteViews.setInt(R.id.frameLayout, "setBackgroundColor", color);

            Intent intent = new Intent(context, WidgetProvider.class);
            // intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            //for toast on list items
            intent.setAction(TOAST_ACTION);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, realAppWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // for click on frame layout without listview just blank layout click listener -->  remoteViews.setOnClickPendingIntent(R.id.frameLayout, pendingIntent);
            remoteViews.setPendingIntentTemplate(R.id.listView, pendingIntent);

            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }




}

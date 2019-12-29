package com.faramarz.tictacdev.widget.simple_widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.INotificationSideChannel;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.faramarz.tictacdev.widget.R;

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    String[] list = {"Android", "Java", "Kotlin", "C++", "C#", "Python", "Ruby"};

    public WidgetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item);
        remoteViews.setTextViewText(R.id.textView, list[position]);

        // for click on listview elements
        Intent intent = new Intent();
        intent.putExtra(WidgetProvider.KEY_ITEM, list[position]);
        remoteViews.setOnClickFillInIntent(R.id.list_item,intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

# Widget  


<p align="center">
  <img src="https://img.gadgethacks.com/img/60/79/63634089280641/0/12-best-android-widgets-for-getting-things-done.1280x600.jpg"
  width=700 height=250  /> 
</p>


Create a new layout resource file named `list_widget.xml`    

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   android:layout_margin="@dimen/widget_margin">

   <LinearLayout
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:background="#bbDEDFDE"
       android:orientation="vertical">

       <ListView
           android:id="@+id/widget_list"
           android:layout_width="match_parent"
           android:layout_height="match_parent">
       </ListView>
   </LinearLayout>

</FrameLayout>
```

**Populating the collection widget**  

Next, we need to create a data provider for our ListView.   
Create a new Java class named `DataProvider.java` and add the following:  

```java
public class DataProvider implements RemoteViewsService.RemoteViewsFactory {

   List<String> myListView = new ArrayList<>();
   Context mContext = null;

   public DataProvider(Context context, Intent intent) {
       mContext = context;
   }

   @Override
   public void onCreate() {
       initData();
   }

   @Override
   public void onDataSetChanged() {
       initData();
   }

   @Override
   public void onDestroy() {

   }

   @Override
   public int getCount() {
       return myListView.size();
   }

   @Override
   public RemoteViews getViewAt(int position) {
       RemoteViews view = new RemoteViews(mContext.getPackageName(),
       simple_list_item_1);
               view.setTextViewText(text1, myListView.get(position));
               return view;
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

   private void initData() {
       myListView.clear();
       for (int i = 1; i <= 15; i++) {
           myListView.add("ListView item " + i);
       }

   }
  }

```

To create an Android widget, you need to create several files.  

Our first widget-specific file is an `AppWidgetProvider`, which is a BroadcastReceiver where you’ll define the various widget lifecycle methods, such as the method that’s called when your widget is first created and the method that’s called when that widget is eventually deleted.  

Create a new Java class `(File > New > Java Class)` named *CollectionWidget*.

To start, all widget provider files must extend from the AppWidgetProvider class. We then need to load the `list_widget.xml` layout resource file into a RemoteViews object, and inform the AppWidgetManager about the updated RemoteViews object:  
```java
public class CollectionWidget extends AppWidgetProvider {

   static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                     int appWidgetId) {

//Instantiate the RemoteViews object//

       RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_widget);
           setRemoteAdapter(context, views);

//Request that the AppWidgetManager updates the application widget//

       appWidgetManager.updateAppWidget(appWidgetId, views);

   }

```


**Create the adapter**  

Since we’re displaying our data in a ListView, we need to define a `setRemoteAdapter()` method in our AppWidgetProvider.  
The `setRemoteAdapter()` is equivalent to calling `AbsListView.setRemoteViewsAdapter()` but is designed to be used in application widgets.  
In this method, we need to define the id of the AdapterView `(R.id.widget_list)` and the intent of the service that’ll eventually provide the data to our RemoteViewsAdapter.  

```java
private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
   views.setRemoteAdapter(R.id.widget_list,new Intent(context, WidgetService.class));
}
```

The `onUpdate()` widget lifecycle method is responsible for updating your widget’s Views with new information.  
This method is called each time:  

The user performs an action that manually triggers the `onUpdate()` method. The application’s specified update interval has elapsed.  
The user places a new instance of this widget on their homescreen. An `ACTION_APPWIDGET_RESTORED` broadcast intent is sent to the AppWidgetProvider.  
This broadcast intent is triggered if the widget is ever restored from backup.  


When you call `onUpdate()`, you need to specify whether you’re updating every instance of this widget, or a specific instance only.  
If you want to update every instance, then you can use appWidgetIds, which is an array of IDs that identifies every instance across the device.  
```java
@Override
public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
   for (int appWidgetId : appWidgetIds) {

//Update all instances of this widget//

       updateAppWidget(context, appWidgetManager, appWidgetId);
   }
   super.onUpdate(context, appWidgetManager, appWidgetIds);
}
```

**onEnabled: Performing the initial setup**  

The `onEnabled()` lifecycle method is called in response to `ACTION_APPWIDGET_ENABLED`, which is sent when an instance of your widget is added to the homescreen for the first time.  
If the user creates two instances of your widget, then `onEnabled()` will be called for the first instance, but not for the second.  

The `onEnabled()` lifecycle method is where you should perform any setup that’s required for all instances of your widget, such as creating the database that’ll feed your widget information.  
```java
@Override
public void onEnabled(Context context) {
   Toast.makeText(context,"onEnabled called", Toast.LENGTH_LONG).show();
}
```




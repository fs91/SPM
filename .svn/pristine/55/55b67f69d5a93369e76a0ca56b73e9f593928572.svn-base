/**
 * 
 */
package edu.purdue.spm;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.purdue.spm.adapter.TaskListBaseAdapter;
import edu.purdue.spm.component.CreatePersonalTaskPanelFragment;
import edu.purdue.spm.component.TaskInformationPanelFragment;
import edu.purdue.spm.entity.Task;
import edu.purdue.spm.util.LoadBitmap;
import edu.purdue.spm.util.Portal;
import edu.purdue.spm.util.PullToRefreshListView;
import edu.purdue.spm.util.StopWatch;
import edu.purdue.spm.util.YahooWeatherToolKit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import android.text.format.Time;


public class MainDashBoardFragment extends Fragment {

	public static ArrayList<Task> fullTaskList;
	public static ArrayList<Task> partialTaskList;	
	// Partial List maintains the tasks to be displayed in the drop down listview. 
	// The changes subject to date picker, 
	// the list view only displays tasks that due on or after the selected date
	
	LocationManager locationManager = null;
	LocationListener locationListener = null;
	View base;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist.  The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed.  Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		base = inflater.inflate(R.layout.main_dashboard_layout, container, false);		// IMPORTANT: This is for accessing any object in the fragment layout

		final CalendarView calendarView = (CalendarView) base.findViewById(R.id.calendarView);
		/*------------------------------V Load the Fragments for Task Information Display and Task Creation V----------------------------------------*/

		// Create new fragment and transaction
		final Fragment taskDetailFragment = new TaskInformationPanelFragment();
		final Fragment createNewTaskFragment = new CreatePersonalTaskPanelFragment();


		//	getFragmentManager().beginTransaction().replace(R.id.task_information_panel_container, taskDetailFragment).commit();

		//	getFragmentManager().beginTransaction().replace(R.id.task_information_panel_container, createNewTaskFragment).commit();

		/*------------------------------V Task Creation Buttons V----------------------------------------*/
		createNewTaskFragment.getView();

		/*------------------------------V Task PullToRefresh_ListView V----------------------------------------*/
		fullTaskList = updateTaskListFromServerAndLocalStorage();
		partialTaskList = (ArrayList<Task>) fullTaskList.clone();
		partialTaskList.add(null);

		final edu.purdue.spm.util.PullToRefreshListView task_ListView = (edu.purdue.spm.util.PullToRefreshListView) base.findViewById(R.id.task_list_listview);			// This how you access objects in the fragment layout

		if(task_ListView == null){	Log.i("MSG", "taskList is null !");	}
		//	final ViewPager viewpager = (ViewPager) getActivity().findViewById(R.id.viewpager);		// This is how you access objects in the activity which loaded this fragment

		task_ListView.setLockScrollWhileRefreshing(true);
		task_ListView.setAdapter(new TaskListBaseAdapter(container.getContext(), partialTaskList));

		task_ListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
				Task selectedTask = (Task) task_ListView.getItemAtPosition(position+1);		// This +1 is introduced after we migrate from listview to pulltorefreshlistview
				if(selectedTask != null){
					Log.i("MSG", "Selected Task is "+selectedTask.getTitle()+" with ID: "+selectedTask.getTaskID());
					((TaskInformationPanelFragment) taskDetailFragment).setTaskDetail(selectedTask);
					getFragmentManager().beginTransaction().replace(R.id.task_information_panel_container, taskDetailFragment).commit();
					try{
						TaskInformationPanelFragment.populateTaskSpecPanel();	// UGLY: It looks like in the very beginning the fragment is created 'later' than we call this
					}catch(Exception e){
						Log.e("MSG", e.getMessage()+"");	// Exception occurs when first loading the fragment
					}
				}else{
					getFragmentManager().beginTransaction().replace(R.id.task_information_panel_container, createNewTaskFragment).commit();
				}	
			}
		});
		Log.i("MSG", "partialTaskList.size is "+partialTaskList.size());

		task_ListView.setOnRefreshListener(new edu.purdue.spm.util.PullToRefreshListView.OnRefreshListener() {

			public void onRefresh() {
				// Your code to refresh the list contents goes here
				// Make sure you call listView.onRefreshComplete()
				// when the loading is done. This can be done from here or any
				// other place, like on a broadcast receive from your loading
				// service or the onPostExecute of your AsyncTask.

				// For the sake of this sample, the code will pause here to
				// force a delay when invoking the refresh

				calendarView.setDate(StopWatch.getTimeInLong()+86400000);
				calendarView.setDate(StopWatch.getTimeInLong());			// UGLY trick

				task_ListView.postDelayed(new Runnable() {

					@Override
					public void run() {
						task_ListView.onRefreshComplete();
					}
				}, 2000);
			}
		});

		/*------------------------------V Exit Button & Pop up Window V----------------------------------------*/
		final Button exit_button = (Button) base.findViewById(R.id.exit_button);
		exit_button.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {

				View popupView = inflater.inflate(R.layout.exit_confirmation_popup_window, null);  
				final PopupWindow popupWindow = new PopupWindow(
						popupView, 
						LayoutParams.WRAP_CONTENT,  
						LayoutParams.WRAP_CONTENT);  

				Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
				btnDismiss.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
					}
				});

				Button confirm_exit_button = (Button)popupView.findViewById(R.id.confirm_exit_button);
				confirm_exit_button.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						System.exit(0);
					}
				});

				//		popupWindow.showAsDropDown(exit_button, 50, -30);
				popupWindow.showAtLocation(base, Gravity.CENTER, 0, 0);
			}});


		/*------------------------------V Location Service V----------------------------------------*/
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				Log.i("MSG", location.toString());
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		Log.i("TRACK", "locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) is"+locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
		double latitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
		double longitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
		Log.i("MSG", "Location is "+latitude+", "+longitude);


		/*------------------------------V Weather Service V----------------------------------------*/
		Log.i("MSG", YahooWeatherToolKit.obtainWeatherInfo("2517274"));
		YahooWeatherToolKit.MyWeather weatherObj = YahooWeatherToolKit.obtainWeatherObj("2517274");

		Bitmap bimage = LoadBitmap.loadImageFromURL(weatherObj.imgURL);
		((ImageView)base.findViewById(R.id.weather_icon_imageview)).setImageBitmap(bimage);
		((TextView)base.findViewById(R.id.temperature_textview)).setText(weatherObj.temperature+"бу");
		String timeStamp = StopWatch.getTimeInLocale(StopWatch.getTimeInTime());
		String date = timeStamp.split(" ")[0]+" "+timeStamp.split(" ")[1] +" "+ timeStamp.split(" ")[2];
		String time = timeStamp.split(" ")[3]+" "+timeStamp.split(" ")[4];
		((TextView)base.findViewById(R.id.weather_date_textview)).setText(date);
		((TextView)base.findViewById(R.id.weather_time_textview)).setText(time);
		((TextView)base.findViewById(R.id.weather_location_textview)).setText(weatherObj.city+", "+weatherObj.region);



		/*------------------------------V Calendar V----------------------------------------*/
		calendarView.setOnDateChangeListener(new OnDateChangeListener(){

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				ArrayList<Task> tempTaskList = new ArrayList<Task>();
				for(Task t : fullTaskList){
					Time dueTime = t.getDueTime();
					if(dueTime.year >= year && dueTime.month >= month && dueTime.monthDay >= dayOfMonth){
						tempTaskList.add(t);
					}
				}
				partialTaskList = tempTaskList;
				partialTaskList.add(null);
				task_ListView.setAdapter(new TaskListBaseAdapter(container.getContext(), partialTaskList));
			}

		});


		return base;	// This is VERY IMPORTANT! Otherwise the listview will not show anything
	}



	private boolean SQL_saveTask(Task task){
		SQLiteDatabase db = ((ViewPagerFragmentActivity)this.getActivity()).getDB();
		//initialize table
		try{
			db.execSQL("CREATE TABLE IF NOT EXISTS task (" +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"parentTaskId VARCHAR, " +
					"taskId VARCHAR, " +
					"ownerId VARCHAR, " +
					"title VARCHAR, " +
					"description VARCHAR, " +
					"dueTime LONG, " +
					"timeStamp LONG, " +
					"progress INT, " +
					"type INT, " +
					"weight INT, " +
					"depth INT" +
					")");
		} catch (Exception e)
		{
			return false;
		}

		//adjust values
		String ParentTaskID = task.getParentTaskID();
		String taskID = task.getTaskID();
		String ownerID = task.getOwnerID();
		String title = task.getTitle();
		String discription = task.getDiscription();
		//
		Time dueTime = task.getDueTime();
		long dueTimelong = dueTime.toMillis(false);
		Time timeStamp = task.getTimeStamp();
		long timeStamplong = timeStamp.toMillis(false);
		//
		double progress = task.getProgress();
		int progressint = (int)(progress*10000.0);
		char type = task.getType();	// P - personal task, G - group task,
		int typeInt = 0;
		switch(type){
		case 'P':
			typeInt = 0;
			break;
		case 'G':
			typeInt = 1;
			break;
		}
		double weight = task.getWeight();
		int weightInt = (int)(weight*10000.0);
		char depth = task.getDepth();
		int depthInt = 0;
		switch(depth){
		case 'R':
			depthInt = 0;
			break;
		}

		//query
		db.execSQL("INSERT INTO task VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{
				ParentTaskID,
				taskID,
				ownerID,
				title,
				discription,
				dueTimelong,
				timeStamplong,
				progressint,
				typeInt,
				weightInt,
				depthInt
		});

		db.close();

		return true;
	}


	private ArrayList<Task> SQL_readTasksList(){
		SQLiteDatabase db = ((ViewPagerFragmentActivity)this.getActivity()).getDB();
		ArrayList<Task> tasksList = new ArrayList<Task>();

		//query
		Cursor c = db.rawQuery("SELECT * FROM task", null);

		int _id;
		String ParentTaskID = null;
		String taskID = null;
		String ownerID = null;
		String title = null;
		String discription = null;
		Time dueTime = null;
		long dueTimeLong = 0;
		Time timeStamp = null;
		long timeStampLong = 0;
		double progress = 0;
		int progressInt = 0;
		char type = 'P';	// P - personal task, G - group task,
		int typeInt = 0;
		double weight = 0;
		int weightInt = 0;
		char depth = 'R';
		int depthInt = 0;

		//Retrieve tasks
		while (c.moveToNext()) {  
			_id = c.getInt(c.getColumnIndex("_id"));
			ParentTaskID = c.getString(c.getColumnIndex("parentTaskId"));
			taskID = c.getString(c.getColumnIndex("taskId"));
			ownerID = c.getString(c.getColumnIndex("ownerId"));
			title = c.getString(c.getColumnIndex("title"));
			discription = c.getString(c.getColumnIndex("description"));
			dueTimeLong = c.getLong(c.getColumnIndex("dueTime"));
			dueTime = new Time();
			dueTime.set(dueTimeLong);
			timeStampLong = c.getLong(c.getColumnIndex("timeStamp"));
			timeStamp = new Time();
			timeStamp.set(timeStampLong);
			progressInt = c.getInt(c.getColumnIndex("progress"));
			progress = (double)(((double)progressInt)/10000.0);
			typeInt = c.getInt(c.getColumnIndex("type"));
			switch(typeInt){
			case 0:
				type = 'P';
				break;
			case 1:
				type = 'G';
				break;
			}
			weightInt = c.getInt(c.getColumnIndex("weight"));
			weight = (double)(((double)weightInt)/10000.0);
			depthInt = c.getInt(c.getColumnIndex("depth"));
			switch(depthInt){
			case 0:
				depth = 'R';
				break;
			}
			Task newTask = new Task(ParentTaskID, taskID, ownerID, title, discription, dueTime, timeStamp, progress, type, weight, depth);
			tasksList.add(newTask);
		}

		db.close();

		return tasksList;
	}


	private boolean SQL_updateTasksList(ArrayList<Task> tasksList){
		
		SQLiteDatabase db = ((ViewPagerFragmentActivity)this.getActivity()).getDB();
		if(tasksList == null || db == null){
			return false;
		}

		((ViewPagerFragmentActivity)this.getActivity()).clearTaskTable();

		int size = tasksList.size();
		for(int i=0; i<size; i++){
			SQL_saveTask(tasksList.get(i));
		}
		db.close();
		return true;	
	}


	private ArrayList<Task> updateTaskListFromServerAndLocalStorage(){

		Task newTask;
		Time dueTime = new Time();
		Time earlierTime = new Time();
		ArrayList<Task> insList = new ArrayList<Task>();

	//	((ViewPagerFragmentActivity)this.getActivity()).clearTaskTable();

		
		Portal.sendMessageGivenContext(base.getContext(), "ShowTask:;:"+Login.GCMaccount);
		try {	Thread.sleep(3000);	} catch (InterruptedException e) {	e.printStackTrace();	}
		
		Log.i("AAAAA", "MessageBack: "+Portal.messageBack);
		
		String[] buf = Portal.messageBack.split("[(\\[)(\\]<)(><)(>)]");
		for(String s: buf){
			if(s.length() == 0){continue;}
			insList.add(new Task(s));
		}

		SQL_updateTasksList(insList);

		ArrayList<Task> tasksList = SQL_readTasksList();

		return tasksList;
	}


}

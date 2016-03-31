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


public class MainDashBoardFragment extends Fragment {

	public static ArrayList<Task> fullTaskList;
	public static ArrayList<Task> partialTaskList;
	LocationManager locationManager = null;
	LocationListener locationListener = null;
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
		final View base = inflater.inflate(R.layout.main_dashboard_layout, container, false);		// IMPORTANT: This is for accessing any object in the fragment layout
		
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
		fullTaskList = generateDummyTaskList();
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
					Log.i("MSG", "Selected Task is "+selectedTask.getTitle());
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
					Time timeStamp = t.getTimeStamp();
					if(timeStamp.year <= year && timeStamp.month <= month && timeStamp.monthDay <= dayOfMonth){
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




	private ArrayList<Task> generateDummyTaskList(){
		ArrayList<Task> dummyList = new ArrayList<Task>();

		Time earlierTime = new Time();
		earlierTime.set(StopWatch.getTimeInLong()-1000000000);
		Time dueTime = new Time();
		dueTime.set(StopWatch.getTimeInLong()+1000000);
		Task newTask = new Task(null, null, null, "task title 1", "task 1 discription task 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discriptiontask 1 discription", dueTime, StopWatch.getTimeInTime(), 0.5, 'P', 1.0, 'R');
		dummyList.add(newTask);

		dueTime.set(StopWatch.getTimeInLong()+1000000);
		newTask = new Task(null, null, null, "task title 2", "task 2 discription", dueTime, StopWatch.getTimeInTime(), 0.5, 'G', 1.0, 'R');
		dummyList.add(newTask);

		dueTime.set(StopWatch.getTimeInLong()+1000000);
		newTask = new Task(null, null, null, "task title 3", "task 3 discription", earlierTime, StopWatch.getTimeInTime(), 0.5, 'P', 1.0, 'R');
		dummyList.add(newTask);

		dueTime.set(StopWatch.getTimeInLong()+1000000);
		newTask = new Task(null, null, null, "task title 4", "task 4 discription", dueTime, earlierTime, 0.5, 'G', 1.0, 'R');
		dummyList.add(newTask);

		dueTime.set(StopWatch.getTimeInLong()+1000000);
		newTask = new Task(null, null, null, "task title 5", "task 5 discription", dueTime, StopWatch.getTimeInTime(), 0.5, 'P', 1.0, 'R');
		dummyList.add(newTask);

		dueTime.set(StopWatch.getTimeInLong()+1000000);
		newTask = new Task(null, null, null, "task title 6", "task 6 discription", dueTime, StopWatch.getTimeInTime(), 0.5, 'G', 1.0, 'R');
		dummyList.add(newTask);

		dueTime.set(StopWatch.getTimeInLong()+1000000);
		newTask = new Task(null, null, null, "task title 7", "task 7 discription", dueTime, StopWatch.getTimeInTime(), 0.5, 'P', 1.0, 'R');
		dummyList.add(newTask);

		dueTime.set(StopWatch.getTimeInLong()+1000000);
		newTask = new Task(null, null, null, "task title 8", "task 8 discription", dueTime, StopWatch.getTimeInTime(), 0.5, 'P', 1.0, 'R');
		dummyList.add(newTask);

		dueTime.set(StopWatch.getTimeInLong()+1000000);
		newTask = new Task(null, null, null, "task title 9", "task 9 discription", dueTime, StopWatch.getTimeInTime(), 0.5, 'P', 1.0, 'R');
		dummyList.add(newTask);

		return dummyList;
	}


}

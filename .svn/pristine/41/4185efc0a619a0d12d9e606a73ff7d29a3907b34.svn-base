package edu.purdue.spm.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.purdue.spm.Login;
import edu.purdue.spm.MainDashBoardFragment;
import edu.purdue.spm.R;
import edu.purdue.spm.ViewPagerFragmentActivity;
import edu.purdue.spm.entity.Contact;
import edu.purdue.spm.entity.Task;
import edu.purdue.spm.util.Portal;
import edu.purdue.spm.util.StopWatch;
import edu.purdue.spm.util.UUID;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreatePersonalTaskPanelFragment extends Fragment{
	
	View base;
	Task createdTask;
	
	public Task getCreatedTask(){
		return createdTask;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.i("MSG", "CreatePersonalTaskPanelFragment onCreateView !!!");
		
		base = inflater.inflate(R.layout.create_new_personal_task_panel, container, false);
		
		ScrollView view = (ScrollView)base.findViewById(R.id.create_personal_task_ScrollView);
		view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}

		});


		Button saveNewTaskButton = (Button)base.findViewById(R.id.save_new_personal_task_Button);
		saveNewTaskButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createdTask = obtainUserCreatedTask();
				
				MainDashBoardFragment.fullTaskList.add(createdTask);
				
				Date dueDate = new Date(createdTask.getDueTime().toMillis(true));

				String dueTime = String.format("%d-%02d-%02d %02d:%02d:%02d", 1900+dueDate.getYear(), dueDate.getMonth(), dueDate.getDate(), dueDate.getHours(), dueDate.getMinutes(), dueDate.getSeconds());
				
				Log.i("WOWOW", "duetime: "+dueTime);
//				
				
				String request = "AddTask:;:"+Login.GCMaccount+":;:<"+createdTask.getTaskID()+":.:"+null+":.:'"+Login.GCMaccount+"':.:'"+createdTask.getTitle()+
						"':.:'"+createdTask.getDiscription()+"':.:'"+dueTime+"':.:0:.:'P':.:1.0:.:'L'>";
				
				Portal.messageBack = null;
				Portal.sendMessageGivenContext(base.getContext(), request);
				 
				
				while(Portal.messageBack == null){
				//	try {	Thread.sleep(1000);	} catch (InterruptedException e) {	e.printStackTrace();	}
				}
				
				Log.i("TRACK on create task", Portal.messageBack);
				SQL_saveTask(createdTask);
//				MainDashBoardFragment.partialTaskList.remove(MainDashBoardFragment.partialTaskList.size()-1);		// Recall we used the last entry as null in order to allow "add new task"
//				MainDashBoardFragment.partialTaskList.add(createdTask);
//				MainDashBoardFragment.partialTaskList.add(null);
				Toast.makeText(base.getContext(), Portal.messageBack, Toast.LENGTH_LONG).show();
			}
			
		});
		DatePicker datePicker = (DatePicker)base.findViewById(R.id.personal_task_due_datePicker);
		datePicker.setCalendarViewShown(false);
		return base;
	}
	
	public Task obtainUserCreatedTask(){
		String taskID = ((int)(Math.random()*1000))+""; //UUID.randomUUID();
		String taskTitle = ((EditText) base.findViewById(R.id.personal_task_title_textfield)).getText().toString();
		String taskDetail = ((EditText) base.findViewById(R.id.personal_task_detail_TextField)).getText().toString(); 
		
		
		DatePicker datePicker = (DatePicker)base.findViewById(R.id.personal_task_due_datePicker);
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();
		TimePicker timePicker = (TimePicker)base.findViewById(R.id.personal_task_due_timePicker);
		timePicker.clearFocus();
		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		int second = 30;	// Dummy Default value
		Time dueTime = new Time();
		dueTime.set(second, minute, hour, day, month, year);
		Task newTask = new Task(taskID, null, Login.GCMaccount, taskTitle, taskDetail, dueTime, StopWatch.getTimeInTime(), 0, 'P', 1.0, 'R');
		
		return newTask;
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

		((ViewPagerFragmentActivity)this.getActivity()).closeDB();

		return true;
	}
}

package edu.purdue.spm.component;

import edu.purdue.spm.MainDashBoardFragment;
import edu.purdue.spm.R;
import edu.purdue.spm.entity.Task;
import edu.purdue.spm.util.StopWatch;
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
				
//				MainDashBoardFragment.partialTaskList.remove(MainDashBoardFragment.partialTaskList.size()-1);		// Recall we used the last entry as null in order to allow "add new task"
//				MainDashBoardFragment.partialTaskList.add(createdTask);
//				MainDashBoardFragment.partialTaskList.add(null);
			}
			
		});
		DatePicker datePicker = (DatePicker)base.findViewById(R.id.personal_task_due_datePicker);
		datePicker.setCalendarViewShown(false);
		return base;
	}
	
	public Task obtainUserCreatedTask(){
		
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
		Task newTask = new Task(null, null, null, taskTitle, taskDetail, dueTime, StopWatch.getTimeInTime(), 0, 'P', 1.0, 'R');
		
		return newTask;
	}
}

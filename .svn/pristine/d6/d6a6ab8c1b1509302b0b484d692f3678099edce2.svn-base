package edu.purdue.spm.component;

import edu.purdue.spm.Login;
import edu.purdue.spm.MainDashBoardFragment;
import edu.purdue.spm.R;
import edu.purdue.spm.entity.Task;
import edu.purdue.spm.util.Portal;
import edu.purdue.spm.util.StopWatch;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class TaskInformationPanelFragment extends Fragment{

	static View base;
	static Task selectedTaskDetail;
	
	public void setTaskDetail(Task taskDetail){
		this.selectedTaskDetail = taskDetail;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		Log.i("MSG", "TaskInformationPanelFragment onCreateView !!!");
		base = inflater.inflate(R.layout.task_detail_information_panel, container, false);
		
		
		populateTaskSpecPanel();

		Button modifyTaskButton = ((Button)base.findViewById(R.id.modify_task_button));
		modifyTaskButton.setVisibility(modifyTaskButton.INVISIBLE);
		
		Button deleteTaskButton = (Button)base.findViewById(R.id.delete_task_button);
		
		deleteTaskButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				String request = "DeleteTask:;:"+Login.GCMaccount+":;:"+selectedTaskDetail.getTaskID()+"";
				Log.i("HOHOHO", "Request is: "+request);
				Portal.messageBack = null;
				Portal.sendMessageGivenContext(base.getContext(), request);
				while(Portal.messageBack == null){}
				
				Log.i("TRACK on create task", Portal.messageBack);

				Toast.makeText(base.getContext(), Portal.messageBack, Toast.LENGTH_LONG).show();
				
				int i = 0;
				for(i=0; i<MainDashBoardFragment.fullTaskList.size(); i++){
					if (MainDashBoardFragment.fullTaskList.get(i).getTaskID().equals(selectedTaskDetail.getTaskID())){
						break;
					}
				}
				MainDashBoardFragment.fullTaskList.remove(i);
			}
			
		});
        return base;
    }
	
	public static void populateTaskSpecPanel(){
		
		((EditText)base.findViewById(R.id.tasktitle_EditText)).setText(selectedTaskDetail.getTitle());
		((EditText)base.findViewById(R.id.tasktitle_EditText)).setFocusable(false);
		((TextView)base.findViewById(R.id.taskowner_textview)).setText(selectedTaskDetail.getOwnerID());

		((TextView)base.findViewById(R.id.dateassigned_textview)).setText(StopWatch.getTimeInLocale(selectedTaskDetail.getTimeStamp()));

		((TextView)base.findViewById(R.id.duetime_textview)).setText(StopWatch.getTimeInLocale(selectedTaskDetail.getDueTime()));

		((TextView)base.findViewById(R.id.taskprogress_textview)).setText(selectedTaskDetail.getProgress()+"");

		((EditText)base.findViewById(R.id.taskcontent_detail_EditText)).setText(selectedTaskDetail.getDiscription());
		((EditText)base.findViewById(R.id.taskcontent_detail_EditText)).setFocusable(false);
	}
}

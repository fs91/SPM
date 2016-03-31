package edu.purdue.spm.component;

import edu.purdue.spm.R;
import edu.purdue.spm.entity.Task;
import edu.purdue.spm.util.StopWatch;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;

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

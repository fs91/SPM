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

public class TimeLineFragment extends Fragment{

	static View base;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		Log.i("MSG", "TimeLineFragment onCreateView !!!");
		base = inflater.inflate(R.layout.task_detail_information_panel, container, false);
		

        return base;
    }
	

}

/**
 * 
 */
package edu.purdue.spm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


/**
 * @author Binhao Lin
 *
 */
public class GroupDashBoardFragment extends Fragment {

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	AlertDialog.Builder alert;
	Intent login;
	
	Button logoutBtn;
	
	ListView listView;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	//	this.getActivity().setTitle("Group Dash Board");
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
		final View base = inflater.inflate(R.layout.group_dashboard_layout, container, false);		// IMPORTANT: This is for accessing any object in the fragment layout
		
		alert = new AlertDialog.Builder(getActivity());
		login = new Intent(getActivity(), Login.class);
		
		
		logoutBtn = (Button) base.findViewById(R.id.button1);
		logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		logout();
            }
        });
		
		/*listView = (ListView) base.findViewById(R.id.listView1);
        String[] values = new String[] { "Task1", "Task2", "Task3", "Task4", "Task5", "Task6", "Task7"
        		, "Task8", "Task9", "Task10", "Task11", "Task12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_layout, values);
        listView.setAdapter(adapter); 
        */
        
		return base;
	}

	protected void logout() {
		alert.setTitle("Are you sure to log out?");
    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			startActivity(login);
    			System.exit(0);
    		}
    	});
    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			
    		} 
    	});
    	alert.show();
	}
}

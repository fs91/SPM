package edu.purdue.spm;

import com.google.android.gcm.GCMRegistrar;

import edu.purdue.spm.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivityAfterLogin extends Activity {
	
	Button logout;
	ListView listView;
	
	AlertDialog.Builder alert;
	Intent login;
	
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_after_login);
	        alert = new AlertDialog.Builder(this);
	        login = new Intent(this, Login.class);
	        
	        logout = (Button)findViewById(R.id.button1);
	        logout.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	        		logout();
	            }
	        });

	        listView = (ListView) findViewById(R.id.listView1);
	        String[] values = new String[] { "Task1", "Task2", "Task3", "Task4", "Task5", "Task6", "Task7"
	        		, "Task8", "Task9", "Task10", "Task11", "Task12"};
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_layout, values);
	        listView.setAdapter(adapter); 
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
	
	
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	logout();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}

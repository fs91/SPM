package edu.purdue.spm.component;


import java.util.ArrayList;
import java.util.HashMap;

import edu.purdue.spm.R;
import edu.purdue.spm.entity.Contact;
import edu.purdue.spm.util.StopWatch;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.widget.Button;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;


public class ContactViewerFragment extends Fragment {
	static TextView tv;
	
	static HashMap<String, ArrayList<String>> chattingHistoryMap = new HashMap<String, ArrayList<String>>();	// <UserID, list<history>>
	static RelativeLayout base;
	
	static String tempTargetID;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		base = (RelativeLayout) inflater.inflate(R.layout.chat_panel_fragment, container, false);

		Button mButton = (Button) base.findViewById(R.id.sendbutton);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText editText = (EditText)getView().findViewById(R.id.edit_message);
				String message = editText.getText().toString()+"\n";
				tv = (TextView)getView().findViewById(R.id.text);
				StopWatch sw = new StopWatch();
				Time time = new Time();
				message = "You said:("+sw.getTimeInLocale(time)+") \n           "+message;
				tv.append(message);
				if(chattingHistoryMap.get(tempTargetID) == null){
					chattingHistoryMap.put(tempTargetID, new ArrayList<String>());
				}
				chattingHistoryMap.get(tempTargetID).add(message);
			}
		});

		return base;

	}
	
	public static void populateContactInfoPanel(Contact contact)
	{
		tempTargetID = contact.getUser_ID();
		tv = (TextView)base.findViewById(R.id.info);
		String temp = contact.getFirstName()+" ";
		tv.setText(temp);
		temp = contact.getLastName()+"\n";
		tv.append(temp);
		temp = contact.getPhone()+"\n";
		tv.append(temp);
		temp = contact.getAddress()+"\n";
		tv.append(temp);

	}
	
	public static void populateContactHistoryPanel(Contact contact)
	{
		tv = (TextView)base.findViewById(R.id.text);
		ArrayList<String> tmp = chattingHistoryMap.get(tempTargetID);
		if(tmp==null){
			return;
		}
		for(String i: tmp)
		{
			tv.append(i);
		}
	}
	
	public static void clearChat()
	{
		tv = (TextView)base.findViewById(R.id.text);
		tv.setText("");
	}


}

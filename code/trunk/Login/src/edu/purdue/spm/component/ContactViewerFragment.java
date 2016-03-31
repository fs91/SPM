package edu.purdue.spm.component;


import java.util.ArrayList;
import java.util.HashMap;
import edu.purdue.spm.ContactBookFragment;
import edu.purdue.spm.Login;
import edu.purdue.spm.R;
import edu.purdue.spm.adapter.ContactListBaseAdapter;
import edu.purdue.spm.entity.Contact;
import edu.purdue.spm.util.Portal;
import edu.purdue.spm.util.StopWatch;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.widget.Button;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.format.Time;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;


public class ContactViewerFragment extends Fragment {
	static TextView tv;
	
	static HashMap<String, ArrayList<String>> chattingHistoryMap = new HashMap<String, ArrayList<String>>();	// <UserID, list<history>>
	static RelativeLayout base;
	
	static String tempTargetID;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		base = (RelativeLayout) inflater.inflate(R.layout.chat_panel_fragment, container, false);
		
		//----change textview into scrollview
		
		//-------------------------Deletebutton-----------------------
		Button deleteButton = (Button) base.findViewById(R.id.deletebutton);
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				final View popupView = inflater.inflate(R.layout.changelist_popup_window, null);  
				final PopupWindow popupWindow = new PopupWindow(
						popupView, 
						LayoutParams.WRAP_CONTENT,  
						LayoutParams.WRAP_CONTENT);  
				popupWindow.setFocusable(true);

				TextView popupid = (TextView)popupView.findViewById(R.id.popup_id);
				popupid.setText("Delete a Friend");
				
				Button cancel = (Button)popupView.findViewById(R.id.cancel_change);
				cancel.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
					}
				});

				//Confirm
				Button confirm = (Button)popupView.findViewById(R.id.confirm_change);
				confirm.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						EditText editText = (EditText)popupView.findViewById(R.id.input_id);
						String targetUserID = editText.getText().toString();
						for(int i=0; i< ContactBookFragment.contactNameList.size();i++)
						{
							if(ContactBookFragment.contactNameList.get(i).getUser_ID().equals(targetUserID))
							{
								ContactBookFragment.contactNameList.remove(i);
								break;
							}
						}
						String message = "DeleteFriend:;:"+Login.GCMaccount+":;:"+targetUserID;
						Portal.sendMessageGivenContext(base.getContext(), message);
						ContactBookFragment.contact_ListView.setAdapter(new ContactListBaseAdapter(ContactBookFragment.base.getContext(), ContactBookFragment.contactNameList));
						popupWindow.dismiss();
					}
				});

				//		popupWindow.showAsDropDown(exit_button, 50, -30);
				popupWindow.showAtLocation(base, Gravity.CENTER, 0, 0);
			}});
		

		//-----------------------Add button-----------------------
		Button addButton = (Button) base.findViewById(R.id.addbutton);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				final View popupView = inflater.inflate(R.layout.changelist_popup_window, null);  
				final PopupWindow popupWindow = new PopupWindow(
						popupView, 
						LayoutParams.WRAP_CONTENT,  
						LayoutParams.WRAP_CONTENT);  
				popupWindow.setFocusable(true);

				final TextView popupid = (TextView)popupView.findViewById(R.id.popup_id);
				popupid.setText("Add a Friend");
				
				Button cancel = (Button)popupView.findViewById(R.id.cancel_change);
				cancel.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
					}
				});

				// Confirm add firend button
				Button confirm = (Button)popupView.findViewById(R.id.confirm_change);
				confirm.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						String exist="";
						EditText editText = (EditText)popupView.findViewById(R.id.input_id);
						String targetUserID = editText.getText().toString();
						for(int i=0; i< ContactBookFragment.contactNameList.size();i++)
						{
							if(ContactBookFragment.contactNameList.get(i).getUser_ID().equals(targetUserID))
							{
								exist = "exist";
								break;
							}
						}
						if(!exist.equals("exist"))
						{
							String message = "AddFriend:;:"+Login.GCMaccount+":;:"+targetUserID;
							Portal.sendMessageGivenContext(base.getContext(), message);
							Contact contact = new Contact(Portal.messageBack);
							ContactBookFragment.contactNameList.add(contact);
							ContactBookFragment.contact_ListView.setAdapter(new ContactListBaseAdapter(ContactBookFragment.base.getContext(), ContactBookFragment.contactNameList));
							popupWindow.dismiss();
						}
					}
				});

				//		popupWindow.showAsDropDown(exit_button, 50, -30);
				popupWindow.showAtLocation(base, Gravity.CENTER, 0, 0);
			}});
		
		//---------------Send text button---------------------
		Button mButton = (Button) base.findViewById(R.id.send_button);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText editText = (EditText)getView().findViewById(R.id.edit_message);
				String message = editText.getText().toString();
				//send message to server
				String serverquote = "SendMessage:;:"+Login.GCMaccount+":;:"+tempTargetID+":;:"+message;
				Portal.sendMessageGivenContext(base.getContext(), serverquote);
				//---------
				message = message+"\n";
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

		//-----------History button-----------------------
		Button hButton = (Button)base.findViewById(R.id.historybutton);
		hButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String message = "AddFriend:;:"+Login.GCMaccount+":;:";
				Portal.sendMessageGivenContext(base.getContext(), message);
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
	
	public static void updateChat(String message)
	{
		
	}
	
	public static void clearChat()
	{
		tv = (TextView)base.findViewById(R.id.text);
		tv.setText("");
	}


}

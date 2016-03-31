package edu.purdue.spm;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import edu.purdue.spm.adapter.ContactListBaseAdapter;
import edu.purdue.spm.adapter.TaskListBaseAdapter;
import edu.purdue.spm.component.TaskInformationPanelFragment;
import edu.purdue.spm.component.ContactViewerFragment;
import edu.purdue.spm.entity.Contact;
import edu.purdue.spm.entity.Task;
import edu.purdue.spm.util.Portal;
import edu.purdue.spm.util.StopWatch;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



/**
 * @author mwho
 *
 */
public class ContactBookFragment extends Fragment {
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	ProgressDialog progressDialog;
	public static View base;
	String messageBack;
	static Scanner in;

	public static ArrayList<Contact> contactNameList;
	public static ListView contact_ListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

		Log.i("MSG", "LALALALAL");

		base = inflater.inflate(R.layout.contact_book_layout, container, false);	
		final ContactViewerFragment viewFragment = new ContactViewerFragment();
		getFragmentManager().beginTransaction().replace(R.id.chat_views_container, viewFragment).commit();
		contact_ListView = (ListView) base.findViewById(R.id.contacts_ListView);			// This how you access objects in the fragment layout
		
		// TO DO. First call the server to get the contact list and store it into
		// ContactNameList array and show it in the list view.
		
		
		Portal.sendMessageGivenContext(base.getContext(), "ShowFriend:;:"+Login.GCMaccount);
		contactNameList = getContactListFromServer();
		SQL_updateContactsList(contactNameList);

		Log.i("ArraySize", ""+contactNameList.size());

		contact_ListView.setAdapter(new ContactListBaseAdapter(container.getContext(), contactNameList));
		
		contact_ListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
				
				Contact selectedContact = (Contact) contact_ListView.getItemAtPosition(position);	

				Log.i("MSG", "Selected contact is "+ selectedContact.getFirstName());

				//	((TaskInformationPanelFragment) taskDetailFragment).setTaskDetail(selectedTask);	// Passing parameters
			//	getFragmentManager().beginTransaction().replace(R.id.task_information_panel_container, taskDetailFragment).commit();
				

					ContactViewerFragment.clearChat();
					ContactViewerFragment.populateContactInfoPanel(selectedContact);
					ContactViewerFragment.populateContactHistoryPanel();
// UGLY: It looks like in the very beginning the fragment is created 'later' than we call this

			}
		});

		return base;
	}

	public static ArrayList<Contact> getContactListFromServer(){
		ArrayList<Contact> dummyList = new ArrayList<Contact>();
		String s[] = Portal.messageBack.split("[(\\[)(\\]<)(><)(>)]");
		for(int i = 3;i<s.length;i=i+2)
		{
			System.out.println("~"+i+" :"+s[i]);
			Contact newContact = new Contact(s[i]);
			dummyList.add(newContact);
		}
		return dummyList;
	}
	
	private boolean SQL_saveContact(Contact contact){
		SQLiteDatabase db = ((ViewPagerFragmentActivity)this.getActivity()).getDB();
		try{
			db.execSQL("CREATE TABLE IF NOT EXISTS contact (" +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"user_id VARCHAR, " +
					"firstname VARCHAR, " +
					"lastname VARCHAR, " +
					"phone VARCHAR, " +
					"address VARCHAR" +
					")");
		}
		catch(Exception e)
		{
			return false;
		}
		String user_ID = contact.getUser_ID();
		String firstName = contact.getFirstName();
		String lastName = contact.getLastName();
		String phone = contact.getPhone();
		String address = contact.getAddress();
		
		db.execSQL("INSERT INTO contact VALUES (NULL, ?, ?, ?, ?, ?)", new Object[]{
				user_ID, 
				firstName, 
				lastName, 
				phone, 
				address
		});
		
		db.close();
		
		return true;
	}
	
	private ArrayList<Contact> SQL_readContactsList(){
		SQLiteDatabase db = ((ViewPagerFragmentActivity)this.getActivity()).getDB();
		ArrayList<Contact> contactsList = new ArrayList<Contact>();
		
		Cursor c = db.rawQuery("SELECT * FROM contact", null);
		
		String user_ID = null;
		String firstName = null;
		String lastName = null;
		String phone = null;
		String address = null;
		
		//Retrieve tasks
		while (c.moveToNext()) {
			user_ID = c.getString(c.getColumnIndex("user_id"));
			firstName = c.getString(c.getColumnIndex("firstname"));
			lastName = c.getString(c.getColumnIndex("lastname"));
			phone = c.getString(c.getColumnIndex("phone"));
			address = c.getString(c.getColumnIndex("address"));
			
			Contact newContact = new Contact(user_ID, firstName, lastName, phone, address);
			contactsList.add(newContact);
		}
		
		db.close();
		
		return contactsList;
	}
	
	private boolean SQL_updateContactsList(ArrayList<Contact> contactsList){
		
		SQLiteDatabase db = ((ViewPagerFragmentActivity)this.getActivity()).getDB();
		if(contactsList == null || db == null){
			return false;
		}
		
		((ViewPagerFragmentActivity)this.getActivity()).clearContactTable();
		
		int size = contactsList.size();
		for(int i=0; i<size; i++){
			SQL_saveContact(contactsList.get(i));
		}
		db.close();
		return true;	
	}


	private ArrayList<Contact> generateDummyTaskList(){
		Log.e("SQL", "generating dumming list");
		ArrayList<Contact> dummyList = new ArrayList<Contact>();


		Contact newContact = new Contact("000", "Buster", "Dunsmore","FOUR","FIVE");
		dummyList.add(newContact);

		newContact = new Contact("001", "Di", "Xu","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("002", "Junyu", "Chen","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("003", "Binhao", "Lin","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("004", "Weihan", "Ding","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("005", "Su", "Feng","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("006", "First1", "Last1","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("007", "First2", "Last2","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("008", "First3", "Last3","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("009", "First4", "Last4","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("010", "First5", "Last5","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("011", "First6", "Last6","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("012", "First7", "Last7","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("013", "First8", "Last8","FOUR","FIVE");
		dummyList.add(newContact);
		
		SQL_updateContactsList(dummyList);


		return SQL_readContactsList();
		
	}


}




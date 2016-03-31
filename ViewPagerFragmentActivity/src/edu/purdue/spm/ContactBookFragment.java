package edu.purdue.spm;

import java.util.ArrayList;

import edu.purdue.spm.adapter.ContactListBaseAdapter;
import edu.purdue.spm.adapter.TaskListBaseAdapter;
import edu.purdue.spm.component.TaskInformationPanelFragment;
import edu.purdue.spm.component.ContactViewerFragment;
import edu.purdue.spm.entity.Contact;
import edu.purdue.spm.entity.Task;
import edu.purdue.spm.util.StopWatch;
import android.graphics.Color;
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
import android.widget.AdapterView.OnItemClickListener;



/**
 * @author mwho
 *
 */
public class ContactBookFragment extends Fragment {
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	public static ArrayList<Contact> contactNameList;

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

		final View base = inflater.inflate(R.layout.contact_book_layout, container, false);	
		final ContactViewerFragment viewFragment = new ContactViewerFragment();
		getFragmentManager().beginTransaction().replace(R.id.chat_views_container, viewFragment).commit();
		final ListView contact_ListView = (ListView) base.findViewById(R.id.contacts_ListView);			// This how you access objects in the fragment layout

		contactNameList = generateDummyTaskList();
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
					ContactViewerFragment.populateContactHistoryPanel(selectedContact);
// UGLY: It looks like in the very beginning the fragment is created 'later' than we call this

			}
		});

		return base;
	}







	private ArrayList<Contact> generateDummyTaskList(){
		ArrayList<Contact> dummyList = new ArrayList<Contact>();


		Contact newContact = new Contact("000", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);

		newContact = new Contact("001", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("002", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("003", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("004", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("005", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("006", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("007", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("008", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("009", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("010", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("011", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("012", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);
		newContact = new Contact("013", "TWO", "THREE","FOUR","FIVE");
		dummyList.add(newContact);

		return dummyList;
	}
}

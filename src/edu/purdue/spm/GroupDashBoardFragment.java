/**
 * 
 */
package edu.purdue.spm;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.purdue.spm.component.GroupTaskInformationPanelFragment;
import edu.purdue.spm.component.TaskInformationPanelFragment;
import edu.purdue.spm.entity.Task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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
	
	ListView taskList;
	ArrayAdapter<String> adapter;

	ImageView chartImage;
	int CHART_WIDTH = 600;
	int CHART_HEIGHT = 290;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
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
		
		taskList = (ListView) base.findViewById(R.id.listView1);
		chartImage = (ImageView) base.findViewById(R.id.imageView1); 

		

		final Fragment GrouptaskDetailFragment = new GroupTaskInformationPanelFragment();
		
		taskList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
				chartImage.setImageDrawable(null);
				String selectedItem = taskList.getItemAtPosition(position).toString();
				System.out.println("Selected group task: "+selectedItem);
				getFragmentManager().beginTransaction().replace(R.id.chart_info_container, GrouptaskDetailFragment).commit();
			}
		});

		
		final LinearLayout group_members_label = (LinearLayout) base.findViewById(R.id.membersLabelLinearLayout);
		
		//--------------------Group Names Drop Down Menu--------------------//
		final Spinner group_dropdown_menu = (Spinner) base.findViewById(R.id.spinner1);
		final String [] group_ID = getGroupID("login");
		String [] group_names = getGroupNames(group_ID);
		
	    ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, group_names);
	    LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    group_dropdown_menu.setAdapter(LTRadapter);
	    group_dropdown_menu.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				final ArrayList<String> selectedMembers = new ArrayList<String>();

				
            	//Get the number of groups
            	int group_count = group_dropdown_menu.getCount();
            	
            	//Determine which group is clicked
            	String group_name = group_dropdown_menu.getSelectedItem().toString();
                System.out.println("Selected group: "+group_name + " At index: "+position);
                
                if(position == group_count - 1) {
                	//The last item is "Create Group". If it's clicked, then create a group.
                	System.out.println("Create a group");
                	//group_dropdown_menu.setSelection(0);
                	 
                	//Pop up view for Creating New Group 
     				View popupView = inflater.inflate(R.layout.create_group_popup_window, null);  
    				final PopupWindow popupWindow = new PopupWindow(
    						popupView, 
    						LayoutParams.WRAP_CONTENT,  
    						LayoutParams.WRAP_CONTENT);  
    				popupWindow.setFocusable(true);
    				
    				final EditText group_name_edittext = (EditText)popupView.findViewById(R.id.editText1);
    				
    				//Cancel Button
    				Button dismissBtn = (Button)popupView.findViewById(R.id.button1);
    				dismissBtn.setOnClickListener(new Button.OnClickListener(){
    					@Override
    					public void onClick(View v) {
    						popupWindow.dismiss();
    					}
    				});


    				//Confirm Button
    				Button confirmBtn = (Button)popupView.findViewById(R.id.button2);
    				confirmBtn.setOnClickListener(new Button.OnClickListener(){
    					@Override
    					public void onClick(View v) {
    						if(group_name_edittext.getText().toString().isEmpty()) {
    		        	        Toast.makeText(base.getContext(), "Group Name cannot be empty.", Toast.LENGTH_LONG).show();
    						} else if(selectedMembers.isEmpty()){
    		        	        Toast.makeText(base.getContext(), "Please select at least one group member.", Toast.LENGTH_LONG).show();
    						} else {
    							Log.i("GroupDash", "Creating group with members: ");
    							for(int i=0;i<selectedMembers.size(); i++) {
    								System.out.println(selectedMembers.get(i));
    							}
    							popupWindow.dismiss();
    						}
    					}
    				});
    				
    				TextView group_leader_label = (TextView)popupView.findViewById(R.id.groupLeaderTextView);
    				group_leader_label.setText("Lin185@purdue.edu");
    				
    				 
    				// Group member selection list when creating a group
    				final ListView list = (ListView) popupView.findViewById(R.id.groupMemberlistView);
    				final String [] contact_list = {"Binhao Lin", "Yunyu Chen", "Di Xu", "James", "Lucas", "Tracy"};
    				final ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(getActivity(), R.layout.group_member_listview, contact_list);
    				list.setAdapter(LTRadapter); 
    				list.setOnItemClickListener(new OnItemClickListener() {
    				    @Override
    				    public void onItemClick(AdapterView<?> parent, View view,
    				            int position, long id) {
    				    	System.out.println("position:"+position);
    				    	if(contact_list[position].charAt(contact_list[position].length()-1)=='¡Ì') {
    				    		contact_list[position] = contact_list[position].substring(0, contact_list[position].length()-2);
    				    		deleteMemberToSelectedList(selectedMembers, contact_list[position]);
    				    	} else {
    				    		addMemberToSelectedList(selectedMembers, contact_list[position]);
    				    		contact_list[position] += " ¡Ì";
    				    	}
    				    	LTRadapter.notifyDataSetChanged();
    				    }

    				});
 
    				
    				popupWindow.showAtLocation(base, Gravity.CENTER, 0, 0);
                } else {
                	//Click on one group
                	
                	//Get group tasks and set the tast list
                	String [] group_task = getGroupTasks(group_ID[position]);
                	setTaskList(group_task);
                	
                	//Get group members and set the member label
                	String [] group_members = getGroupMembers(group_ID[position]);
                	setMembersList(group_members_label, group_members);
                	
                	//Draw task progress charts
    				getFragmentManager().beginTransaction().remove(GrouptaskDetailFragment).commit();
                	drawProgressChart(chartImage, group_task);
                }
              
            }



			@Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
		return base;
	}

	
	/* Input: group tasks list
	 * Parse the tasks and generate progress bar chart
	 * Using Google Chart API
	 * Down the chart and set the imageview
	 */
	protected void drawProgressChart(final ImageView chartImg, String[] group_task){
		String task = "[ShowTask]<001:;:111:;:linbinhao:;:Task1:;:Finish UI:;:2013-04-10 18:00:00:;:0.8:;:G:;:0.3:;:L:;:TimeStamp>" +
								"<002:;:111:;:xudi:;:Task2:;:Finish code today:;:2013-04-10 23:59:59:;:0.2:;:G:;:0.3:;:L:;:TimeStamp>" +
								"<003:;:111:;:junyu:;:Task3:;:Design:;:2013-04-11 23:59:59:;:0.1:;:G:;:0.2:;:L:;:TimeStamp>" +
								"<004:;:111:;:ding:;:Task4:;:Testing:;:2013-04-10 22:00:00:;:0.5:;:G:;:0.2:;:L:;:TimeStamp>" +
								"<005:;:111:;:ding:;:Task5:;:Testing:;:2013-04-11 22:00:00:;:0.5:;:G:;:0.2:;:L:;:TimeStamp>" +
								"<006:;:111:;:ding:;:Task6:;:Testing:;:2013-04-12 08:00:00:;:0.5:;:G:;:0.2:;:L:;:TimeStamp>" +
								"<007:;:111:;:ding:;:Task7:;:Testing:;:2013-04-11 18:00:00:;:0.5:;:G:;:0.2:;:L:;:TimeStamp>";
		
		String s[] = task.split("[(\\[)(\\]<)(><)(>)]");
		
		
		String barColors [] = {"ff0000", "ffa000", "00ff00", "FF00FF", "6495ED", "ADFF2F", "FA8072", "66CDAA", "40E0D0", "ff0000"};
		randomizeColorArray(barColors);
		
		int dueTime[] = new int[s.length]; 
		int count = 0;
		int max = 0;
		for(int i=0;i<s.length;i++) {
			String ss[] = s[i].split(":;:");
			if(ss.length > 6) {
				String time = ss[5];
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				formatter.setLenient(false);

				Date curDate = new Date();
				long curMillis = curDate.getTime();

				Date dueDate;
				try {
					dueDate = formatter.parse(time);
					long dueMillis = dueDate.getTime();

					long diff = dueMillis - curMillis;
					if(diff < 0) diff = 0;

					int hour = (int) (((diff / 1000.0)/60.0)/60.0);
					dueTime[count++] = hour;
					if(hour > max) max = hour;
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
		

		//Generate Google Chart API URL
		String url = "http://chart.apis.google.com/chart?";
		
		//Char type
		url += "cht=bvg";
		
		//Char size
		url += "&chs="+CHART_WIDTH+"x"+CHART_HEIGHT;
		
		//Chart Data
		url += "&chd=t:";
		for(int i=0; i<count; i++) {
			url += dueTime[i];
			if(i != count - 1)
				url += ",";
		}
		
		//Chart Axis Range
		url += "&chxr=1,0,"+max;
		
		//Chart Data Scale
		url += "&chds=0,"+max;
		
		//Chart Color
		url += "&chco=";
		for(int i=0; i<count; i++) {
			url += barColors[i];
			if(i != count - 1)
				url += "|";
		}
		
		//Chart Bar Size
		int barSpacing = ( CHART_WIDTH - 65 * count) / count;
		url += "&chbh=50,0,"+barSpacing;
		
		//Chart Axis Type
		url += "&chxt=x,y";
		
		//Chart Axis Label
		url += "&chxl=0:|";
		for(int i=0; i<count; i++) {
			url += "Task"+(i+1) + "|";
		}
		//url += "2:||Tasks||";
		
		//Chart Axis Style
		url += "&chxs=2,000000,12";
		
		//Chart Title
		url += "&chtt=Remaining+Time+(Hours)";
		
		// (Chart Title Style)
		url += "&chts=000000,20";
		
		//Chart Grid lines
		url += "&chg=0,25,5,5";
		
		System.out.println("url: "+url);
		

		
		AsyncTask<Void, Void, Void> downloadImageTask;
		final String final_url = url;
		downloadImageTask = new AsyncTask<Void, Void, Void>() {
			Bitmap pic;
			@Override
			protected Void doInBackground(Void... params) {
				
				 try {
			            URL image_url = new URL(final_url);
			            URLConnection conn = image_url.openConnection();
			            pic = BitmapFactory.decodeStream(conn.getInputStream());
			           // chartImg.setImageBitmap(pic);   
				 } catch (Exception ex) {
				 }
				return null;
			}
	
			@Override
			protected void onPostExecute(Void result) {
				chartImg.setImageBitmap(pic);
			}
	
		};
		downloadImageTask.execute(null, null, null);

	}

	
	private void randomizeColorArray(String [] barColors){
		for (int i = 0; i < barColors.length; i++) {
			int r = (int) (Math.random() * (i+1));
			String swap = barColors[r];
			barColors[r] = barColors[i];
			barColors[i] = swap;
		}		
	}
	
	
	private String[] getGroupMembers(String group_ID) {
    	String [] group_members = {group_ID + " Leader", group_ID + " Member1", group_ID + " Member2", group_ID + " Member3" };
		return group_members;
	}
	
	private void setMembersList(LinearLayout group_members_label, String [] group_members) {
		group_members_label.removeAllViews();
		for(int i=0; i<group_members.length; i++) {
			TextView tv = new TextView(this.getActivity());
		    tv.setText(group_members[i]);
		    tv.setTextColor(Color.WHITE);
		    tv.setPadding(10, 0, 10, 0);
		    group_members_label.addView(tv);
		}
	}
	
	
	private void deleteMemberToSelectedList(
			ArrayList<String> selectedMembers, String member) {
		selectedMembers.remove(member);
	}
	
	private void addMemberToSelectedList(
			ArrayList<String> selectedMembers, String member) {
		selectedMembers.add(member);
	}
	
	/* Given user account, return group ID of the user from server */
	private String[] getGroupID(String account) {
		String [] group_IDs = {"gid1", "gid2", "gid3", "gid4"};
		return group_IDs;
	}
	
	/* Given group IDs array, return group names array from server */
	private String[] getGroupNames(String[] group_IDs) {
		String [] group_names = {"Group 1", "Group 2", "Group 3", "Group 4", "Create Group"};
		return group_names;
	}
	
	/* Given group ID, return group tasks from server */
    private String[] getGroupTasks(String group_ID) {
    	String [] group_tasks = {group_ID + " task1", group_ID + " task2", group_ID + " task3", group_ID + " task4", group_ID + " task5", group_ID + " task6", group_ID + " task7" };
		return group_tasks;
	}
    

	private void setTaskList(String [] tasks) {
        adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.group_task_listview, tasks);
        taskList.setAdapter(adapter); 
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

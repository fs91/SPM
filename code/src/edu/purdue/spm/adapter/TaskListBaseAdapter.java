package edu.purdue.spm.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import edu.purdue.spm.R;
import edu.purdue.spm.entity.Task;
import edu.purdue.spm.util.StopWatch;


import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListBaseAdapter extends BaseAdapter {
	private static ArrayList<Task> taskDetailList;

	/*
	private Integer[] imgid = {
			R.drawable.p1,
			R.drawable.bb2,
			R.drawable.p2,
			R.drawable.bb5,
			R.drawable.bb6,
			R.drawable.d1
			};
	 */

	private LayoutInflater l_Inflater;

	public TaskListBaseAdapter(Context context, ArrayList<Task> results) {
		taskDetailList = results;
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return taskDetailList.size();
	}

	public Object getItem(int position) {
		return taskDetailList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.task_tuple, null);
			holder = new ViewHolder();
			holder.task_name = (TextView) convertView.findViewById(R.id.task_name_textview);
			holder.task_timeLeft = (TextView) convertView.findViewById(R.id.time_left_textview);
			holder.task_badge = (ImageView) convertView.findViewById(R.id.owner_badge_imageview);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(taskDetailList.get(position)==null){
			holder.task_name.setText("Create New Task");
			holder.task_timeLeft.setText("");
			return convertView;
		}
		
		holder.task_name.setText(taskDetailList.get(position).getTitle());

		holder.task_timeLeft.setText(StopWatch.getTimeDifferenceInString(Calendar.getInstance().getTimeInMillis(), taskDetailList.get(position).getDueTime().toMillis(false)));
		//		holder.itemImage.setImageResource(imgid[taskDetailList.get(position).getImageNumber() - 1]);
		//		imageLoader.DisplayImage("http://192.168.1.28:8082/ANDROID/images/BEVE.jpeg", holder.itemImage);

		return convertView;
	}

	static class ViewHolder {
		TextView task_name;
		TextView task_timeLeft;
		ImageView task_badge;
	}
}

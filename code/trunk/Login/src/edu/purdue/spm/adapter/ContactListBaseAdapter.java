package edu.purdue.spm.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import edu.purdue.spm.R;
import edu.purdue.spm.entity.Contact;
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

public class ContactListBaseAdapter extends BaseAdapter {
	private static ArrayList<Contact> contactDetailList;

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

	public ContactListBaseAdapter(Context context, ArrayList<Contact> results) {
		contactDetailList = results;
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return contactDetailList.size();
	}

	public Object getItem(int position) {
		return contactDetailList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.contact_tuple, null);
			holder = new ViewHolder();
			holder.contact_name = (TextView) convertView.findViewById(R.id.contact_name_textview);
			holder.contact_status = (TextView) convertView.findViewById(R.id.contact_status_textview);
			holder.contact_picture = (ImageView) convertView.findViewById(R.id.contact_picture_imageview);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Log.i("MSG", holder.contact_name+"");
		
		holder.contact_name.setText(contactDetailList.get(position).getFirstName()+" "+contactDetailList.get(position).getLastName());

		holder.contact_status.setText("Active");
		//		holder.itemImage.setImageResource(imgid[taskDetailList.get(position).getImageNumber() - 1]);
		//		imageLoader.DisplayImage("http://192.168.1.28:8082/ANDROID/images/BEVE.jpeg", holder.itemImage);

		return convertView;
	}

	static class ViewHolder {
		TextView contact_name;
		TextView contact_status;
		ImageView contact_picture;
	}
}

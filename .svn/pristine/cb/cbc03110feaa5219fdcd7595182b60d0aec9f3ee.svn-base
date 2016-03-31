package edu.purdue.spm.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.purdue.spm.Login;

import android.text.format.Time;

public class Task {

	String taskID;
	String ParentTaskID;
	String ownerID;
	String title;
	String discription;
	Time dueTime;
	Time timeStamp;
	double progress;
	char type;	// G - group task, P - personal task
	double weight;
	char depth;


	public Task(String raw){
		String[] buf = raw.split(":;:");
		for(int i=0; i< buf.length; i++){
			System.out.println("@"+i+" "+buf[i]);
		}
		taskID = buf[0];
		ParentTaskID = buf[1];
		ownerID = Login.GCMaccount;
		title = buf[2];
		discription = buf[3];

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Date temp = null;
		try {	temp = format.parse(buf[4]);	} catch (ParseException e) {	e.printStackTrace();	}
		dueTime = new Time();
		dueTime.set(temp.getTime());
		
		progress = Double.parseDouble(buf[5]);
		type = buf[6].charAt(0);
		weight = Double.parseDouble(buf[7]);
		depth = buf[8].charAt(0);
		
		try {	temp = format.parse(buf[9]);	} catch (ParseException e) {	e.printStackTrace();	}
		timeStamp = new Time();
		timeStamp.set(temp.getTime());
	}


	public Task(String taskID, String parentTaskID, String ownerID,
			String title, String discription, Time dueTime, Time timeStamp,
			double progress, char type, double weight, char depth) {
		super();
		ParentTaskID = parentTaskID;
		this.taskID = taskID;
		this.ownerID = ownerID;
		this.title = title;
		this.discription = discription;
		this.dueTime = dueTime;
		this.timeStamp = timeStamp;
		this.progress = progress;
		this.type = type;
		this.weight = weight;
		this.depth = depth;
	}


	public String getParentTaskID() {
		return ParentTaskID;
	}


	public void setParentTaskID(String parentTaskID) {
		ParentTaskID = parentTaskID;
	}


	public String getTaskID() {
		return taskID;
	}


	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}


	public String getOwnerID() {
		return ownerID;
	}


	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDiscription() {
		return discription;
	}


	public void setDiscription(String discription) {
		this.discription = discription;
	}


	public Time getDueTime() {
		return dueTime;
	}


	public void setDueTime(Time dueTime) {
		this.dueTime = dueTime;
	}


	public Time getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(Time timeStamp) {
		this.timeStamp = timeStamp;
	}


	public double getProgress() {
		return progress;
	}


	public void setProgress(double progress) {
		this.progress = progress;
	}


	public char getType() {
		return type;
	}


	public void setType(char type) {
		this.type = type;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public char getDepth() {
		return depth;
	}


	public void setDepth(char depth) {
		this.depth = depth;
	}	



}

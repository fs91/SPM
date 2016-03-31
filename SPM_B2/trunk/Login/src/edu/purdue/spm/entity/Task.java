package edu.purdue.spm.entity;

import android.text.format.Time;

public class Task {
	
	String ParentTaskID;
	String taskID;
	String ownerID;
	String title;
	String discription;
	Time dueTime;
	Time timeStamp;
	double progress;
	char type;	// G - group task, P - personal task
	double weight;
	char depth;
	
	
	public Task(String parentTaskID, String taskID, String ownerID,
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

package edu.purdue.spm.entity;

import android.text.format.Time;

public class ChatHistory {

	
	String from_User_id;
	String to_User_id;
	Time timeStamp;
	String content;
	
	public ChatHistory(String from_User_id, String to_User_id, Time timeStamp,
			String content) {
		super();
		this.from_User_id = from_User_id;
		this.to_User_id = to_User_id;
		this.timeStamp = timeStamp;
		this.content = content;
	}

	public String getFrom_User_id() {
		return from_User_id;
	}

	public void setFrom_User_id(String from_User_id) {
		this.from_User_id = from_User_id;
	}

	public String getTo_User_id() {
		return to_User_id;
	}

	public void setTo_User_id(String to_User_id) {
		this.to_User_id = to_User_id;
	}

	public Time getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Time timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
}

package edu.purdue.spm.entity;


import java.util.ArrayList;

import edu.purdue.spm.util.StopWatch;

import android.text.format.Time;

public class Group {

	
	String group_id;
	String group_name;
	String teamLeader_id;
	ArrayList<String> teamMember_list;
	Time timeStamp;
	
	public Group(String group_id, String group_name, String teamLeader_id,
			ArrayList<String> teamMember_list) {
		super();
		this.group_id = group_id;
		this.group_name = group_name;
		this.teamLeader_id = teamLeader_id;
		this.teamMember_list = teamMember_list;
		this.timeStamp = StopWatch.getTimeInTime();
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getTeamLeader_id() {
		return teamLeader_id;
	}

	public void setTeamLeader_id(String teamLeader_id) {
		this.teamLeader_id = teamLeader_id;
	}

	public ArrayList<String> getTeamMember_list() {
		return teamMember_list;
	}

	public void setTeamMember_list(ArrayList<String> teamMember_list) {
		this.teamMember_list = teamMember_list;
	}

	public Time getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Time timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	
}

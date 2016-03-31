package edu.purdue.spm.util;

import java.util.Calendar;
import java.util.Date;

import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

public class StopWatch {

	
	public static int getTimeInSec(){
		Calendar c = Calendar.getInstance(); 
		int seconds = c.get(Calendar.SECOND);
		return seconds;
	}
	
	public static int getTimeInMin(){
		Calendar c = Calendar.getInstance(); 
		int minutes = c.get(Calendar.MINUTE);
		return minutes;
	}
	
	public static int getTimeInHour(){
		Calendar c = Calendar.getInstance(); 
		int hours = c.get(Calendar.HOUR);
		return hours;
	}
	
	public static long getTimeInLong(){
		Time timeStamp = new Time();
		timeStamp.set(Calendar.getInstance().getTimeInMillis());
		return timeStamp.toMillis(false);
	}
	
	public static Time getTimeInTime(){
		Time timeStamp = new Time();
		timeStamp.set(Calendar.getInstance().getTimeInMillis());
		return timeStamp;
	}
	
	public static String getTimeInGMT(Time timeStamp){
		return (new Date(timeStamp.toMillis(false))).toGMTString();
	}
	
	public static String getTimeInLocale(Time timeStamp){
		return (new Date(timeStamp.toMillis(false))).toLocaleString();
	}
	
	public static String getTimeDifferenceInString(Time t1, Time t2){
		String timeLeft = (String) DateUtils.getRelativeTimeSpanString(t1.toMillis(false), t2.toMillis(false), 0);
		timeLeft = timeLeft.split(" ago")[0];
		return timeLeft;
	}
	
	public static String getTimeDifferenceInString(long t1, long t2){
		String timeLeft = (String) DateUtils.getRelativeTimeSpanString(t1, t2, 0);
		if(timeLeft.contains("ago")){
			timeLeft = timeLeft.split(" ago")[0];
			return timeLeft;
		}else{
			return "Overdue";
		}
		
	}
}

package com.iceman.yangtze;

public class Lesson {
	private String lessonName;
	private String location;
	private String singleOrDoubleWeek;
	private int weekId;
	private int sectionId;


	public Lesson(String lessonName,String location,String singleOrDoubleWeek,int weekId,int sectionId){
		this.lessonName=lessonName;
		this.location=location;
		this.singleOrDoubleWeek=singleOrDoubleWeek;
		this.weekId=weekId;
		this.sectionId=sectionId;
	}


	public String getLessonName() {
		return lessonName;
	}


	public String getLocation() {
		return location;
	}


	public String getSingleOrDoubleWeek() {
		return singleOrDoubleWeek;
	}


	public int getWeekId() {
		return weekId;
	}


	public int getSectionId() {
		return sectionId;
	}
}

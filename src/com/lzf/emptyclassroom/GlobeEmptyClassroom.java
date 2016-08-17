package com.lzf.emptyclassroom;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class GlobeEmptyClassroom {

	public static List<EmptyClassroom> listOfEmptyClassrooms = new ArrayList<EmptyClassroom>();
	public static List<Result> resultOfEmptyClassrooms = new ArrayList<Result>();
//	public static InputStream is = Context.getAssets().open("sample.txt");
	
	public static void clear(){
		resultOfEmptyClassrooms.clear();
	}
	
}

class EmptyClassroom{
	
	private int dayId;
	private int sectionId;
	private String building;
	private String classrooms;
	private String singleOrDouble;
	
	public EmptyClassroom(int dayId,int sectionId,String building,String classrooms,String singleOrDouble){
		this.dayId = dayId;
		this.sectionId = sectionId;
		this.building = new String(building);
		this.classrooms = new String(classrooms);
		this.singleOrDouble=singleOrDouble;
	}
	
	public int getDayId(){
		return dayId;
	}
	public int getSectionId(){
		return sectionId;
	}
	public String getBuilding(){
		return building;
	}
	public String getClassrooms(){
		return classrooms;
	}
	public String getSingleOrDouble(){
		return singleOrDouble;
	}
}

package com.lzf.emptyclassroom;

public class Result{
	
	private String emptyClassroom;
	private int[] mode;
	
	public Result(String emptyClassroom,int[] mode){
		this.emptyClassroom = new String(emptyClassroom);
		this.mode = new int[5];
		for( int i=0 ; i<5 ; i++ ){
			this.mode[i] = mode[i];
		}
	}
	
	public String getEmptyClassroom(){
		return emptyClassroom;
	}
	
	public int[] getMode(){
		return mode;
	}
	
}
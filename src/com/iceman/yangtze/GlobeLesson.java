package com.iceman.yangtze;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;



import android.os.Environment;

public class GlobeLesson {
	
	public static Lesson[] lessons = new Lesson[30];

	public  GlobeLesson() throws IOException{
		readTimeTableDoc();
	}
	
	public static void writeTimeTableDoc(){
	
		File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/文澜网络");
		if(!myDir.exists()){
			myDir.mkdir();
		}				
		
		File myFile = new File(myDir,"课表.txt");
		try {
			DataOutputStream out = new DataOutputStream( new FileOutputStream(myFile) );
			
			try{
				for( int i=0 ; i<30 ; i++ ){
					
					Lesson thisLesson = GlobeLesson.lessons[i];
					if( thisLesson==null ){
						
						break;
							
					}else{
						
						System.out.println(i);
						out.writeUTF( thisLesson.getSingleOrDoubleWeek() + "label" + thisLesson.getLocation() + "label" + thisLesson.getLessonName() + "label" + thisLesson.getWeekId() + "label" + thisLesson.getSectionId() + "\n");

					}
					
				}
			}catch (ArrayIndexOutOfBoundsException e){
					
			}
			
			out.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static void readTimeTableDoc() throws IOException  {
	    File sdCardDir=Environment.getExternalStorageDirectory();
	    FileInputStream file = new FileInputStream(sdCardDir.getCanonicalPath()+"/文澜网络/课表.txt");//Environment.getExternalStorageDirectory().getAbsolutePath()+
		System.out.println("已进入读文件");
		//InputStreamReader reader = null;
	     try {
	           // reader = new InputStreamReader(file);
	    	 BufferedReader reader=new BufferedReader(new InputStreamReader(file));
	            int i = 0;
	            while(reader.read()!=-1){
	            	
	            	char tempChar;
		            StringBuffer tempString = new StringBuffer();
		            String[] lessonInfo;
		            while ((tempChar = (char) reader.read()) != '\n') {
		                tempString.append(tempChar);               
		            }
		            lessonInfo = tempString.toString().split("label");
		            lessonInfo[0] = new String(lessonInfo[0].substring(1, lessonInfo[0].length()));
		            GlobeLesson.lessons[i] = new Lesson(lessonInfo[2],lessonInfo[1],lessonInfo[0],Integer.parseInt(lessonInfo[3]),Integer.parseInt(lessonInfo[4]));
		            i++;
		            System.out.println(GlobeLesson.lessons[i-1].getLessonName()+GlobeLesson.lessons[i-1].getLocation()+GlobeLesson.lessons[i-1].getSingleOrDoubleWeek()+ String.valueOf(GlobeLesson.lessons[i-1].getWeekId())+String.valueOf(GlobeLesson.lessons[i-1].getSectionId()) );

	            }
	            
	            reader.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 	}
}
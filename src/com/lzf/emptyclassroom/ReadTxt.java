package com.lzf.emptyclassroom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;


public class ReadTxt {

	public static void readTxt(InputStream inputStream){
		try {
			//String encoding="Unicode";
			InputStreamReader read = new InputStreamReader(
					inputStream);
			BufferedReader bufferedReader = new BufferedReader(read);
			String tempString = null;
			while((tempString = bufferedReader.readLine()) != null){
				String[] classroomInfo;
				classroomInfo = tempString.toString().split("LABEL");
				EmptyClassroom eC = new EmptyClassroom(Integer.parseInt(classroomInfo[3])/10,Integer.parseInt(classroomInfo[3])%10,classroomInfo[1],classroomInfo[2],classroomInfo[4]);
				GlobeEmptyClassroom.listOfEmptyClassrooms.add(eC);   	            
			}
			read.close();
			System.out.println("list"+GlobeEmptyClassroom.listOfEmptyClassrooms.size());           
		} catch (Exception e) {
			System.out.println("读取文件错误");
		}
	}

	public static void getCurEmptyClassroom(int weekId,int dayId,int[] mode){
		List<EmptyClassroom> emptyClassroomOfThisDay = new ArrayList<EmptyClassroom>();
		Set<String> setOfEmptyClassrooms = new HashSet<String>();
		Set<String> setOfToRemove = new HashSet<String>();
		List<EmptyClassroom> firstSection  = new ArrayList<EmptyClassroom>();
		List<EmptyClassroom> secondSection = new ArrayList<EmptyClassroom>();
		List<EmptyClassroom> thirdSection  = new ArrayList<EmptyClassroom>();
		List<EmptyClassroom> fourthSection = new ArrayList<EmptyClassroom>();
		List<EmptyClassroom> fifthSection  = new ArrayList<EmptyClassroom>();

		for(EmptyClassroom eC:GlobeEmptyClassroom.listOfEmptyClassrooms){
			if(eC.getSingleOrDouble().charAt(weekId-1)=='0'&&eC.getDayId()==dayId){
				emptyClassroomOfThisDay.add(eC);
				switch(eC.getSectionId()){
				case 1:firstSection.add(eC);break;
				case 2:secondSection.add(eC);break;
				case 3:thirdSection.add(eC);break;
				case 4:fourthSection.add(eC);break;
				case 5:fifthSection.add(eC);break;
				}
				setOfEmptyClassrooms.add(eC.getBuilding()+eC.getClassrooms());
			}
		}
		System.out.println(setOfEmptyClassrooms.size());
		for( int i=0 ; i<5 ; i++ ){
			if( mode[i]==0 ){
				continue;
			}else{
				for(String eC:setOfEmptyClassrooms){
					Here:	                	switch(i+1){
					case 1:for(EmptyClassroom thisEC:firstSection){
						if(eC.equals(thisEC.getBuilding()+thisEC.getClassrooms())){
							break Here;
						}

					}setOfToRemove.add(eC);break;
					case 2:for(EmptyClassroom thisEC:secondSection){
						if(eC.equals(thisEC.getBuilding()+thisEC.getClassrooms())){
							break Here;
						}

					}setOfToRemove.add(eC);break;
					case 3:for(EmptyClassroom thisEC:thirdSection){
						if(eC.equals(thisEC.getBuilding()+thisEC.getClassrooms())){
							break Here;
						}

					}setOfToRemove.add(eC);break;
					case 4:for(EmptyClassroom thisEC:fourthSection){
						if(eC.equals(thisEC.getBuilding()+thisEC.getClassrooms())){
							break Here;
						}

					}setOfToRemove.add(eC);break;
					case 5:for(EmptyClassroom thisEC:fifthSection){
						if(eC.equals(thisEC.getBuilding()+thisEC.getClassrooms())){
							break Here;
						}

					}setOfToRemove.add(eC);break;
					}

				}
			}
		}
		setOfEmptyClassrooms.removeAll(setOfToRemove);
		for( String eC : setOfEmptyClassrooms ){
			int[] thisMode = new int[5];
			for( int i=0 ; i<5 ; i++ ){
				thisMode[i] = mode[i];
			}
			for( int i=0 ; i<5 ; i++ ){
				if( thisMode[i]==0 ){
					switch(i+1){
					case 1:for(EmptyClassroom emC:firstSection){
						if((emC.getBuilding()+emC.getClassrooms()).equals(eC)){
							thisMode[i] = 1;
							break;
						}
					}break;
					case 2:for(EmptyClassroom emC:secondSection){
						if((emC.getBuilding()+emC.getClassrooms()).equals(eC)){
							thisMode[i] = 1;
							break;
						}
					}break;
					case 3:for(EmptyClassroom emC:thirdSection){
						if((emC.getBuilding()+emC.getClassrooms()).equals(eC)){
							thisMode[i] = 1;
							break;
						}
					}break;
					case 4:for(EmptyClassroom emC:fourthSection){
						if((emC.getBuilding()+emC.getClassrooms()).equals(eC)){
							thisMode[i] = 1;
							break;
						}
					}break;
					case 5:for(EmptyClassroom emC:fifthSection){
						if((emC.getBuilding()+emC.getClassrooms()).equals(eC)){
							thisMode[i] = 1;
							break;
						}
					}break;
					}
				}else{
					continue;
				}
			}
			Result result = new Result(eC,thisMode);
			GlobeEmptyClassroom.resultOfEmptyClassrooms.add(result);
		}


	}


}

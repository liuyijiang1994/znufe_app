package com.example.final_me;
/**
 * @author wtm
 * Created on 2014-2-10
 *  */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;


/**
 * TextExtractor���ܲ�����.
 */

public class UseDemo {
	
	
/*public static void main(String[] args) throws IOException {
		
		
		String url;
		url="http://wellan.znufe.edu.cn/llyd/5.html";//从这里给到网址
		//forFinalUse(url);
		
		/*List<titleInfo> objectTitleInfo=forFinalUse(url);
		for(int i=0;i<objectTitleInfo.size();i++){
		System.out.println(objectTitleInfo.get(i).getLink());
		System.out.println(objectTitleInfo.get(i).getTitle());
		}*/
		/**
		 * 编码gbk，utf——8方式的选择与适配
		 */
		/*String content = getHTML(url, "gbk");//得到html的代码		
		String initPassage=TextExtract.parse(content);//调用哪个类里面的方法	
		//System.out.println(initPassage);
		List<String > titleList = dealToEachTitle(getPassage(initPassage), ")");
		
		
		List<String>linkList1= divideToTitle( titleList);//获取纯文本标题内容，为了给后面的那个抓链接用的
		for(int i=1;i<linkList1.size();i++){
		    System.out.println(i+linkList1.get(i));	}
		
		
		//都从1开始哈，0开始的那条怎么都是不对的
		for(int i =0;i<titleList.size();i++){
		System.out.println(i+titleList.get(i));	}
		System.out.println("++++++++++++++++++++++++++++++++++++++");
		List<String>linkList=dealToEachLink(linkList1,content);
		for(int i=1;i<linkList.size();i++){
		    System.out.println(i+linkList.get(i));	}
		
		
		
		
		
	}
*/

   /* public static List<titleInfo> forFinalUse(String url) throws IOException{
    	
    	String content = getHTML(url, "gbk");//得到html的代码		
		String initPassage=TextExtract.parse(content);//调用哪个类里面的方法	
		
		List<titleInfo> ti=new  ArrayList<titleInfo>();//用来存放每条title的信息
		titleInfo temp=new titleInfo();
		//System.out.println(initPassage);
		
		//获取到带有时间的标题
		List<String > titleList = dealToEachTitle(getPassage(initPassage), ")");
		
		//获取纯文本标题内容，为了给后面的那个抓链接用的
		List<String>linkList1= divideToTitle( titleList);
		//for(int i=0;i<linkList1.size();i++){
		  //  System.out.println(i+linkList1.get(i));	}
		
		
		//以下24为新闻的条目数，最好别改，就能输出这么多了
		//for(int i =0;i<titleList.size();i++){
		//System.out.println(i+titleList.get(i));	}
		
		//System.out.println("++++++++++++++++++++++++++++++++++++++");
		
		//获取到链接的内容
		List<String>linkList=dealToEachLink(linkList1,content);
		//for(int i=0;i<linkList.size();i++){
		  //  System.out.println(i+linkList.get(i));	}
		
		
		//把分开的信息放到每个对象中
		for(int i=0;i<linkList.size();i++){
			temp.setLink(linkList.get(i));
			temp.setTitle(titleList.get(i));
			ti.add(temp);
		}
		
		return ti;
		
		
		
    	
    	
    }*/
	public static String getHTML(String strURL,String codeWay) throws IOException {
		URL url = new URL(strURL);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),codeWay));
		String s = "";
		StringBuilder sb = new StringBuilder("");
		while ((s = br.readLine()) != null) {
			sb.append(s + "\n");
			//System.out.println(sb.toString());给到html的源码
		}
	
		return sb.toString();
	}
	
	public static String getPassage(String initPassage){
		String passage;
		passage=initPassage.replaceAll("<.*?>", "");
		passage=passage.replaceAll("<", "");
		return passage;	
	}
	//把一整个大的string，就是都是各种标题的那一个大字符串，分割成一个个的小title，然后放到数组里面，再转到字符串里面去
	public static List<String> dealToEachTitle(String titleString,String keyString) throws UnsupportedEncodingException, IOException
	{  	
		List<String> list = new ArrayList<String>();//定义一个list，用来放标题		
		String str = titleString;//定义一个 字符串
		String temp;
		StringTokenizer token = new StringTokenizer(str, keyString);//按照空格和逗号进行截取 			
		for(int i=0;i<30;i++) { 
		    temp=token.nextToken()+")";
		    //Log.v("temp",temp+"  "+i);
		    //temp=temp.replace("<", "");
		    list.add(temp);//将分割开的子字符串放入数组中
		} 
		return list;
		}
	
	//将标题的文字与实践分解开来
	public static List<String>divideToTitle(List<String> titleList){
		List<String> pureTitle=new ArrayList<String>();//就是纯粹的放不带有标题的时间
		int start=0;//对于每一个来说都是从0位置开始截取
		String strEnd="(";
		int end;
		String temp;
		for(int i=0;i<titleList.size();i++){
			end=titleList.get(i).indexOf(strEnd);
			temp=titleList.get(i).substring(start, end);
			pureTitle.add(i, temp);
			//System.out.println(temp);
			
		}
		return pureTitle;	
	}
	
	//将标题文字与时间分割开来,only acquire time
	public static List<String>divideToTime(List<String> titleList){
		List<String> pureTime=new ArrayList<String>();//就是纯粹的放不带有标题的时间
		int start;//对于每一个来说都是从0位置开始截取
		String strStart="(";
		int end;
		String strEnd=")";		
		String temp;
		for(int i=0;i<titleList.size();i++){
			start=titleList.get(i).indexOf(strStart);
			end=titleList.get(i).indexOf(strEnd);
			temp=titleList.get(i).substring(start+1, end);
			pureTime.add(i, temp);
			System.out.println(temp);			
		}
		return pureTime;	
	}
	public static List<String> dealToEachLink(List<String> titleList,String contentString){
		    int index=0;
		    List<String> linkList=new ArrayList<String>();	
			String strAll = contentString ;//html	
			strAll=strAll.substring(strAll.indexOf("<div class=\"center_box1\">"));
			int start;
			int end;
			
			//System.out.print("1st");
			String strStart = "<a href" ;
			start = strAll.indexOf(strStart) ;
			//strAll=strAll.substring(start);
			//System.out.println(strAll);
				
			for(int i=0;i<30;i++){		
				//strAll=strAll.substring(start);
				//System.out.println(strAll);
			//String strStart = "<a href" ;
			//之所以是25，是不能把全部的都输出来
			String strEnd = titleList.get(i);
			//System.out.print(strEnd);		
			end=strAll.indexOf(strEnd, start);
			
			if(end==-1)
				end=start+100;
			//System.out.println("++++++++++++++++++++"+start);
			 //end = strAll.indexOf(strEnd) ;
				//System.out.println("开始的字符"+strStart);System.out.println("开始字符的位置"+start);	
			//System.out.println("结束的字符"+strEnd);System.out.println("结束字符的头两个字的位置"+end);
			
			// String result = strAll.substring(start,end) ;
			String result="";result = strAll.substring(start,end) ;
			 start=end;
			 result = drawCh(result) ;
			 linkList.add(result);//将分割开的子字符串放入数组中 
			 index++; 
			 //System.out.println("----------------------------------------------------------------");
			}
			return linkList;
	}
	
	
	
	public static String drawCh(String str){
		StringBuffer strBuf = new StringBuffer() ;
		String regex="<a href=\"(.*?).html\" (.*?)>";//<a href="/ttxw/2/28925.html" target="_blank">
		Matcher matcher = Pattern.compile(regex).matcher(str);
		if(matcher.find()){
			str = strBuf.append(matcher.group(0)).toString() ;
		}
		str=str.replaceAll("<a href=", "");
		str=str.replaceAll("target=\"_blank\">", "");
		str=str.replaceAll("\"","");
		str="http://wellan.znufe.edu.cn"+str;//它的文件都是用的相对路径，这里一定要给他转成 
		//System.out.println(str);
		return str ;
	}
	
	
}
	
	
	


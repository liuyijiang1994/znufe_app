package net.shopnc.android.common;

import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BBCodeHelper {

		private static Pattern pattern_ncsmiley = Pattern.compile("\\[ncsmiley\\]([^\\[\\]]*)\\[/ncsmiley\\]", Pattern.MULTILINE);
		//private static Pattern pattern_image2 = Pattern.compile("\\[img=\\d{4},\\d{3}\\]([^\\[\\]]*)\\[/img\\]", Pattern.MULTILINE);
		//private static Pattern pattern_image = Pattern.compile("\\[img\\]([^\\[\\]]*)\\[/img\\]", Pattern.MULTILINE);
		private static Pattern pattern_pic = Pattern.compile("\\[img\\]([^\\[\\]]*)\\[/img\\]",  Pattern.MULTILINE);
		private static Pattern pattern_link = Pattern.compile("\\[url=([^\\[\\]]*)\\]([^\\[\\]]*)\\[/url\\]",  Pattern.MULTILINE);
		private static Pattern bbcode = Pattern.compile("\\[[^\\[\\]]*\\]");
		private static Pattern pattern_attach = Pattern.compile("\\[attach\\]([^\\[\\]]*)\\[/attach\\]",  Pattern.MULTILINE);
		
		private static Pattern pattern_href= Pattern.compile("^http://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$");
		
		private static String img_html = "<img src=''{0}''/>";
		private static String link_html = "<a href=''{0}''>{1}</a>";
		
		/**
		 * 提取img并去掉标签
		 * @param src
		 * @param al
		 * @return
		 */
		public static String processImage(String src, LinkedHashSet<String> al){
			String result = null;
			Matcher matcher = pattern_pic.matcher(src);
			while(matcher.find()){
				String url = matcher.group(1);
				if(pattern_href.matcher(url).matches()){
					al.add(url);
				}
			}
			result = matcher.replaceAll("");
			
			return result;
		}
		
		/**
		 * 把URL转成超链接
		 * @param src
		 * @return
		 */
		public static String processURL(String src){
			for(Matcher matcher = pattern_link.matcher(src);
					matcher.find(); matcher = pattern_link.matcher(src)){
				
				String url = matcher.group(1);
				String content = matcher.group(2);
				String dest = MessageFormat.format(link_html, url, content);
				
				src = matcher.replaceFirst(dest);
			}
			return src;
		}
		
		/**
		 * 把附件标签及内容全部去掉
		 * @param src
		 * @return
		 */
		public static String processAttach(String src){
			return src.replaceAll(pattern_attach.pattern(), "");
		}
		
		/**
		 * 把表情改成图片
		 * @param src
		 * @return
		 */
		public static String processNcsmiley(String src){
			for(Matcher matcher = pattern_ncsmiley.matcher(src);
					matcher.find(); matcher = pattern_ncsmiley.matcher(src)){
				
				String url = matcher.group(1);
				String dest = MessageFormat.format(img_html, url);
				dest = dest.replaceAll("\\\\", "/");
				src = matcher.replaceFirst(dest);
			}
			return src;
		}
//		/**
//		 * 过滤掉嵌入图标签
//		 * @param src
//		 * @return
//		 */
//		public static String processImage2(String src){
//			for(Matcher matcher = pattern_image.matcher(src);
//					matcher.find(); matcher = pattern_image.matcher(src)){
//				
//				String url = matcher.group(1);
//				String dest = MessageFormat.format(img_html, url);
//				dest = dest.replaceAll("\\\\", "/");
//				src = matcher.replaceFirst(dest);
//			}
//			return src;
//		}
//		/**
//		 * 过滤掉嵌入图标签2
//		 * @param src
//		 * @return
//		 */
//		public static String processImage3(String src){
//			for(Matcher matcher = pattern_image2.matcher(src);
//					matcher.find(); matcher = pattern_image2.matcher(src)){
//				
//				String url = matcher.group(1);
//				String dest = MessageFormat.format(img_html, url);
//				dest = dest.replaceAll("\\\\", "/");
//				src = matcher.replaceFirst(dest);
//			}
//			return src;
//		}
		/**
		 * 过滤掉嵌入图标签
			 * @param src
			 * @return
			 */
		public static  String parseHtml(String str) {
//			if(str.indexOf("[/img]") > -1) {
//				str = str.replaceAll("\\[/img\\]", "' />///");			
//			}else{
//				return str;
//			}		
//			String[] urls = str.split("\\///");
// 			int tagBeginIdx, tagEndIdx;		
//			String regx, html = "";
//			for(int i = 0; i < urls.length; ++i) {
//				System.out.println("urls["+i+"]"+urls[i]);
//				tagBeginIdx = urls[i].indexOf("[img");
//				tagEndIdx = urls[i].indexOf("]");			
//				if(tagBeginIdx > -1 && tagEndIdx > - 1 && tagBeginIdx < tagEndIdx ) { 
//					System.out.println("执行了么。。。");
//					regx = urls[i].substring(tagBeginIdx, tagEndIdx + 1).replaceAll("\\[", "\\\\[");
//					regx = regx.replaceAll("\\]", "\\\\]");
//					html += urls[i].replaceAll(regx, "<img src='");
//				}else{
//					html += urls[i];
//				}
//			}
			if(str.indexOf("[/img]") > -1) {
				str = str.replaceAll("\\[/img\\]", "' />///");			
			}else{
				return str;
			}		
			String[] urls = str.split("\\///");
 			int tagBeginIdx, tagEndIdx;		
			String regx, regx2,html = "";
			for(int i = 0; i < urls.length; ++i) {
				tagBeginIdx = urls[i].indexOf("[img");
				if(tagBeginIdx > -1) { 
					regx = urls[i].substring(tagBeginIdx);
					regx2=urls[i].replace(regx,"");
					tagEndIdx = regx.indexOf("]");	
					if(tagEndIdx>-1){
						regx=regx.replace(regx.substring(0,tagEndIdx+1), "<img src='");
					}
					html += (regx2+regx);
				}else{
					html += urls[i];
				}
			}
				return html;
		}
		/**
		 * 过滤<img src=''/>标签*/
		public static  String parseHtmlExcludeImgTag(String str) {
			int start_idx = -1, end_idx = -1;
			String text = "";
			String subString;
			int i = 0;		
			do {		
				start_idx = str.indexOf("<img", i);
				end_idx = str.indexOf("/>", i + 1);
				if(end_idx == -1) end_idx = str.indexOf(">");
				if(start_idx > -1 && end_idx > -1) {
					subString = str.substring(i, start_idx);
					if(!subString.equals(""))
						text += "<span>" + subString + "</span>";
					i = end_idx + 2;				
				}
				else {
					subString = str.substring(i);
					if(!subString.equals(""))
						text += "<span>" + str.substring(i) + "</span>";
					break;
				}
			} while(start_idx > -1);		
			return text;
		}
		/**
		 * 忽略BBCode
		 * @param src
		 * @return
		 */
		public static String ignoreBBCode(String src){
			return bbcode.matcher(src).replaceAll("");
		}
		
		/**
		 * 处理BBCode，并获取其中出现的图片
		 * @param src
		 * @param al
		 * @return
		 */
		public static String processBBCode(String src, LinkedHashSet<String> al){
			String result = src.replaceAll("\\x0a|\\x0d","");
			
			result = processImage(result, al);
			//result = processImage2(result);
			//result = processImage3(result);
			result=parseHtml(result);
			result = processURL(result);
			result = processNcsmiley(result);
			result = processAttach(result);
			result = ignoreBBCode(result);
			
			return result;
		}
		
		/**
		 * 处理BBCode
		 * @param src
		 * @return
		 */
		public static String processBBCode(String src){
			String result = src.replaceAll("\\x0a|\\x0d","");
			result=parseHtml(result);
			//result = processImage2(src);
			//result = processImage3(src);
			result = processURL(result);
			result = processAttach(result).replaceAll("<img", "<img style=\"width:100%;\"");
			result = processNcsmiley(result);
			result = ignoreBBCode(result);
			return result;
		}
		
		
		public static void main(String[] args) {
			String src = "a[ncsmiley]http://www.tjitcast.com/image/cc.gif[/ncsmiley]a " +
					"ad[url=aaaa]aaaa[/url]fad  adsfad[ncsmiley]" +
					"http://www.tjitcast.com/image/dd.gif[/ncsmiley] opwer" +
					"[img]http://www.phpbb.com/images/phplogo.gif[/img]sadf[img]" +
					"http://www.phpbb.com/images/phplogo.gif[/img]sa[img]" +
					"http://www.phpbb.com/images/111.gif[/img]df" + 
					"[url=bbb]bbb[/url]";
			
			
			LinkedHashSet<String> al = new LinkedHashSet<String>();
			System.out.println(processBBCode(src, al));
			
			System.out.println(al);
		}
}

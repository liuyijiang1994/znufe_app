package com.example.final_me;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * ������ʱ���ڳ�ȡ�����ࣨ���š����͵ȣ���ҳ�����ġ�
 * ������<b>�����п�ֲ�����</b>�ķ�����Ϊ����ͨ����û������ض���վ��д����
 * </p>
 * @author  wtm
 */
public class TextExtract {
	
	private static List<String> lines;
	private final static int blocksWidth;
	private static int threshold;
	private static String html;
	private static boolean flag;
	private static int start;
	private static int end;
	private static StringBuilder text;
	private static ArrayList<Integer> indexDistribution;
	
	static {
		lines = new ArrayList<String>();
		indexDistribution = new ArrayList<Integer>();
		text = new StringBuilder();
		blocksWidth = 3;
		flag = false;
		/* �����ȡ����ҳ��������ɿ�����ű���δ�޳�ʱ��ֻҪ������ֵ���ɡ�*/
		/* ��ֵ���׼ȷ�������ٻ����½���ֵ��С�������󣬵����Ա�֤�鵽ֻ��һ�仰������ */
		threshold	= 80;   
	}
	
	public static void setthreshold(int value) {
		threshold = value;
	}

	/**
	 * ��ȡ��ҳ���ģ����жϸ���ҳ�Ƿ���Ŀ¼�͡�����֪����Ŀ϶��ǿ��Գ�ȡ���ĵ���������ҳ��
	 * 
	 * @param _html ��ҳHTML�ַ�
	 * 
	 * @return ��ҳ����string
	 */
	public static String parse(String _html) {
		return parse(_html, false);
	}
	
	/**
	 * �жϴ���HTML��������������ҳ�����ȡ���ģ��������<b>"unkown"</b>��
	 * 
	 * @param _html ��ҳHTML�ַ�
	 * @param _flag true�����������ж�, ʡ�Դ˲�����Ĭ��Ϊfalse
	 * 
	 * @return ��ҳ����string
	 */
	public static String parse(String _html, boolean _flag) {
		flag = _flag;
		html = _html;	//System.out.println(html);
		preProcess();
		//System.out.println(html);
		return getText();
	}
	
	private static void preProcess() {
		//System.out.print(html);
		html = html.replaceAll("(?is)<!DOCTYPE.*?>", "");
		html = html.replaceAll("(?is)<!--.*?-->", "");				// remove html comment
		html = html.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
		html = html.replaceAll("(?is)<style.*?>.*?</style>", "");   // remove css
		//html = html.replaceAll("(?is)<a.*?>.*?</a>", "");
		html = html.replaceAll("&.{2,5};|&#.{2,5};", " ");			// remove special char
		
		html = html.replaceAll("(?is)<iframe.*?>", "");
		html = html.replaceAll("(?is)<input.*?>", "");
		html = html.replaceAll("(?is)<ins.*?>", "");
		html = html.replaceAll("<[d|D|L|l|a|A|s|S|/|u|U|o|O|t|T|h|H|?|M|m|E|e|f|F](.*?)>", "");
		//html = html.replaceAll("<[d|D|L|l|s|S|/|u|U|o|O|t|T|h|H|?|M|m|E|e|f|F](.*?)>", "");
		//<!--[if !IE]>|xGv00|9900d21eb16fa4350a3001b3974a9415<![endif]--> 
		html=html.replaceAll("|xGv00|9900d21eb16fa4350a3001b3974a9415<![endif]", "");
		
		//html = html.replaceAll("(?is)<(?!p|a|A|/p)(.*?)>", "");
		html = html.replaceAll("(?is)<(?!p|P|img|IMG|/p)(.*?)>", "");		
		html = html.replaceAll("(?is)<iframe.*?>", "");
		html = html.replaceAll("(?is)<input.*?>", "");
		html = html.replaceAll("(?is)<ins.*?>", "");
		html = html.replaceAll("<IMG|img(.*?)>", "");
		//System.out.print(html);
	}
	
	private static String getText() {
		lines = Arrays.asList(html.split("\n"));
		indexDistribution.clear();
		
		for (int i = 0; i < lines.size() - blocksWidth; i++) {
			int wordsNum = 0;
			for (int j = i; j < i + blocksWidth; j++) { 
				lines.set(j, lines.get(j).replaceAll("\\s\\s+", ""));
				wordsNum += lines.get(j).length();
			}
			indexDistribution.add(wordsNum);
			//System.out.println(lines.size());
			//System.out.print(wordsNum+",");
		}
		
		start = -1; end = -1;
		boolean boolstart = false, boolend = false;
		text.setLength(0);
		
		for (int i = 0; i < indexDistribution.size() - 1; i++) {
			if (indexDistribution.get(i) > threshold && ! boolstart) {
				if (indexDistribution.get(i+1).intValue() != 0 
					|| indexDistribution.get(i+2).intValue() != 0
					|| indexDistribution.get(i+3).intValue() != 0) {
					boolstart = true;
					start = i;
					continue;
				}
			}
			if (boolstart) {
				if (indexDistribution.get(i).intValue() == 0 
					|| indexDistribution.get(i+1).intValue() == 0) {
					end = i;
					boolend = true;
				}
			}
			StringBuilder tmp = new StringBuilder();
			if (boolend) {
				//System.out.println(start+1 + "\t\t" + end+1);
				for (int ii = start; ii <= end; ii++) {
					if (lines.get(ii).length() < 5) continue;
					tmp.append(lines.get(ii) + "\n");
				}
				String str = tmp.toString();
				//System.out.println(str);
				if (str.contains("Copyright")  || str.contains("��Ȩ����") ) continue; 
				text.append(str);
				boolstart = boolend = false;
			}
		}
		return text.toString();
	}
}

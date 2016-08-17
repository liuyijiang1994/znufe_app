/**
 *  ClassName: Tools.java
 *  created on 2012-5-4
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.common;

/**
 * @author hjgang
 */
public class Tools {
	public Tools() {
	}

	/**
	 * 字符串按字节截取
	 * 
	 * @param str
	 *            原字符
	 * @param len
	 *            截取长度
	 * @return String
	 * @author hjgang
	 * @since 2006.07.20
	 */
	public static String splitString(String str, int len) {
		return splitString(str, len, "...");
	}

	/**
	 * 字符串按字节截取
	 * 
	 * @param str
	 *            原字符
	 * @param len
	 *            截取长度
	 * @param elide
	 *            省略符
	 * @return String
	 * @author hjgang
	 * @since 2006.07.20
	 */
	public static String splitString(String str, int len, String elide) {
		if (str == null) {
			return "";
		}
		byte[] strByte = str.getBytes();
		int strLen = strByte.length;
		int elideLen = (elide.trim().length() == 0) ? 0
				: elide.getBytes().length;
		if (len >= strLen || len < 1) {
			return str;
		}
		if (len - elideLen > 0) {
			len = len - elideLen;
		}
		int count = 0;
		for (int i = 0; i < len; i++) {
			int value = (int) strByte[i];
			if (value < 0) {
				count++;
			}
		}
		if (count % 2 != 0) {
			len = (len == 1) ? len + 1 : len - 1;
		}
		return new String(strByte, 0, len) + elide.trim();
	}

	public static void test(String str) {
		byte[] bc = null;
		int byteCount = 0;
		bc = str.getBytes();
		byteCount = bc.length;
		System.out.println("byteCount:" + byteCount);
	}

	/**
	 * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
	 * 
	 * @author hjgang
	 * @param String
	 *            origin, 原始字符串
	 * @param int len, 截取长度(一个汉字长度按2算的)
	 * @return String, 返回的字符串
	 */
	public static String substring(String origin, int len) {
		if (origin == null || origin.equals(""))
			return "";
		byte[] strByte = new byte[len];
		if (len > origin.length())
			return origin;
		System.arraycopy(origin.getBytes(), 0, strByte, 0, len);
		int count = 0;
		for (int i = 0; i < len; i++) {
			int value = (int) strByte[i];
			if (value < 0) {
				count++;
			}
		}
		if (count % 2 != 0) {
			len = (len == 1) ? ++len : --len;
		}
		return new String(strByte, 0, len);
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param char c, 需要判断的字符
	 * @return boolean, 返回true,Ascill字符
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param String
	 *            s ,需要得到长度的字符串
	 * @return int, 得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	/**
	 * 得到一个字符串的长度,一个汉字或日韩文长度为2,英文字符长度为1,截取前48个
	 * 
	 * @param String
	 *            s ,需要得到长度的字符串
	 * @return int, 得到的字符串长度
	 */
	public static StringBuffer length2(String s) {
		StringBuffer sb = new StringBuffer();
		if (s == null) {
			return null;
		} else {
			char[] c = s.toCharArray();
			int len = 0;
			for (int i = 0; i < c.length; i++) {
				len++;
				if (!isLetter(c[i])) {
					len++;
					System.out.println(i + "+");
				}
				if (len == 48) {
					return sb;
				} else {
					sb.append(c[i]);
				}
			}
			return sb;
		}
	}
}
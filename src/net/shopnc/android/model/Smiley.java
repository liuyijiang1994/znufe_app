package net.shopnc.android.model;

import java.text.MessageFormat;

/**
 * 表情
 * @author qjyong
 */
public class Smiley {
	public static final String BBCODE_TEMPLATE = "[ncsmiley]{0}[/ncsmiley]";
	/** 编码 */
	private String code;
	/** 别名 */
	private String name;
	/** bbcode编码 */
	private String bbcode;
	/** 网络地址 */
	private String path;
	/** 本地图片名 */
	private String localName;
	
	public Smiley(){}

	public Smiley(String code, String path) {
		this.code = code;
		this.path = path;
		this.bbcode = MessageFormat.format(BBCODE_TEMPLATE, this.code);
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public String getPath() {
		return this.path;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public void setCode(String code) {
		this.code = code;
		this.bbcode = MessageFormat.format(BBCODE_TEMPLATE, this.code);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBbcode() {
		return bbcode;
	}

	public void setBbcode(String bbcode) {
		this.bbcode = bbcode;
	}

	@Override
	public String toString() {
		return "Face [code=" + code + ", name=" + name + ", bbcode=" + bbcode
				+ ", path=" + path + ", localName=" + localName + "]";
	}
}

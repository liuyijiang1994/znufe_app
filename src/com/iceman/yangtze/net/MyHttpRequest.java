package com.iceman.yangtze.net;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
/**
 * @author iceman http请求封装类
 */
public class MyHttpRequest {
    private int type = 0;
    private String url;
    private ArrayList<NameValuePair> params;
    private int pipIndex = 0;
    public boolean isBlock;
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public ArrayList<NameValuePair> getParams() {
        return params;
    }
    public void setParams(ArrayList<NameValuePair> params) {
        this.params = params;
    }
    /**
     * 
     * @param 1为get,2为post
     * @param url
     * @param params
     * @param isBlock
     */
    public MyHttpRequest(int type, String url, ArrayList<NameValuePair> params,boolean isBlock) {
        super();
        this.type = type;
        this.url = url;
        this.params = params;
        this.isBlock = isBlock;
    }
    public int getPipIndex() {
        return pipIndex;
    }
    public void setPipIndex(int pipIndex) {
        this.pipIndex = pipIndex;
    }
    
}

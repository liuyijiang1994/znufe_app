package com.iceman.yangtze.net;

import org.jsoup.nodes.Document;
/**
 * @author iceman http响应封装类
 */
public class MyHttpResponse {
    private int pipIndex;
    private Document data;
    public int getPipIndex() {
        return pipIndex;
    }
    public void setPipIndex(int pipIndex) {
        this.pipIndex = pipIndex;
    }
    public Document getData() {
        return data;
    }
    public void setData(Document data) {
        this.data = data;
    }
    public MyHttpResponse(int pipIndex, Document data) {
        super();
        this.pipIndex = pipIndex;
        this.data = data;
    }
    
    
}

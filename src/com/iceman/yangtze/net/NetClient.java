
package com.iceman.yangtze.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.Handler;
import android.os.Message;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.WindowActivity;
import com.special.ResideMenuDemo.StartPage;
/**
 * @author iceman 用来执行网络事件的类
 */
public class NetClient {
    private Cookie mCookie;

    private WindowActivity mActivity;

    private DefaultHttpClient client = new DefaultHttpClient();

    private HttpPost post;

    private HttpGet get;

    private HttpResponse response;

    private static ArrayList<MyHttpRequest> mRequestTemp = new ArrayList<MyHttpRequest>();

    private static ArrayList<MyHttpResponse> mResponseTemp = new ArrayList<MyHttpResponse>();

    public boolean isRunning;

    public Handler mRefreshHandler;

    public NetClient(WindowActivity activity) {
        this.mActivity = activity;
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        mRefreshHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (!mActivity.isFinishing()) {
                    switch (msg.what) {
                        case NetConstant.MSG_REFRESH:
                            if (mResponseTemp.size() != 0) {
                                System.out.println("temp中已有数据,交给activity"+mResponseTemp.size());
                                mActivity.handResponse(mResponseTemp.get(0));
                                mResponseTemp.remove(0);
                            }
                            if (mRequestTemp.size() == 0) {
                                mActivity.dismissNetLoadingDialog();
                            }
                            break;
                        case NetConstant.MSG_SERVER_ERROR:
                            mActivity.dismissNetLoadingDialog();
                            mActivity.showDialog(NetConstant.MSG_SERVER_ERROR);
                            break;
                        case NetConstant.MSG_NET_ERROR:
                            mActivity.dismissNetLoadingDialog();
                            mActivity.showDialog(NetConstant.MSG_NET_ERROR);
                            break;
                        case NetConstant.MSG_NONE_NET:
                            mActivity.dismissNetLoadingDialog();
                            mActivity.showDialog(NetConstant.MSG_NONE_NET);
                            break;

                        default:
                            break;
                    }
                }
            }
        };
        isRunning = true;
        start();
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    if (mRequestTemp.size() != 0) {
                        MyHttpRequest req = mRequestTemp.get(0);
                        if (req.getType() == NetConstant.TYPE_GET) {// 1为get
                            System.out.println("准备发送一个get请求");
                            get = new HttpGet(req.getUrl());
                            // System.out.println("url:"+req.getUrl());
                            // get.addHeader("Cookie", Globe.sCookieString);
                            // System.out.println("添加cookie:"+Globe.sCookieString);
                            try {
                                System.out.println("执行前");
                                response = client.execute(get);
                                System.out.println("执行后");
                                if (response.getStatusLine().getStatusCode() == 200) {
                                	
                                    String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");                      
                                    Document doc = Jsoup.parse(strResult);//这步执行之后mRequestTemp的大小就变为0
                                    System.out.println("enter request队列大小"+mRequestTemp.size());
                                    if(mRequestTemp.size()==0){ mRequestTemp.add(req);}
                                    System.out.println("开始response");
                                    MyHttpResponse resp = new MyHttpResponse(req.getPipIndex(), doc);
                                    mResponseTemp.add(resp);                      
                                    System.out.println("队列大小"+mRequestTemp.size());
                                    mRequestTemp.remove(0);
                                } else {
                                    mRefreshHandler.sendEmptyMessage(NetConstant.MSG_SERVER_ERROR);
                                }
                            } catch (ClientProtocolException e) {
                                mRefreshHandler.sendEmptyMessage(NetConstant.MSG_NET_ERROR);
                                e.printStackTrace();
                            } catch (ConnectTimeoutException e) {
                                mRefreshHandler.sendEmptyMessage(NetConstant.MSG_NET_ERROR);
                            } catch (IOException e) {
                                mRefreshHandler.sendEmptyMessage(NetConstant.MSG_OTHER_ERROR);
                                e.printStackTrace();
                            } finally {
                                get.abort();
                            }
                        } else {// 为post请求
                            System.out.println("准备发送一个post请求");
                            post = new HttpPost(req.getUrl());
                            try {
                                if (req.getPipIndex() != NetConstant.LOGIN) {
                                    // post.addHeader("Cookie",
                                    // Globe.sCookieString);
                                    // System.out.println("添加cookie:" +
                                    // Globe.sCookieString);
                                }
                                post.setEntity(new UrlEncodedFormEntity(req.getParams(), "gb2312"));
                                System.out.println("执行前");
                                response = client.execute(post);
                                System.out.println("执行后");
                                if (response.getStatusLine().getStatusCode() == 200) {
                                    String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
                                    Document doc = Jsoup.parse(strResult);
                                    if (req.getPipIndex() == NetConstant.LOGIN) {
                                        CookieStore store = client.getCookieStore();
                                        List<Cookie> cookies = store.getCookies();
                                        if (cookies.isEmpty()) {
                                        } else {
                                            mCookie = cookies.get(0);
                                            Globe.sCookieString = mCookie.getName() + "="
                                                    + mCookie.getValue();
                                            System.out.println("获得cookie:" + Globe.sCookieString);
                                        }
                                    }
                                    System.out.println("取出数据,放入temp");
                                    MyHttpResponse resp = new MyHttpResponse(req.getPipIndex(), doc);
                                    mResponseTemp.add(resp);
                                    mRequestTemp.remove(0);
                                } else {
                                    mRefreshHandler.sendEmptyMessage(NetConstant.MSG_SERVER_ERROR);
                                }
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (ClientProtocolException e) {
                                mRefreshHandler.sendEmptyMessage(NetConstant.MSG_NET_ERROR);
                                e.printStackTrace();
                            } catch (ConnectTimeoutException e) {
                                mRefreshHandler.sendEmptyMessage(NetConstant.MSG_NET_ERROR);
                            } catch (IOException e) {
                                mRefreshHandler.sendEmptyMessage(NetConstant.MSG_OTHER_ERROR);
                                e.printStackTrace();
                            } finally {
                                post.abort();
                            }
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }.start();
        new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    mRefreshHandler.sendEmptyMessage(NetConstant.MSG_REFRESH);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void setCurrentWindow(WindowActivity windowActivity) {
        this.mActivity = windowActivity;
    }

    public ArrayList<MyHttpRequest> getmRequestTemp() {
        return mRequestTemp;
    }

    public void setmRequestTemp(ArrayList<MyHttpRequest> mRequestTemp) {
        this.mRequestTemp = mRequestTemp;
    }

    public void sendRequest(MyHttpRequest req) {
        if (!isRunning) {
            start();
            isRunning = true;
        }
        if (req.isBlock) {
        	System.out.println("执行进度条！");
            if(StartPage.isShowNetDialog) mActivity.showNetLoadingDialog();
        }
        System.out.println("添加一个请求至temp");
        mRequestTemp.add(req);
        System.out.println("队列大小"+mRequestTemp.size());
    }
}

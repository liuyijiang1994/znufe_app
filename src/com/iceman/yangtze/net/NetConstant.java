
package com.iceman.yangtze.net;

/**
 * @author iceman 网络相关的一些常量
 */
public interface NetConstant {
    int TYPE_GET = 1;

    int TYPE_POST = 2;

    int HOMEPAGE = 9999;

    int UPDATE = 9998;

    int LOGIN = 1000;

    int CJCX_HOMEPAGE = 2000;

    int CJCX_SEARCH = 2001;

    int XSCX_HOMEPAGE = 3000;

    int XSCX_SEARCH = 3001;

    int XKCX_HOMEPAGE = 4000;

    int XKCX_INFO = 4001;

    int INFO_HOMEPAGE = 5000;

    int INFO_EDIT = 5001;

    String URL_HOMEPAGE = "http://jwc.znufe.edu.cn";
    String URL_UPDATE = "http://hi.baidu.com/android_bin/item/1a9cfd25768d7086af48f5a5";
    String URL_LOGIN_EVALUATE = "http://202.114.224.81:8088/jxpg/login.jsp";
    String URL_KBCX = "http://202.114.224.81:7777/pls/wwwbks/xk.CourseView";
    String URL_CJCX ="http://202.114.224.81:7777/pls/wwwbks/bkscjcx.curscopre";
    String URL_JIDIAN="http://202.114.224.81:7777/pls/wwwbks/bkscjcx.yxkc";
    String KAO_SHI="http://202.114.224.81:7777/pls/wwwbks/bkscjcx.xskssjcx";
    String URL_LOGIN = "http://202.114.224.81:7777/zhxt_bks/xk_login.html";
    String URL_LOGIN2 = "http://202.114.224.81:7777/pls/wwwbks/bks_login2.login";
    String URL_LOGIN_EVALUATE_END = "http://202.114.224.81:8088/jxpg/list_wj.jsp";
    String URL_XSCX = "http://jwc.yangtzeu.edu.cn:8080/student.aspx";

    String URL_XKCX = "http://jwc.yangtzeu.edu.cn:8080/xkinfo.aspx";

    String URL_INFO = "http://jwc.yangtzeu.edu.cn:8080/xssfz.aspx";

    int MSG_REFRESH = 0;

    int MSG_SERVER_ERROR = 1;

    int MSG_NONE_NET = 2;

    int MSG_NET_ERROR = 3;

    int MSG_PASSWORD_ERROR = 4;

    int MSG_OTHER_ERROR = 99;

}

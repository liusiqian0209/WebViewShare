package cn.liusiqian.webviewdemo;

/**
 * Created by liusiqian on 2018/11/29.
 */
public class Consts {
    public static final String TAG = "liusiqianjsdemo";

    public static final String IP = "172.17.160.224";
    public static final String HTTP_HOST = "http://"+IP+":3000/";
    public static final String HTTPS_HOST = "httpS://"+IP+":3001/";

    public static final String HOST = HTTPS_HOST;
    public static final String VERIFY_CODE_PATH = "verifycode.html";
    public static final String CHROME_CLIENT_PATH = "chrome_client_test.html";
    public static final String ARTICLE_PATH = "article.html";
    public static final String JSON_TEST_PATH = "json/test.json";

    public static final String JS_BRIDGE_SCHEME = "liusiqian";
    public static final String JS_BRIDGE_ACTION_CLEAR_TICKET = "clearTicket";
    public static final String JS_BRIDGE_PARAM_CLEAR_TICKET_DEF = "def";
}

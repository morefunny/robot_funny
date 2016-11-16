package api;

import com.loopj.android.http.*;
/**
 * Created by sherlockhua on 2016/11/14.
 */
public class RobotApiClient {

    private static final String BASE_URL = "http://api.twitter.com/1/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params,  AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}

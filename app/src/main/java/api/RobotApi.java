package api;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sherlockhua on 2016/11/16.
 */

public class RobotApi {

    public RobotApi() {

    }

    public void GetJokeList(int userId, int catId, int limit, ReceiveThreadListHandler handler) {

        String url = String.format("/user/query?uid=%d&cat_id=%d&limit=%d", userId, catId, limit);

        RobotApiClient.get(url, new AsyncHttpResponseHandler() {

            public void  onSuccess(int statusCode, Header[] headers, byte[] responseBody){

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

        });
    }
}

package api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sherlockhua on 2016/11/16.
 */

public class RobotApi {

    public RobotApi() {

    }

    public void GetJokeList(int userId, final int catId, int limit, final ReceiveThreadListHandler handler) {

        String url = String.format("/user/query?uid=%d&cat_id=%d&limit=%d", userId, catId, limit);

        RobotApiClient.get(url, new AsyncHttpResponseHandler() {

            public void  onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                String data = new String(responseBody);
                Log.e("HEHE", data);
                try {
                    JSONObject respObject = new JSONObject(data);
                    JSONObject dataObject = respObject.getJSONObject("data");
                    JSONArray threadsArr = dataObject.getJSONArray("threads");

                    ArrayList<Thread> threadList = new ArrayList<Thread>();
                    for (int i = 0; i < threadsArr.length();i ++) {
                        JSONObject threadObj = threadsArr.getJSONObject(i);
                        Thread thread = new Thread(threadObj);

                        threadList.add(thread);
                    }

                    handler.onReceiveThreadList(catId, threadList);
                } catch (org.json.JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

        });
    }
}

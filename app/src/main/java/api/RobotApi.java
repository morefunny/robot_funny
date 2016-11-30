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

    public void getJokeList(int userId, final int catId, int limit, final ReceiveThreadListHandler handler) {

        String url = String.format("/user/query?uid=%d&cat_id=%d&limit=%d", userId, catId, limit);

        RobotApiClient.get(url, new AsyncHttpResponseHandler() {

            public void  onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                String data = new String(responseBody);
                Log.e("HEHE", data);

                ArrayList<Thread> threadList = new ArrayList<Thread>();
                try {
                    JSONObject respObject = new JSONObject(data);
                    JSONObject dataObject = respObject.getJSONObject("data");
                    JSONArray threadsArr = dataObject.getJSONArray("threads");

                    for (int i = 0; i < threadsArr.length();i ++) {
                        JSONObject threadObj = threadsArr.getJSONObject(i);
                        Thread thread = new Thread(threadObj);

                        threadList.add(thread);
                    }
                } catch (org.json.JSONException e) {
                    Log.e("exception", e.getMessage());
                } finally {
                    handler.onReceiveThreadList(200, catId, threadList);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                ArrayList<Thread> threadList = new ArrayList<Thread>();
                handler.onReceiveThreadList(statusCode, catId, threadList);
            }

        });
    }


    public void getCategory(String cateType, final ReceiveCategoryHandler handler) {

        String url = String.format("/category/getall?type=%s", cateType);

        RobotApiClient.get(url, new AsyncHttpResponseHandler() {

            public void  onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                String data = new String(responseBody);
                Log.e("HEHE", data);

                ArrayList<RCategory> catgoryList = new ArrayList<RCategory>();
                try {
                    JSONObject respObject = new JSONObject(data);
                    JSONObject dataObject = respObject.getJSONObject("data");
                    JSONArray arr = dataObject.getJSONArray("category");

                    for (int i = 0; i < arr.length();i ++) {
                        JSONObject obj = arr.getJSONObject(i);
                        RCategory cat = new RCategory(obj);

                        catgoryList.add(cat);
                    }
                } catch (org.json.JSONException e) {
                    Log.e("exception", e.getMessage());
                } finally {
                    handler.onReceiveCategoryList(200, catgoryList);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                ArrayList<RCategory> catList = new ArrayList<RCategory>();
                handler.onReceiveCategoryList(statusCode, catList);
            }

        });
    }
}

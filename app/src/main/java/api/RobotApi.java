package api;

import java.util.ArrayList;
import com.loopj.android.http.*;

/**
 * Created by sherlockhua on 2016/11/16.
 */

public class RobotApi {

    public RobotApi() {

    }

    public ArrayList<Thread> GetJokeList(int userId, int catId, int limit) {

        String url = String.format("/user/query?uid=%d&cat_id=%d&limit=%d", userId, catId, limit);

        RobotApiClient.get(url, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject data) {

            }
        });
    }
}

package api;

import java.util.ArrayList;

/**
 * Created by sherlockhua on 2016/11/16.
 */

public interface ReceiveThreadListHandler {
    void onReceiveThreadList(int code, int catId, ArrayList<Thread> data);
}

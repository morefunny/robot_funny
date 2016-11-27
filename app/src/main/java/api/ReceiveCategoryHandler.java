package api;

import java.util.ArrayList;

/**
 * Created by sherlockhua on 2016/11/27.
 */

public interface ReceiveCategoryHandler {
    void onReceiveCategoryList(int code, ArrayList<RCategory> data);
}

package api;

import java.util.ArrayList;

/**
 * Created by sherlockhua on 2016/11/16.
 */



public class Thread {
    private int _threadId;
    private int _categoryId;
    private int _topicId;
    private int _userId;
    private String _threadStatus;
    private int _upCount;
    private int _downCount;
    private int _commentCount;
    private String _createTime;

    private ArrayList<ThreadData> _dataList;
    private String _threadType;

    public Thread() {

    }

    public void setThreadId(int threadId) {
        _threadId = threadId;
    }

    public void setCategoryId(int catId) {
        _categoryId = catId;
    }

    public void setTopicId(int topicId) {
        _topicId = topicId;
    }
}

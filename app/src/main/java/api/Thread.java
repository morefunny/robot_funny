package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private boolean _isNotify;

    private ArrayList<ThreadData> _contentList;
    private String _threadType;

    public Thread(boolean isNotify, String content) {
        _isNotify = isNotify;
        ThreadData data = new ThreadData(content, "text");
        _contentList = new ArrayList<ThreadData>();
        _contentList.add(data);
    }

    public Thread(JSONObject obj) throws JSONException {
        _threadId = obj.getInt("ThreadId");
        _categoryId = obj.getInt("CategoryId");
        _topicId = obj.getInt("TopicId");
        _userId = obj.getInt("UserId");
        _threadStatus = obj.getString("ThreadStatus");

        _upCount = obj.getInt("UpCount");
        _downCount = obj.getInt("DownCount");
        _commentCount = obj.getInt("CommentCount");
        _createTime = obj.getString("CreateTime");
        _threadType = obj.getString("ThreadType");

        _contentList = new ArrayList<ThreadData>();
        String content = obj.getString("Content");
        JSONArray contentArr = new JSONArray(content);
        for (int i = 0; i < contentArr.length(); i++) {
            JSONObject nodeObj = contentArr.getJSONObject(i);

            String data = nodeObj.getString("data");
            String type = nodeObj.getString("type");
            ThreadData threadData = new ThreadData(data, type);
            _contentList.add(threadData);
        }
    }

    public  boolean isNotify () {
        return _isNotify;
    }

    public boolean hasImage() {
        return _threadType.equals("Pic");
    }

    public void setThreadId(int threadId) {
        _threadId = threadId;
    }

    public int getThreadId() {
        return _threadId;
    }

    public void setCategoryId(int catId) {
        _categoryId = catId;
    }

    public void setTopicId(int topicId) {
        _topicId = topicId;
    }

    public ArrayList<ThreadData> getContentList() {
        return _contentList;
    }

    public String getContent() {

        StringBuilder content = new StringBuilder();
        for(int i = 0; i < _contentList.size(); i++) {
            if (_contentList.get(i).getDataType().equals("text")) {
                content.append(_contentList.get(i).getData());
            }
        }
        return content.toString();
    }

    public String getUpCount() {
        return String.format("%d", _upCount);
    }

    public String getDownCount() {
        return String.format("%d", _downCount);
    }

    public String getCommentCount() {
        return String.format("%d", _commentCount);
    }
}

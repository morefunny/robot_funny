package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sherlockhua on 2016/11/16.
 */



public class Thread implements Serializable{
    private int _threadId;
    private int _categoryId;
    private int _topicId;
    private int _userId;
    private String _threadStatus;
    private int _upCount;
    private int _downCount;
    private int _commentCount;
    private String _createTime;
    private String _title;
    private int _isLongText;

    private boolean _isNotify;
    private int MaxNumberFormat = 10000;

    private ArrayList<ThreadData> _contentList;
    private String _threadType;

    public Thread(boolean isNotify, String content) {
        _isNotify = isNotify;
        ThreadData data = new ThreadData(content, "text", "");
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
        _title = obj.getString("Title");
        _isLongText = obj.getInt("IsLongText");

        _contentList = new ArrayList<ThreadData>();
        String content = obj.getString("Content");
        JSONArray contentArr = new JSONArray(content);
        for (int i = 0; i < contentArr.length(); i++) {
            JSONObject nodeObj = contentArr.getJSONObject(i);

            String data = nodeObj.getString("data");
            String type = nodeObj.getString("type");
            String thumb;
            try {
                thumb = nodeObj.getString("thumb");
            } catch (JSONException e) {
                thumb = "";
            }

            ThreadData threadData = new ThreadData(data, type, thumb);
            _contentList.add(threadData);
        }
    }

    public  boolean isNotify () {
        return _isNotify;
    }

    public boolean hasImage() {
        return isPic() || isGif();
    }

    public boolean isPic() {
        return _threadType.equals("Pic");
    }

    public boolean isGif() {
        return _threadType.equals("Gif");
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

    public String getTitle() {
        return _title;
    }

    public boolean isLongText() {
        return _isLongText == 1;
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

        if (_upCount < MaxNumberFormat) {
            return String.format("%d", _upCount);
        }

        return String.format("%.2f万", _upCount*1.0f/10000);
    }

    public String getDownCount() {

        if (_downCount < MaxNumberFormat) {
            return String.format("%d", _downCount);
        }

        return String.format("%.2f万", _downCount*1.0f/10000);
    }

    public String getCommentCount() {

        if (_commentCount < MaxNumberFormat) {
            return String.format("%d", _commentCount);
        }

        return String.format("%.2f万", _commentCount*1.0f/10000);
    }
}

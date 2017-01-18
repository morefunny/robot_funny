package api;

import android.util.Log;

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

    private String _threadStatus;
    private int _upCount;
    private int _downCount;
    private int _commentCount;
    private String _createTime;
    private String _title;

    private boolean _isNotify;
    private int MaxNumberFormat = 10000;

    private ArrayList<ThreadData> _contentList;
    private String _threadType;
    private String _layout;
    private String _source;

    public Thread(boolean isNotify, String content) {
        _isNotify = isNotify;
        ThreadData data = new ThreadData(content, "text", "");
        _contentList = new ArrayList<ThreadData>();
        _contentList.add(data);

        _layout = "list_item_notify";
    }

    public Thread(JSONObject obj) throws JSONException {
        _threadId = obj.getInt("ThreadId");
        _categoryId = obj.getInt("CategoryId");

        _threadStatus = obj.getString("ThreadStatus");

        _upCount = obj.getInt("UpCount");
        _downCount = obj.getInt("DownCount");
        _commentCount = obj.getInt("CommentCount");
        _createTime = obj.getString("CreateTime");
        _threadType = obj.getString("ThreadType");
        _title = obj.getString("Title");
        _source = obj.getString("Source");

        boolean hasImage = false;
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

            if (type.equals("pic") || type.equals("gif")) {
                hasImage = true;
            }

            ThreadData threadData = new ThreadData(data, type, thumb);
            _contentList.add(threadData);
        }

        try {
            _layout = obj.getString("Layout");
        } catch (Exception e) {
            if (hasImage) {
                _layout = "list_item_image";
            } else {
                _layout = "list_item_text";
            }
        }

        Log.e("threadapi", _layout);
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


    public ArrayList<ThreadData> getContentList() {
        return _contentList;
    }

    public String getTitle() {
        return _title;
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

    public boolean isArticle() {
        if (_threadType == null) {
            return false;
        }
        return _threadType.equals("article");
    }

    public boolean isShort() {
        if (_threadType == null) {
            return false;
        }
        return _threadType.equals("short");
    }

    public String getLayout() {
        return _layout;
    }

    public String getSource() {
        return _source;
    }
}

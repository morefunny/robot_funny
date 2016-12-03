package api;

import java.io.Serializable;

/**
 * Created by sherlockhua on 2016/11/16.
 */

public class ThreadData implements Serializable {

    private String _dataType;
    private String _data;
    //当dataType为gif时，thumb保存gif的第一帧url
    private String _thumb;

    public ThreadData(String data, String dataType, String thumb) {
        _data = data;
        _dataType = dataType;
        _thumb = thumb;
    }

    public String getDataType() {
        return _dataType;
    }

    public String getData() {
        return _data;
    }

    public String getThumb() {
        return _thumb;
    }
}

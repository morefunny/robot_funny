package api;

import java.io.Serializable;

/**
 * Created by sherlockhua on 2016/11/16.
 */

public class ThreadData implements Serializable {

    private String _dataType;
    private String _data;

    public ThreadData(String data, String dataType) {
        _data = data;
        _dataType = dataType;
    }

    public String getDataType() {
        return _dataType;
    }

    public String getData() {
        return _data;
    }
}

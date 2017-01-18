package api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by sherlockhua on 2016/11/27.
 */

public class RCategory  implements Serializable {

    private int _categoryId;
    private  String _categoryName;
    private String _categoryType;

    public RCategory(JSONObject obj) throws JSONException {

        _categoryId = obj.getInt("id");
        _categoryName = obj.getString("name_cn");
        _categoryType = obj.getString("type");
    }

    public int GetCategoryId() {
        return _categoryId;
    }

    public String GetCategoryName() {
        return _categoryName;
    }

    public String GetCategoryType() {
        return _categoryType;
    }
}

package api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sherlockhua on 2016/11/27.
 */

public class RCategory {

    private int _categoryId;
    private  String _categoryName;

    public RCategory(JSONObject obj) throws JSONException {


        _categoryId = obj.getInt("CategoryId");
        _categoryName = obj.getString("CategoryName");

    }

    public int GetCategoryId() {
        return _categoryId;
    }

    public String GetCategoryName() {
        return _categoryName;
    }
}

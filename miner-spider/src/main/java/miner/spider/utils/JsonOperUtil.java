package miner.spider.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class JsonOperUtil {
    //returm JSONArray
    public static JSONArray getJsonArray(JSONObject jo, String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        try {
            return jo.getJSONArray(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //return JSONbject
    public static JSONObject getJsonObject(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        try {
            JSONObject jo = new JSONObject(str);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void printJsonObject(JSONObject jsonObj) {
        if (jsonObj == null) {
            return;
        }
//		Set<String> keySet = jsonObj.keySet();
//		for (String key : keySet) {
//			System.out.println(key + "====" + jsonObj.get(key));
//		}
    }
}

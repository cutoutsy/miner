package miner.parse.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import miner.parse.DocObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * @ name:      JsonUtil.java
 * @ author:    white
 * @ info:      处理JSON文档的类
 * */
public class JsonUtil {
	private JSONObject root_obj;
	/* 存放JSONObj文本的队列 */
	private Queue<String> json_obj_queue;
	private Queue<String> json_tag_queue;
	private Map<String, String> map;

	public JsonUtil(String document, DocObject object) {
		this.json_obj_queue = new LinkedList<String>();
		this.json_tag_queue = new LinkedList<String>();
		this.map = object.get_json_map();
		this.root_obj = get_Json_Obj(document);
//		 out.println(document);
	}

	public void parse() {
		/* 根节点入队 */
		Iterator<?> root_it = root_obj.keys();
		while (root_it.hasNext()) {
			String root_ele = root_it.next().toString();// tag
			String root_ele_obj_str = get_Json_Str(root_obj, root_ele);// str
			if (root_ele_obj_str.startsWith("[")) {
				json_obj_queue.offer(root_ele_obj_str);
				json_tag_queue.offer(root_ele + "0");
				//注释以下代码,将会在解析json时将数组当作一个值,不会在进入数组里面遍历
//				try {
//					JSONArray root_array = new JSONArray(root_ele_obj_str);
//					for (int i = 0; i < root_array.length(); i++) {
//						String array_obj_str = root_array.getJSONObject(i)
//								.toString();
//						json_obj_queue.offer(array_obj_str);
//						json_tag_queue.offer(root_ele + i);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
			} else {
				json_obj_queue.offer(root_ele_obj_str);
				json_tag_queue.offer(root_ele + "0");
			}
			// out.println(root_ele);
			// out.println(root_ele_obj_str);
		}
		/* 遍历 */
		String ele_obj;// str
		String ele_tag = null;// tag
		while ((ele_obj = json_obj_queue.poll()) != null
				&& (ele_tag = json_tag_queue.poll()) != null) {
//			 if (!ele_obj.startsWith("{")) {
//			 out.println(ele_tag + "=" + ele_obj);
//			 }
//			out.println(ele_tag);
			map.put(ele_tag, ele_obj);
			JSONObject ele_j_obj;
			if (ele_obj.startsWith("{")) {
				ele_j_obj = get_Json_Obj(ele_obj);
				Iterator<?> it = ele_j_obj.keys();
				String inner_tag;
				String inner_obj;
				while (it.hasNext()) {
					inner_tag = it.next().toString();
					inner_obj = get_Json_Str(ele_j_obj, inner_tag);
					if (inner_obj.startsWith("[")) {
						try {
							JSONArray j_a = new JSONArray(inner_obj);
							for (int j = 0; j < j_a.length(); j++) {
								String array_obj_str = j_a.getJSONObject(j)
										.toString();
								json_obj_queue.offer(array_obj_str);
								json_tag_queue.offer(ele_tag + "." + inner_tag
										+ j);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						json_tag_queue.offer(ele_tag + "." + inner_tag + "0");
						json_obj_queue.offer(inner_obj);
					}
				}
			}
		}

//		Iterator it = map.keySet().iterator();
//		while(it.hasNext()){
//			String key = it.next().toString();
//			System.out.println(key+"=="+map.get(key));
//		}
	}

	/*
	 * @ info:获取JSON对象.
	 * 
	 * @ param:原始JSON字符串.
	 * 
	 * @ return:对应的JSONObj.
	 */
	public JSONObject get_Json_Obj(String str) {
		try {
			return new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @ info:获取JSONArray.
	 * 
	 * @ param:JSONObj和key.
	 * 
	 * @ return:对应的JSONArray.
	 */
	public JSONArray get_Json_Array(JSONObject obj, String str) {
		try {
			return obj.getJSONArray(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @ info:获取JSON value.
	 * 
	 * @ param:原始JSONObj和key.
	 * 
	 * @ return:对应key的value.
	 */
	public String get_Json_Str(JSONObject obj, String str) {
		try {
			//System.out.println(str);
			return obj.getString(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}

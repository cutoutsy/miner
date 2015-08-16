package miner.parse.data;

import java.util.Map;

import miner.parse.RuleItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import miner.parse.DataType;


public class Packer {
	private DataItem data_item;
	private Map<String, Object> data_object;
	private Map<String,RuleItem> rule_items;

	public Packer(DataItem data_item, Map<String, Object> data_object,Map<String,RuleItem> rule_items) {
		this.data_item = data_item;
		this.data_object = data_object;
		this.rule_items=rule_items;
	}

	public String pack() {
		JSONObject return_obj = new JSONObject();
		try {
			/* 四元组 */
			return_obj.put("data_id", data_item.get_data_id());
			return_obj.put("project_id", data_item.get_project_id());
			return_obj.put("task_id", data_item.get_task_id());
			return_obj.put("workstation_id", data_item.get_workstation_id());
			/* key */
            String row_key_str=data_item.get_row_key();
            String f_key_str=data_item.get_foreign_key();
            String f_value_str=data_item.get_foreign_value();

            if(row_key_str.equals("none")){
                return_obj.put("row_key","none");
            }else{
			    return_obj.put("row_key", data_object.get(row_key_str));
            }
            if(f_key_str.equals("none")){
                return_obj.put("foreign_key","none");
            }else{
			    return_obj.put("foreign_key", data_object.get(f_key_str));
            }
            if(f_value_str.equals("none")){
                return_obj.put("foreign_value","none");
            }else{
			    return_obj.put("foreign_value", data_object.get(f_value_str));
            }
			return_obj.put("link", data_item.get_link());

			JSONObject property_obj=new JSONObject();

			for(int i=0;i<data_item.get_data_items().length;i++){
				String tag_id =data_item.get_data_items()[i];
				RuleItem ri=rule_items.get(tag_id);
				DataType type=ri.get_type();
				String tag=ri.get_name();
				if (type.equals(DataType.STR)) {
					String value = (String) data_object.get(tag);
					property_obj.put(tag, value);
				} else if (type.equals(DataType.ARRAY)) {
					String[] values = (String[]) data_object.get(tag);
					JSONArray tmp_json_array = new JSONArray();
					for (int j = 0; j < values.length; j++) {
						tmp_json_array.put(values[j]);
					}
					property_obj.put(tag, tmp_json_array);
				}
			}
			return_obj.put("property",property_obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return return_obj.toString();
	}
}

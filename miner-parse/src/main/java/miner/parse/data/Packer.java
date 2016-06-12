package miner.parse.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import miner.parse.RuleItem;
import org.json.JSONException;
import org.json.JSONObject;
import miner.parse.DataType;

/*
 * @ name:      Packer.java
 * @ author:    white
 * @ info:      一个packer对象只负责生成一个data，仅对应一个DataItem对象
 * */
public class Packer {
	private DataItem data_item;
	private Map<String,Object> data_object;
	private Map<String,RuleItem> rule_items;

	public Packer(DataItem data_item, Map<String, Object> data_object,Map<String,RuleItem> rule_items) {
		this.data_item      = data_item;
		this.data_object    = data_object;
		this.rule_items     = rule_items;
	}

    public String[] pack(){
        Set<String> return_set=new HashSet<String>();
        /* 先处理dataitem中的STR类型，这里封装在一个json对象中*/
        JSONObject single_obj=new JSONObject();
        try{
            /* 四元组 */
            single_obj.put("data_id", data_item.get_data_id());
            single_obj.put("project_id", data_item.get_project_id());
            single_obj.put("task_id", data_item.get_task_id());
            single_obj.put("workstation_id", data_item.get_workstation_id());
			/* key */
            String row_key_str=data_item.get_row_key();
            String f_key_str=data_item.get_foreign_key();
            String f_value_str=data_item.get_foreign_value();
            if(row_key_str.equals("none")){
                single_obj.put("row_key","none");
            }else{
                single_obj.put("row_key", data_object.get(row_key_str));
            }
            if(f_key_str.equals("none")){
                single_obj.put("foreign_key","none");
            }else{
                single_obj.put("foreign_key", data_object.get(f_key_str));
            }
            if(f_value_str.equals("none")){
                single_obj.put("foreign_value","none");
            }else{
                single_obj.put("foreign_value", data_object.get(f_value_str));
            }
            single_obj.put("link", data_item.get_link());
            JSONObject single_property_obj=new JSONObject();
            int flag=0;
            for(int i=0;i<data_item.get_data_items().length;i++){
                String tag_id =data_item.get_data_items()[i];
                RuleItem ri=rule_items.get(tag_id);
                DataType type=ri.get_type();
                String tag=ri.get_name();
                if(type.equals(DataType.STR)){
                    String value = (String) data_object.get(tag);
                    if(!value.equals("none")){
                        single_property_obj.put(tag, value);
                        flag=1;
                    }
                }
            }
            single_obj.put("property",single_property_obj);
            if(flag==1){
                return_set.add(single_obj.toString());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        /* 处理ARRAY类型的抽取数据 */
        int count=-65535;
        do{
            JSONObject array_single_obj=new JSONObject();
            try {
                /* 四元组 */
                array_single_obj.put("data_id", data_item.get_data_id());
                array_single_obj.put("project_id", data_item.get_project_id());
                array_single_obj.put("task_id", data_item.get_task_id());
                array_single_obj.put("workstation_id", data_item.get_workstation_id());
			    /* key */
                String row_key_str = data_item.get_row_key();
                String f_key_str = data_item.get_foreign_key();
                String f_value_str = data_item.get_foreign_value();
                if (row_key_str.equals("none")) {
                    array_single_obj.put("row_key", "none");
                } else {
                    array_single_obj.put("row_key", data_object.get(row_key_str));
                }
                if (f_key_str.equals("none")) {
                    array_single_obj.put("foreign_key", "none");
                } else {
                    array_single_obj.put("foreign_key", data_object.get(f_key_str));
                }
                if (f_value_str.equals("none")) {
                    array_single_obj.put("foreign_value", "none");
                } else {
                    array_single_obj.put("foreign_value", data_object.get(f_value_str));
                }
                array_single_obj.put("link", data_item.get_link());
                /* 内部的property */
                JSONObject array_property_obj=new JSONObject();
                for(int i=0;i<data_item.get_data_items().length;i++){
                    String tag_id =data_item.get_data_items()[i];
                    RuleItem ri=rule_items.get(tag_id);
                    DataType type=ri.get_type();
                    String tag=ri.get_name();
                    if(type.equals(DataType.ARRAY)){
                        String[] values = (String[]) data_object.get(tag);
                        if(count==-65535){
                            count=values.length;
                        }
                        if(!values[count-1].equals("none")){
                            array_property_obj.put(tag,values[count-1]);
                        }
                    }
                }
                count--;

                array_single_obj.put("property", array_property_obj);
//                System.out.println(array_single_obj);

                if(!array_property_obj.toString().equals("{}")) {
                    return_set.add(array_single_obj.toString());
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }while(count>0);
        String[] return_str=new String[return_set.size()];
        Iterator<String> it= return_set.iterator();
        int j=0;
        while(it.hasNext()){
            return_str[j]=it.next();
            j++;
        }

        return return_str;
    }
}

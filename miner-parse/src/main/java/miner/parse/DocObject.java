package miner.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import miner.parse.DocType;
import miner.parse.util.HtmlUtil;
import miner.parse.util.JsonUtil;
import org.jsoup.nodes.Element;
import miner.parse.data.Packer;
import miner.parse.data.DataItem;

public class DocObject {
	private String document;
	private DocType doc_type;
	private CharSet char_set;
	/* 字段存放处 */
	public Map<String, Element> html_map;
	public Map<String, String> json_map;
	/* 工具类声明 */
	private HtmlUtil h_util;
	private JsonUtil j_util;

	public DocType get_type() {
		return this.doc_type;
	}

	public Map<String, String> get_json_map() {
		return this.json_map;
	}

	public Map<String, Element> get_html_map() {
		return this.html_map;
	}

	public DocObject(String document, CharSet char_set, DocType doc_type) {
		if (doc_type.equals(DocType.JSONP)) {
			this.document = document.substring(document.indexOf('{'),
					document.length() - 1);
		} else {
			this.document = document;
		}
		this.doc_type = doc_type;
		this.char_set = char_set;
		this.html_map = new HashMap<String, Element>();
		this.json_map = new HashMap<String, String>();
	}

	public DocObject(String path, CharSet char_set) {
		String encoding = "UTF8";
		String tail_str = path.split("\\.")[1];
		if (tail_str.equals("js")) {
			doc_type = DocType.JSON;
		} else if (tail_str.equals("html") || tail_str.equals("htm")) {
			doc_type = DocType.HTML;
		} else if (tail_str.equals("xml")) {
			doc_type = DocType.XML;
		} else {
			doc_type = null;
		}
		if (char_set.equals(CharSet.UTF8)) {
			encoding = "UTF8";
		} else if (char_set.equals(CharSet.GBK)) {
			encoding = "GBK";
		} else if (char_set.equals(CharSet.GB2312)) {
			encoding = "GB2312";
		}
		this.document = get_doc_str(path, encoding);
		this.html_map = new HashMap<String, Element>();
		this.json_map = new HashMap<String, String>();
	}

	/* 得到文本字符串 */
	private String get_doc_str(String doc_path, String encoding) {
		File file = new File(doc_path);
		String doc_str = "";
		if (file.isFile() && file.exists()) {
			InputStreamReader read;
			try {
				read = new InputStreamReader(new FileInputStream(file),
						encoding);
				BufferedReader buffered_reader = new BufferedReader(read);
				String line = null;
				while ((line = buffered_reader.readLine()) != null) {
					doc_str += line;
				}
				buffered_reader.close();
				read.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return doc_str;
	}

	public void parse() {
		if (doc_type.equals(DocType.HTML)) {
			parse_html();
		} else if (doc_type.equals(DocType.JSON)) {
			parse_json();
		}
	}

	public void parse_json() {
		j_util = new JsonUtil(this.document, this);
		j_util.parse();
	}

	public void parse_html() {
		h_util = new HtmlUtil(this.document, this);
		h_util.parse();
	}

	public String get_value(String path, String tag) {
		if (doc_type.equals(DocType.HTML)) {
			Element e = html_map.get(path);
            if(e.equals(null)){
                return "none";
            }else{
			    String result;
			    if (tag.equals("text")) {
				    result = e.text();
			    } else {
				    result = e.attr(tag);
			    }
                return result;
            }
		} else if (doc_type.equals(DocType.JSON)
				|| doc_type.equals(DocType.JSONP)) {
			// System.out.println(json_map.get(path));
            String result="none";
            if(json_map.get(path)!=null){
                result=json_map.get(path);
            }
			return result;

		}
		return "none";
	}

	public static void main(String[] args) {
		/*
		 * Document:
		 * 	doc,charset
		 * Data Parse:
		 * 	Item:
		 * 		id->name,path,tag,type
		 * Data Pack:
		 *  Item:
		 *  	data_id,project_id,task_id,workstation_id,row_key,foreign_key,foreign_value,link,id0,id1,id2...
		 * */
		/* 抽取单个doc数据的规则库，多个set组成map */
		Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
		data_rule_map.put("id0", new RuleItem("name0",
				"rateDetail.rateCount.picNum", "text", DataType.STR));
		data_rule_map.put("id1", new RuleItem("name1",
				"rateList_array.name", "text", DataType.ARRAY));
        data_rule_map.put("id2",new RuleItem("name2","rateList_array.haha","text",DataType.STR));
		/* 封装数据的规则库map */
		Set<DataItem> data_item_set = new HashSet<DataItem>();
//		data_item_set.add(new DataItem("1", "1", "1", "1", "name0", "name0",
//                "none", "alone", "id0","id1"));
//		data_item_set.add(new DataItem("1", "1", "1", "1", "name1", "none",
//				"alone", "alone", "id0"));
        data_item_set.add(new DataItem("1","1","1","1","none","none","none","alone","id2"));
		/* 数据生成器 */
		Generator g = new Generator();
		g.create_obj("/Users/white/Desktop/workspace/test_json_storage.js",
				CharSet.UTF8);
		for (Map.Entry<String, RuleItem> entry : data_rule_map.entrySet()) {
			g.set_rule(entry.getValue());
		}
		g.generate_data();
		Map<String, Object> m = g.get_result();// m里封装了所有抽取的数据
		Iterator<DataItem> data_item_it = data_item_set.iterator();
		while (data_item_it.hasNext()) {
			Packer packer = new Packer(data_item_it.next(), m, data_rule_map);
			System.out.println("pack_result:" + packer.pack());
		}

		// "/Users/white/Desktop/workspace/test.html"
	}
}

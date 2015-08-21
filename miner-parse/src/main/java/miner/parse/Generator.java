package miner.parse;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Generator {
	private Set<RuleItem> parse_rule_set;
	private DocObject obj;
	private Map<String, Object> result;// 存放抽取出来的字段

	public Generator() {
		parse_rule_set = new HashSet<RuleItem>();
		result = new HashMap<String, Object>();
	}

	/* 抽取之前必须执行的方法 */
	public void create_obj(String document) {
        DocType type=DocType.HTML;
        CharSet char_set= CharSet.UTF8;
        if(document.startsWith("<")){
            type=DocType.HTML;
            Document tmp_doc= Jsoup.parse(document);
            Elements char_text = tmp_doc.select("meta[charset]");
            for(Element e:char_text){
                if(e.attr("charset").equals("utf-8")){
                    char_set=CharSet.UTF8;
                }else if(e.attr("charset").equals("gbk")){
                    char_set=CharSet.GBK;
                }else if(e.attr("charset").equals("gb2312")){
                    char_set=CharSet.GB2312;
                }
            }
        }else if(document.startsWith("{")){
            type=DocType.JSON;
            char_set=CharSet.UTF8;
        }else if(document.endsWith("})")){
            type=DocType.JSONP;
            char_set=CharSet.UTF8;
        }else {
            type=DocType.TEXT;
            char_set=CharSet.UTF8;
        }
		this.obj = new DocObject(document, char_set, type);
		this.obj.parse();
	}

	/* 重载 */
	public void create_obj(String path, CharSet char_set) {

		this.obj = new DocObject(path, char_set);
		this.obj.parse();
	}

	/* 抽取之前必须执行的方法 */
	public void set_rule(RuleItem ri) {
		parse_rule_set.add(ri);
	}

	/* 产生需要的数据，封装在Map内 */
	public void generate_data() {
		Iterator<RuleItem> it = parse_rule_set.iterator();
		while (it.hasNext()) {
			RuleItem tmp_rule = it.next();
			String name = tmp_rule.get_name();
			if (tmp_rule.get_type().equals(DataType.STR)) {
				/* 单个字段 */
				String result_str = this.obj.get_value(tmp_rule.get_path(),
						tmp_rule.get_tag());
				result.put(name, result_str);
			} else if (tmp_rule.get_type().equals(DataType.ARRAY)) {
				/* map里所有的key */
				String[] keys = null;
				if (this.obj.get_type().equals(DocType.HTML)) {
					keys = new String[this.obj.get_html_map().size()];
					int j = 0;
					for (Entry<String, Element> entry : this.obj.get_html_map()
							.entrySet()) {
						keys[j] = entry.getKey();
						j++;
					}
				} else if (this.obj.get_type().equals(DocType.JSON)
						|| this.obj.get_type().equals(DocType.JSONP)) {
					keys = new String[this.obj.get_json_map().size()];
					int j = 0;
					for (Entry<String, String> entry : this.obj.get_json_map()
							.entrySet()) {
						keys[j] = entry.getKey();
//						System.out.println(keys[j]);
						j++;
					}
				}
				/* 筛选key */
				Set<String> key_set = new HashSet<String>();
				String[] path_split = tmp_rule.get_path().split("\\.");
				for (int i = 0; i < keys.length; i++) {
					/* 遍历 */
					if (path_split.length == keys[i].split("\\.").length) {
						String[] s = keys[i].split("\\.");
						boolean b = true;
						for (int x = 0; x < path_split.length; x++) {
							if (s[x].equals(path_split[x])
									|| s[x].startsWith(path_split[x]
											.split("_array")[0])) {
								b &= true;
							} else {
								b &= false;
							}
						}
						if (b) {
							key_set.add(keys[i]);
//							System.out.println(keys[i]);
						}
					}

				}
				String[] str_array = new String[key_set.size()];
				Iterator<String> key_it = key_set.iterator();
				int y = 0;
				while (key_it.hasNext()) {
					str_array[y] = this.obj.get_value(key_it.next(),
							tmp_rule.get_tag());
					y++;
				}
				result.put(tmp_rule.get_name(), str_array);
			}
		}

	}

	public Map<String, Object> get_result() {
		return this.result;
	}

}

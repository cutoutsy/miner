package miner.parse;

import java.util.*;

public class Generator {
	private Set<RuleItem> parse_rule_set;
	private DocObject obj;
	private Map<String, Object> result;// 存放抽取出来的字段

    public Set<RuleItem> get_parse_rule_set(){
        return this.parse_rule_set;
    }

    public DocObject get_doc_obj(){
        return this.obj;
    }

    /* 返回已经抽取出来的数据 */
    public Map<String, Object> get_result() {
        return this.result;
    }

    public Generator() {
		parse_rule_set = new HashSet<RuleItem>();
		result = new HashMap<String, Object>();
	}

    public DocType judge_doc_type(String document){
        DocType type=DocType.HTML;
        if(document.endsWith("</html>")){
            type=DocType.HTML;
        }else if(document.startsWith("<")){
            type=DocType.XML;
        } else if(document.startsWith("{")){
            type=DocType.JSON;
        }else if(document.endsWith("})")){
            type=DocType.JSONP;
        }else {
            type=DocType.TEXT;
        }
        return type;
    }

	/* 抽取之前必须执行的方法 */
	public void create_obj(String document) {
        DocType type = this.judge_doc_type(document);
        this.obj = new DocObject(document, type);
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
//                if(result_str.equals("none")&&tmp_rule.get_extra_paths()!=null){
//                    for(int y=0;y<tmp_rule.get_extra_paths().length;y++){
//                        String extra_path=tmp_rule.get_extra_paths()[y];
//                        this.obj.get_value(extra_path,)
//                    }
//                }
//                System.out.println(tmp_rule.get_path());
				result.put(name, result_str);
			} else if (tmp_rule.get_type().equals(DataType.ARRAY)) {
                String[] tmp_paths=tmp_rule.get_path().split("\\.");
                Set<String> final_set=new HashSet<String>();
                final_set.add("");
                for(int i=0;i<tmp_paths.length;i++){
                    Set<String> tmp_set=new HashSet<String>();
                    if(tmp_paths[i].endsWith("_")){
                        String[] path_clips=tmp_paths[i].split("_");
                        int start=0,end=0;
                        String tag=path_clips[0];
                        start=Integer.parseInt(path_clips[1]);
                        end=Integer.parseInt(path_clips[2]);
                        for(int j=start;j<=end;j++){
                            Iterator<String> set_it=final_set.iterator();
                            while (set_it.hasNext()){
                                String tmp_next=set_it.next();
                                tmp_set.add(tmp_next+tag+j+".");
                            }
                        }
                    }else{
                        /*以单个数组结尾的情况*/
                        String clip=tmp_paths[i];
                        Iterator<String> set_it=final_set.iterator();
                        while(set_it.hasNext()){
                            String tmp_next=set_it.next();
                            tmp_set.add(tmp_next+clip+".");
                        }
                    }
                    final_set=tmp_set;
                }

                Iterator<String> final_it=final_set.iterator();
                String[] str_array = new String[final_set.size()];
                int y=0;
                while (final_it.hasNext()){
                    String ss=final_it.next();
//                    System.out.println(ss.substring(0,ss.length()-1));
                    str_array[y]=this.obj.get_value(ss.substring(0,ss.length()-1),tmp_rule.get_tag());
                    y++;
                }
                result.put(tmp_rule.get_name(), str_array);
            }
		}

	}

}

package miner.parse;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.BlockingDeque;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Generator {
	private Set<RuleItem> parse_rule_set;
	private DocObject obj;
	private Map<String, Object> result;// 存放抽取出来的字段

    public DocObject get_doc_obj(){
        return this.obj;
    }

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
            Elements char_text = tmp_doc.select("meta");
            for(Element e:char_text){
                if(e.attr("charset").equals("utf-8")||e.attr("content").contains("utf-8")){
                    char_set=CharSet.UTF8;
                }else if(e.attr("charset").equals("gbk")||e.attr("content").contains("gbk")){
                    char_set=CharSet.GBK;
                }else if(e.attr("charset").equals("gb2312")||e.attr("content").contains("gb2312")){
                    char_set=CharSet.GB2312;
                }
//                System.out.println(char_set);
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
        String encoding_charset=null;
        if(char_set.equals(CharSet.UTF8)){
            encoding_charset="UTF8";
        }else if(char_set.equals(CharSet.GBK)){
            encoding_charset="GBK";
        } else if (char_set.equals(CharSet.GB2312)){
            encoding_charset="GB2312";
        }
        try{
            byte[] doc_bytes=document.getBytes(encoding_charset);
            String final_doc=new String(doc_bytes,"UTF8");
//            System.out.println(final_doc);
            //下面的document参数等会一定要修改成final_doc
            this.obj = new DocObject(final_doc, char_set, type);
            this.obj.parse();
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
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

	public Map<String, Object> get_result() {
		return this.result;
	}

}

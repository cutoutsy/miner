package miner.parse.util;

import miner.parse.DocObject;
import miner.parse.DocType;
import miner.parse.Generator;
import miner.parse.RuleItem;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * parse junit test case
 */
public class HtmlUtilTest {

    @Test
    public void testParse(){
        File file = new File("/Users/cutoutsy/Desktop/test.html");
        String doc_str = "";
        if (file.isFile() && file.exists()) {
            InputStreamReader read;
            try {
                read = new InputStreamReader(new FileInputStream(file),
                        "UTF8");
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

        DocObject object = new DocObject(doc_str, DocType.HTML);
        HtmlUtil hu = new HtmlUtil(doc_str, object);

        hu.parse();

    }

    @Test
    public void testJsonParse(){
        File file = new File("/Users/cutoutsy/Desktop/stocks.php");
        String doc_str = "";
        if (file.isFile() && file.exists()) {
            InputStreamReader read;
            try {
                read = new InputStreamReader(new FileInputStream(file),
                        "UTF8");
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

        DocObject object = new DocObject(doc_str, DocType.HTML);
        JsonUtil ju = new JsonUtil(doc_str, object);

        ju.parse();
    }

    @Test
    public void testWdjParse(){
        long start = System.currentTimeMillis();
        /* 抽取单个doc数据的规则库，多个set组成map */
        Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
        data_rule_map.put("id_error", new RuleItem("name_error",
                "error0"));
        data_rule_map.put("id_member", new RuleItem("name_member",
                "member0"));
        data_rule_map.put("id_msg", new RuleItem("name_msg",
                "msg0"));
        /* 封装数据的规则库map */
        Set<DataItem> data_item_set = new HashSet<DataItem>();

        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none",
                "none", "none", "id_error", "id_member", "id_msg"));
        /* 数据生成器 */
        Generator g = new Generator();
        g.create_obj(get_test_doc_str("wdj.html"));
        for (Map.Entry<String, RuleItem> entry : data_rule_map.entrySet()) {
            g.set_rule(entry.getValue());
        }
        g.generate_data();
        Map<String, Object> m = g.get_result();// m里封装了所有抽取的数据
        Iterator<DataItem> data_item_it = data_item_set.iterator();
        while (data_item_it.hasNext()) {
            Packer packer = new Packer(data_item_it.next(), m, data_rule_map);
            String[] result_str=packer.pack();
            for(int i=0;i<result_str.length;i++){
                System.out.println(result_str[i]);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("time:"+(double)(end-start)/1000);
    }

    public String get_test_doc_str(String file_path){
        File file = new File(file_path);
        String doc_str = "";
        if (file.isFile() && file.exists()) {
            InputStreamReader read;
            try {
                read = new InputStreamReader(new FileInputStream(file),
                        "UTF8");
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

}

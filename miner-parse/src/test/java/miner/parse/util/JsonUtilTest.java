package miner.parse.util;

import junit.framework.TestCase;
import miner.parse.Generator;
import miner.parse.RuleItem;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by MaFu on 2016/5/21.
 */
public class JsonUtilTest extends TestCase{
    public static void testJson()throws IOException {
        URL url = new URL("http://club.jd.com/productpage/p-935349-s-0-t-3-p-2.html?callback=fetchJSON_comment98vv10861");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream is = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        String doc_str = "";
        int read;
        while ((read = isr.read()) != -1) {
            doc_str += (char) read;
        }
        isr.close();
        long start = System.currentTimeMillis();
        System.out.print(doc_str);
        /* 抽取单个doc数据的规则库，多个set组成map */
        Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
        data_rule_map.put("comments_success", new RuleItem("comment",
                "comments0"));
        /* 封装数据的规则库map */
        Set<DataItem> data_item_set = new HashSet<DataItem>();
        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none",
                "none", "none","comments_success"));
        /* 数据生成器 */
        Generator g = new Generator();
        g.create_obj(doc_str);
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
}

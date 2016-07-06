package miner.parse.util;

import junit.framework.TestCase;
import miner.parse.Generator;
import miner.parse.RuleItem;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
import miner.spider.httpclient.Crawl4HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * 对于Linkdein的parse路径测试
 */
public class LinkedinTest extends TestCase{


    public void testLinkeinParse() {
        Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
//        data_rule_map.put("id_name", new RuleItem("name_name", "html0.body0.main2.div1.div0.div1.div0.div1.h10"));
        data_rule_map.put("id_city", new RuleItem("name_city", "html0.body0.main2.div1.div0.div1.div0.div1.div0.a0"));
//        data_rule_map.put("id_time", new RuleItem("name_time", "html0.body0.main0.div1.div0.div1.div0.div1.div0.span0"));
//        data_rule_map.put("id_inf0_0", new RuleItem("name_inf0_0", "html0.body0.main2.div1.div0.div0.div1.div1.dl0.ul0.li0.div0.div0.div0"));
//        data_rule_map.put("id_inf0_1", new RuleItem("name_inf0_1", "html0.body0.main2.div1.div0.div1.div2.div0.div1.div0"));
//        data_rule_map.put("id_school", new RuleItem("name_school", "html0.body0.main2.div1.div0.div0.div2.div1.dl0.dd0"));
//        data_rule_map.put("gus_name", new RuleItem("name_gus_name", "html0.body0.main2.div1.div0.div1.div3.div1.div0.div0.div0.div0.div0.div0.a0.div1"));
//        data_rule_map.put("gus_comment", new RuleItem("name_gus_comment", "html0.body0.main2.div1.div0.div1.div3.div1.div0.div0.div0.div0.div1.div0.div0.div0.p0"));
//        data_rule_map.put("gus_city", new RuleItem("name_gus_city", "html0.body0.main2.div1.div0.div1.div3.div1.div0.div1.div0.div2.div1.div0.div1.a0"));
//        data_rule_map.put("gus_time", new RuleItem("name_gus_time", "html0.body0.main2.div1.div0.div1.div3.div1.div0.div0.div0.div0.div0.div0.div0"));


    /* 封装数据的规则库map */
        Set<DataItem> data_item_set = new HashSet<DataItem>();

        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_city"));
//        "id_city", "id_time", "id_inf0_0", "id_inf0_1", "id_school", "gus_name", "gus_comment","gus_city","gus_time"
        //        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_price"));
        /* 数据生成器 */
        Generator g = new Generator();
        StringBuffer doc_str = new StringBuffer();
        try {
            String productUrl = "https://zh.airbnb.com/users/show/20928619";
//            String productUrl = "https://www.linkedin.com/in/jeffweiner08";
//            String proxy = "120.52.73.26:8080";
            String pageSource = Crawl4HttpClient.downLoadPage(productUrl);
            doc_str.append(pageSource);
//            URL url = new URL("https://zh.airbnb.com/users/show/20928619");
//            URLConnection uc = url.openConnection();
//            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null)
//                doc_str.append(inputLine);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        System.out.println(doc_str);
        g.create_obj(doc_str.toString());
        for (Map.Entry<String, RuleItem> entry : data_rule_map.entrySet()) {
            g.set_rule(entry.getValue());
        }
        g.generate_data();
        Map<String, Object> m = g.get_result();// m里封装了所有抽取的数据
        Iterator<DataItem> data_item_it = data_item_set.iterator();
        while (data_item_it.hasNext()) {
            Packer packer = new Packer(data_item_it.next(), m, data_rule_map);
            String[] result_str = packer.pack();
            for(int i=0;i<result_str.length;i++){
                System.out.println(result_str[i]+"\n");
            }
        }
    }
}

package miner.parse.util;

import junit.framework.TestCase;
import miner.parse.Generator;
import miner.parse.RuleItem;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
import miner.spider.httpclient.Crawl4HttpClient;

import java.util.*;

/**
 * 对于Linkdein的parse路径测试
 */
public class AirbnbTest extends TestCase{


    public void testAirbnbParse() {
        Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
        data_rule_map.put("id_time", new RuleItem("name_time", "html0.body0.main2.div1.div0.div1.div0.div1.div0.span0.text"));
        data_rule_map.put("id_name", new RuleItem("name_name", "html0.body0.main2.div1.div0.div1.div0.div1.h10.text"));
        data_rule_map.put("id_locality", new RuleItem("name_locality", "html0.body0.main2.div1.div0.div1.div0.div1.div0.a0.text"));
//        data_rule_map.put("id_industry", new RuleItem("name_industry", "html0.body0.div0.main0.div4.div0.section0.div0.div1.div0.dl0.dd1.text"));
//        data_rule_map.put("id_currpos", new RuleItem("name_currpos", "html0.body0.div0.main0.div4.div0.section0.div0.div1.div0.table0.tbody0.tr0.td0.ol0.li0.span0.text"));
//        data_rule_map.put("id_prevpos", new RuleItem("name_prevpos", "html0.body0.div0.main0.div4.div0.section0.div0.div1.div0.table0.tbody0.tr1.td0.ol0.li0.span0.text"));
        data_rule_map.put("id_edu", new RuleItem("name_edu", "html0.body0.main2.div1.div0.div0.div2.div1.dl0.dd0.text"));
        data_rule_map.put("id_conn", new RuleItem("name_conn", "html0.body0.main2.div1.div0.div1.div3.div1.div0.div0.div0.div0.div0.div0.a0.div1.text"));

    /* 封装数据的规则库map */
        Set<DataItem> data_item_set = new HashSet<DataItem>();

        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_name", "id_time", "id_locality", "id_edu", "id_conn"));
//        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_name"));
        /* 数据生成器 */
        Generator g = new Generator();
        StringBuffer doc_str = new StringBuffer();
        try {
//            String productUrl = "https://zh.airbnb.com/users/show/20928619";
            String productUrl = "https://zh.airbnb.com/users/show/2";
//            String productUrl = "https://www.linkedin.com/in/jeffweiner08";
//            String proxy = "114.228.112.254:8998";
            String pageSource = Crawl4HttpClient.downLoadPage(productUrl);
            doc_str.append(pageSource);
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
            System.out.println(result_str.length + "====");
            System.out.println(result_str[0]);
        }
    }
}

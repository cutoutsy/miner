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
public class LinkedinTest extends TestCase{


    public void testLinkeinParse() {
        Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
        data_rule_map.put("id_picture", new RuleItem("name_picture", "html0.body0.div0.main0.div0.div0.section0.div0.div0.a0.img.title"));

    /* 封装数据的规则库map */
        Set<DataItem> data_item_set = new HashSet<DataItem>();

        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_picture"));
        //        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_price"));
        /* 数据生成器 */
        Generator g = new Generator();
        StringBuffer doc_str = new StringBuffer();
        try {
//            String productUrl = "https://www.linkedin.com/in/%C3%B8ystein-wahl-77517b95";
            String productUrl = "https://www.linkedin.com/in/jeffweiner08";
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

package miner.parse.util;

import junit.framework.TestCase;
import miner.parse.Generator;
import miner.parse.RuleItem;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by mafu on 16-7-13.
 */
public class xPath extends TestCase{
    public  void testXpath() throws XPatherException {
        String res = get_document();
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        int i = 0;
        TagNode root = htmlCleaner.clean(res);
        Object[] nameArray = root.evaluateXPath("//*[@id=\"review-31268345\"]/div[2]/div/div[1]/div/p");
        for (Object thenode : nameArray) {
            TagNode tna = (TagNode) thenode;
            System.out.print(tna.getText().toString()+i);
            i++;
        }
    }

    public void testStr(){
        String thepath  = "test$$ll";
        String[] paths = thepath.split("\\$\\$");
        for (int i = 0;i<paths.length;i++){
            System.out.print(paths[i]+"------>"+i+"\n------------\n");
        }
    }

    public void testXrul(){
        RuleItem testRul = new RuleItem("mytest","xpath$$//*[@id=\"review-31268345\"]/div[2]/div/div[1]/div/p");
        System.out.print(testRul.get_name()+"--------->"+testRul.get_path()+"--------->"+testRul.get_tag()+"--------->"+testRul.get_path_type());
    }

    public void testXpath_rule(){
        /* 抽取单个doc数据的规则库，多个set组成map */
        Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
        data_rule_map.put("id_counter", new RuleItem("comment_counter", "html0.body0.main2.div1.div0.div1.div3.div1.h20.small0.text"));
        data_rule_map.put("id_content", new RuleItem("comment_content", "xpath$$//*[@id=\"review-31268345\"]/div[2]/div/div[1]/div/p"));
        /* 封装数据的规则库map */
        Set<DataItem> data_item_set = new HashSet<DataItem>();

        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_counter", "id_content"));
//        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_price"));
        /* 数据生成器 */
        Generator g = new Generator();
        for (Map.Entry<String, RuleItem> entry : data_rule_map.entrySet()) {
            g.set_rule(entry.getValue());
        }
        String doc_str = get_document();
//        System.out.println(doc_str);
        g.both_idea(doc_str);
        Map<String, Object> m = g.get_result();// m里封装了所有抽取的数据
        Iterator<DataItem> data_item_it = data_item_set.iterator();
        while (data_item_it.hasNext()) {
            Packer packer = new Packer(data_item_it.next(), m, data_rule_map);
            String[] result_str=packer.pack();
            System.out.println(result_str[0]);
//            assertEquals(321, result_str[0].length());
        }
    }


    public String get_document(){
        File input = new File("/home/mafu/Templates/airbnb.html");
        BufferedReader reader = null;
        String result = "";
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(input));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                result = result+tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
